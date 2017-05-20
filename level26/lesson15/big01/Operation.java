package com.javarush.test.level26.lesson15.big01;

/**
 * Created by nb on 31/01/16.
 */
public enum Operation
{
    INFO, DEPOSIT, WITHDRAW, EXIT, LOGIN;

    public static Operation getAllowableOperationByOrdinal(Integer i){

        Operation operation;

        switch (i){

            case 0: throw new IllegalArgumentException();
            case 1: operation = Operation.INFO;
                break;
            case 2: operation = Operation.DEPOSIT;
                break;
            case 3: operation = Operation.WITHDRAW;
                break;
            case 4: operation = Operation.EXIT;
                break;
            default: throw new IllegalArgumentException();
        }


        return operation;
    }
}
