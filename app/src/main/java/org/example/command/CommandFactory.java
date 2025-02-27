package org.example.command;

import java.util.Arrays;

public class CommandFactory {
    public static Command getCommand(String[] args) {
        if (args.length == 0) {
            // TODO: Error
            throw new Error("No arguments");
        }

        String cmd = args[0];
        args = Arrays.copyOfRange(args, 1, 6);

        return switch (cmd) {
            case "add" -> parseAdd(args);
            default -> parseAdd(args); // TODO: Should error
        };
    }

    private static Command parseAdd(String[] args) {
        return new AddCommand(args);
    }
}
