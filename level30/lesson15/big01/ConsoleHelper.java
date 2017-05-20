package com.javarush.test.level30.lesson15.big01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by nb on 22/07/16.
 */
public class ConsoleHelper
{
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message){
        System.out.println(message);
    }

    public static String readString(){

        String text = "";
        while (true){
            try
            {
                text = bufferedReader.readLine();
                break;
            } catch (IOException e){
                System.out.println("Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
                continue;
            }
        }
        return text;
    }

    public static int readInt(){
        int a;
        while (true){
            try
            {
                a = Integer.parseInt(readString());
                break;
            }catch (NumberFormatException e){
                System.out.println("Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
                continue;
            }
        }
        return a;
    }
}
