package org.example.task.storage;

import java.util.List;

import org.example.task.Task;
import org.example.task.Status;

public abstract class Storage {
    public abstract Task create(String title) throws Error;

    public abstract void delete(String id) throws Error;

    public abstract void update(Task task) throws Error;

    public abstract Task get(String id) throws Error;

    public abstract List<Task> list() throws Error;

    public abstract List<Task> listBy(Status status) throws Error;
}
