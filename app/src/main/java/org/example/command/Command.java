package org.example.command;

public abstract class Command {
    protected final String[] args;

    public Command(String[] args) {
        this.args = args;
    }

    public abstract byte exec();
}
