package org.example.command;

import org.example.task.storage.Storage;

public class AddCommand extends Command {
	public AddCommand(String[] args) {
		super(args);
	}

	@Override
	public byte exec(Storage storage) throws Error {
        if (this.args.length < 1) {
            return 1;
        }

        String item = this.args[0];
        
        storage.create(item);
        System.out.println("Task added successfully (ID: 1)");

        return 0;
	}
}
