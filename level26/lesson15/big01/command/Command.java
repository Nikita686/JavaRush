package com.javarush.test.level26.lesson15.big01.command;

import com.javarush.test.level26.lesson15.big01.exception.InterruptOperationException;

/**
 * Created by nb on 02/02/16.
 */
 interface Command
{
    void execute() throws InterruptOperationException;
}
