package org.example;

import java.io.IOException;
import java.nio.file.Paths;

import org.example.command.CommandFactory;
import org.example.task.storage.JSONFileStorage;

public class App {
    public static void main(String[] args) throws IOException {
        JSONFileStorage storage = new JSONFileStorage(Paths.get("/var/tmp/tasks.json"));

        CommandFactory.getCommand(args).exec(storage);
    }
}
