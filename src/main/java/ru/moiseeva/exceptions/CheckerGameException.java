package ru.moiseeva.exceptions;

public class CheckerGameException extends Exception{
    public CheckerGameException(String message) {
        System.out.println(message);
    }
}
