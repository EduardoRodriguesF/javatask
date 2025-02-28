package org.example.command;

import java.util.List;

import org.example.task.Status;
import org.example.task.Task;
import org.example.task.storage.Storage;

public class ListCommand extends Command {
    public ListCommand(String[] args) {
        super(args);
    }

    @Override
    public byte exec(Storage storage) {
        List<Task> tasks;
        Status status;

        if (this.args[0] != null && (status = Status.fromString(this.args[0])) != null) {
            tasks = storage.listBy(status);
        } else {
            tasks = storage.list();
        }

        for (Task task : tasks) {
            System.out.println(task.getId() + '\t' + task.getTitle() + '\t' + task.getStatus());
        }

        return 0;
    }
}
