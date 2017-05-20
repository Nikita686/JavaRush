package com.javarush.test.level30.lesson15.big01.client;

import com.javarush.test.level30.lesson15.big01.ConsoleHelper;
import com.javarush.test.level30.lesson15.big01.Connection;
import com.javarush.test.level30.lesson15.big01.Message;
import com.javarush.test.level30.lesson15.big01.MessageType;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by nb on 26/07/16.
 */
public class Client
{
    protected Connection connection;

    private volatile boolean clientConnected = false;

    public static void main(String[] args)
    {
        Client client = new Client();
        client.run();
    }

    public void run() {

        // Создавать новый сокетный поток с помощью метода getSocketThread
        SocketThread socketThread = getSocketThread();
        // Помечать созданный поток как daemon, это нужно для того, чтобы при выходе
        // из программы вспомогательный поток прервался автоматически.
        socketThread.setDaemon(true);
        // Запустить вспомогательный поток
        socketThread.start();

        // Заставить текущий поток ожидать, пока он не получит нотификацию из другого потока
        try {
            synchronized (this) {
                this.wait();
            }
        } catch (InterruptedException e) {
            ConsoleHelper.writeMessage("Ошибка");
            return;
        }

        //После того, как поток дождался нотификации, проверь значение clientConnected
        if (clientConnected) {
            ConsoleHelper.writeMessage("Соединение установлено. Для выхода наберите команду 'exit'.");

            //Считывай сообщения с консоли пока клиент подключен. Если будет введена команда 'exit', то выйди из цикла
            while (clientConnected) {
                String message;
                if (!(message = ConsoleHelper.readString()).equals("exit")) {
                    if (shouldSentTextFromConsole()) {
                        sendTextMessage(message);
                    }
                } else {
                    return;
                }
            }
        }
        else {
            ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");
        }
    }


    protected String getServerAddress(){
        ConsoleHelper.writeMessage("Введите адрес сервера: ");
        return ConsoleHelper.readString();
    }

    protected int getServerPort(){
        ConsoleHelper.writeMessage("Введите адрес порта сервера: ");
        return ConsoleHelper.readInt();
    }

    protected String getUserName(){
        ConsoleHelper.writeMessage("Введите имя пользователя");
        return ConsoleHelper.readString();
    }

    protected boolean shouldSentTextFromConsole(){
        return true;
    }

    protected SocketThread getSocketThread(){
        return new SocketThread();
    }

    protected void sendTextMessage(String text){
        try
        {
            connection.send(new Message(MessageType.TEXT, text));
        } catch (IOException e){
            System.out.println("Error");
            clientConnected = false;
        }
    }

    public class SocketThread extends Thread{

        @Override
        public void run()
        {
            String serverAddress = getServerAddress();
            int port = getServerPort();

            try
            {
                Socket socket = new Socket(serverAddress, port);
                Client.this.connection = new Connection(socket);
                clientHandshake();
                clientMainLoop();

            } catch (IOException e) {
                notifyConnectionStatusChanged(false);
            }

            catch (ClassNotFoundException e){
                notifyConnectionStatusChanged(false);
            }
        }

        protected void processIncomingMessage(String message){
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String userName){
            ConsoleHelper.writeMessage(userName + " присоединился к чату");
        }

        protected void informAboutDeletingNewUser(String userName){
            ConsoleHelper.writeMessage(userName + " покинул чат");
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected){
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this){
                Client.this.notify();
            }
        }

        protected void clientHandshake() throws IOException, ClassNotFoundException
        {
            while (true)
            {
                Message message = connection.receive();
                switch (message.getType())
                {

                    case NAME_REQUEST:
                    {

                        String userName = getUserName();
                        connection.send(new Message(MessageType.USER_NAME, userName));
                        break;
                    }

                    case NAME_ACCEPTED:
                    {

                        notifyConnectionStatusChanged(true);
                        return;
                    }

                    default:
                    {
                        throw new IOException("Unexpected MessageType");
                    }
                }
            }
        }

        protected void clientMainLoop() throws IOException, ClassNotFoundException{

            while (true){
                Message message = connection.receive();

                switch (message.getType()) {
                    case TEXT:
                        processIncomingMessage(message.getData());
                        break;

                    case USER_ADDED:
                        informAboutAddingNewUser(message.getData());
                        break;

                    case USER_REMOVED:
                        informAboutDeletingNewUser(message.getData());
                        break;

                        default: throw new IOException("Unexpected MessageType");
                }
            }
        }
    }
}
