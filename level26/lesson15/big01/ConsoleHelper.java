package com.javarush.test.level26.lesson15.big01;



import com.javarush.test.level26.lesson15.big01.exception.InterruptOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ResourceBundle;

/**
 * Created by nb on 31/01/16.
 */
public class ConsoleHelper
{
    private static ResourceBundle res = ResourceBundle.getBundle(CashMachine.RESOURCE_PATH + "common_en");

    static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage (String message){
        System.out.println(message);
    }

    public static String readString() throws InterruptOperationException {

        String string = "";

        try
        {
            string = bufferedReader.readLine();
        } catch (IOException ignore){}

        if (res.getString("operation.EXIT").equalsIgnoreCase(string)) throw new InterruptOperationException();

        return string;
    }

   public static String askCurrencyCode() throws InterruptOperationException{

        String s;
        writeMessage(res.getString("choose.currency.code"));
        while (true){
            s = readString();
            if (s.length()==3) break;
            else
            writeMessage(res.getString("invalid.data"));
        }
        s=s.toUpperCase();
        return s;
    }

   public static String[] getValidTwoDigits(String currencyCode) throws InterruptOperationException{
        String[] array;
        writeMessage(res.getString("choose.denomination.and.count.format"));

        while (true){
            String s = readString();
            array = s.split(" ");
            int k;
            int l;
            try{
                 k = Integer.parseInt(array[0]);
                l = Integer.parseInt(array[1]);
            }catch (Exception e){
                writeMessage(res.getString("invalid.data"));
                continue;
            }
            if (k <= 0 || l <= 0 || array.length > 2){
                writeMessage(res.getString("invalid.data"));
                continue;
            } else break;
        }

        return array;
    }

    public static Operation askOperation() throws InterruptOperationException{
        Operation operation;
        String text;
        while (true)
        {
            text = readString();
            int i = Integer.parseInt(text);
            try
            {
                operation = Operation.getAllowableOperationByOrdinal(i);
                break;
            } catch (Exception e){
                writeMessage(res.getString("invalid.data"));
                continue;
            }
        }


        return operation;
    }
}
