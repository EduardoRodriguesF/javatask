package org.example.command;

import org.example.task.storage.Storage;

public abstract class Command {
    protected final String[] args;

    public Command(String[] args) {
        this.args = args;
    }

    public abstract byte exec(Storage storage) throws Error;
}
