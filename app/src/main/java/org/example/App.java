package org.example;

import java.io.IOException;

import org.example.command.CommandFactory;
import org.example.task.storage.DynamoDBStorage;

public class App {
    public static void main(String[] args) throws IOException {
        DynamoDBStorage storage = new DynamoDBStorage("tasks");

        CommandFactory.getCommand(args).exec(storage);
    }
}
