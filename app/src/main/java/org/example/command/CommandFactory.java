package org.example.command;

import java.util.Arrays;

public class CommandFactory {
    public static Command getCommand(String[] args) {
        if (args.length == 0) {
            throw new Error("No arguments");
        }

        String cmd = args[0];
        args = Arrays.copyOfRange(args, 1, 6);

        return switch (cmd) {
            case "add" -> parseAdd(args);
            case "delete" -> parseDelete(args);
            case "update" -> parseUpdate(args);
            default -> throw new Error("Invalid command");
        };
    }

    private static Command parseAdd(String[] args) {
        return new AddCommand(args);
    }

    private static Command parseDelete(String[] args) {
        return new DeleteCommand(args);
    }

    private static Command parseUpdate(String[] args) {
        return new UpdateCommand(args);
    }
}
