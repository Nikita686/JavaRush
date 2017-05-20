package com.javarush.test.level30.lesson15.big01.client;

import com.javarush.test.level30.lesson15.big01.ConsoleHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by nb on 26/07/16.
 */
public class BotClient extends Client
{
    public static void main(String[] args)
    {
        BotClient botClient = new BotClient();
        botClient.run();
    }

    @Override
    protected SocketThread getSocketThread()
    {
        return new BotSocketThread();
    }

    @Override
    protected boolean shouldSentTextFromConsole()
    {
        return false;
    }

    @Override
    protected String getUserName()
    {
        return new Date().toString() + "_bot_" + (int)(Math.random()*100);
    }

    public class BotSocketThread extends SocketThread{

        @Override
        protected void processIncomingMessage(String message)
        {
            ConsoleHelper.writeMessage(message);
            if (message.contains(":")){
                String[] temp = message.split(": ");
                Date current = new GregorianCalendar().getTime();
                SimpleDateFormat date = new SimpleDateFormat("d.MM.YYYY");
                SimpleDateFormat time = new SimpleDateFormat("H:mm:ss");
                SimpleDateFormat day = new SimpleDateFormat("d");
                SimpleDateFormat month = new SimpleDateFormat("MMMM");
                SimpleDateFormat year = new SimpleDateFormat("YYYY");
                SimpleDateFormat hour = new SimpleDateFormat("H");
                SimpleDateFormat minute = new SimpleDateFormat("m");
                SimpleDateFormat second = new SimpleDateFormat("s");

                if (temp[1].equals("дата")){
                     sendTextMessage("Информация для " + temp[0] + ": " + date.format(current));
                }

                if (temp[1].equals("день")){
                    sendTextMessage("Информация для " + temp[0] + ": " + day.format(current));
                }

                if (temp[1].equals("месяц")){
                    sendTextMessage("Информация для " + temp[0] + ": " + month.format(current));
                }

                if (temp[1].equals("год")){
                    sendTextMessage("Информация для " + temp[0] + ": " + year.format(current));
                }

                if (temp[1].equals("время")){
                    sendTextMessage("Информация для " + temp[0] + ": " + time.format(current));
                }

                if (temp[1].equals("час")){
                    sendTextMessage("Информация для " + temp[0] + ": " + hour.format(current));
                }

                if (temp[1].equals("минуты")){
                    sendTextMessage("Информация для " + temp[0] + ": " + minute.format(current));
                }

                if (temp[1].equals("секунды")){
                    sendTextMessage("Информация для " + temp[0] + ": " + second.format(current));
                }
            }
        }

        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException
        {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();

        }
    }
}
