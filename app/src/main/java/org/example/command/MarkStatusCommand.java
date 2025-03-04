package org.example.command;

import org.example.task.Status;
import org.example.task.storage.Storage;

public class MarkStatusCommand extends Command {
    private Status status;

    public MarkStatusCommand(String[] args, Status status) {
        super(args);
        this.status = status;
    }

    @Override
    public byte exec(Storage storage) throws Error {
        if (this.args.length < 1 || this.args[0] == null) {
            return 1;
        }

        String id = this.args[0];
        
        var task = storage.get(id);
        task.setStatus(this.status);

        storage.update(task);

        return 0;
    }
}
