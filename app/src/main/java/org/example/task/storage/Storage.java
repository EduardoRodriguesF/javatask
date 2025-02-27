package org.example.task.storage;

import java.util.List;

import org.example.task.Task;
import org.example.task.Status;

public abstract class Storage {
    public abstract Task create(String title);

    public abstract void delete(String id);

    public abstract void update(Task task);

    public abstract Task get(String id);

    public abstract List<Task> list();

    public abstract List<Task> listBy(Status status);
}
