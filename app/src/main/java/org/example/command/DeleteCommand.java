package org.example.command;

import org.example.task.storage.Storage;

public class DeleteCommand extends Command {
	public DeleteCommand(String[] args) {
		super(args);
	}

	@Override
	public byte exec(Storage storage) {
        if (this.args.length < 1) {
            return 1;
        }

        String id = this.args[0];
        
        storage.delete(id);

        return 0;
	}
}
