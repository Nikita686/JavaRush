package com.javarush.test.level26.lesson15.big01;

import com.javarush.test.level26.lesson15.big01.command.CommandExecutor;
import com.javarush.test.level26.lesson15.big01.exception.InterruptOperationException;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by nb on 31/01/16.
 */
public class CashMachine
{
    public static final String RESOURCE_PATH = "com.javarush.test.level26.lesson15.big01.resources.";

    public static void main(String[] args)
    {
        Locale.setDefault(Locale.ENGLISH);
        ResourceBundle res = ResourceBundle.getBundle(RESOURCE_PATH + "common_en");

        try
        {
            CommandExecutor.execute(Operation.LOGIN);
            Operation operation;

            do
            {
                ConsoleHelper.writeMessage(res.getString("choose.operation") + " \n" +
                        res.getString("operation.INFO") + ": 1;\n" +
                        res.getString("operation.DEPOSIT") + ": 2;\n" +
                        res.getString("operation.WITHDRAW") + ": 3;\n" +
                        res.getString("operation.EXIT") + ": 4");
                operation = ConsoleHelper.askOperation();
                CommandExecutor.execute(operation);
            }
            while (operation != Operation.EXIT);

        } catch (InterruptOperationException e){
            ConsoleHelper.writeMessage(res.getString("the.end"));
        }



    }
}
