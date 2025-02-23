package org.example;

import org.example.command.CommandFactory;

public class App {
    public static void main(String[] args) {
        CommandFactory.getCommand(args).exec();
    }
}
