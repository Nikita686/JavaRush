package com.javarush.test.level30.lesson15.big01;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by nb on 22/07/16.
 */
public class Server
{
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    private static class Handler extends Thread{
        private Socket socket;

        public Handler(Socket socket)
        {
            this.socket = socket;
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException
        {

            while (true)
            {
                connection.send(new Message(MessageType.NAME_REQUEST));
                Message message = connection.receive();
                if (message.getType() == MessageType.USER_NAME)
                {
                    if (message.getData() != null && !message.getData().isEmpty())
                    {
                        if (connectionMap.get(message.getData())==null)
                        {
                            connectionMap.put(message.getData(), connection);
                            connection.send(new Message(MessageType.NAME_ACCEPTED));
                            return message.getData();
                        }
                    }
                }



            }
        }

        private void sendListOfUsers(Connection connection, String userName) throws IOException{

            for (String name : connectionMap.keySet()) {
                Message message = new Message(MessageType.USER_ADDED, name);

                if (!name.equals(userName)) {
                    connection.send(message);
                }
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException
        {
            while (true)
            {
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT)
                {
                   sendBroadcastMessage(new Message(MessageType.TEXT, userName + ": " + message.getData()));
                } else System.out.println("Error");

            }
        }

        @Override
        public void run()
        {
            ConsoleHelper.writeMessage("Установлено соединение с удаленным адресом" + socket.getRemoteSocketAddress());
            String clientName = "";
            try(Connection connection = new Connection(socket);)
            {
                clientName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, clientName));
                sendListOfUsers(connection, clientName);
                serverMainLoop(connection, clientName);


            } catch (IOException e) {
              ConsoleHelper.writeMessage("Произошла ошибка при обмене данными с удаленным адресом");

            }

            catch (ClassNotFoundException e) {
                ConsoleHelper.writeMessage("Произошла ошибка при обмене данными с удаленным адресом");
            }

            connectionMap.remove(clientName);

            sendBroadcastMessage(new Message(MessageType.USER_REMOVED, clientName));

            ConsoleHelper.writeMessage("Соединение с удаленным адресом закрыто");

        }



    }




    public static void sendBroadcastMessage(Message message){
        try
        {
            for (Connection connection : connectionMap.values()){
                connection.send(message);
            }
        } catch (IOException e){
            System.out.println("Сообщение не отправлено");
        }
    }

    public static void main(String[] args)
    {
        int serverPort = ConsoleHelper.readInt();
        ServerSocket serverSocket = null;

        try
        {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Server start successfully");
            while (true)
            {
                Handler handler = new Handler(serverSocket.accept());
                handler.start();
            }
        }catch (IOException e) {
            System.out.println("Error");
            try
            {
                serverSocket.close();
            } catch (IOException ex) {
                System.out.println("Error");
            }

        }

    }
}
