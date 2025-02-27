package org.example.command;

import org.example.task.storage.Storage;

public class UpdateCommand extends Command {
	public UpdateCommand(String[] args) {
		super(args);
	}

    @Override
    public byte exec(Storage storage) {
        if (this.args.length < 2) {
            throw new Error("Invalid arguments");
        }

        var id = this.args[0];
        var title = this.args[1];

        var task = storage.get(id);
        task.setTitle(title);

        storage.update(task);

        return 0;
    }
}
