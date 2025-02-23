package org.example.task.storage;

import java.util.List;

import org.example.task.Task;

public abstract class Storage {
    public abstract Task create();

    public abstract void delete();

    public abstract void update(Task task);

    public abstract List<Task> getAll();
}
