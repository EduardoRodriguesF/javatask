package org.example.task;

public class TaskUpdate {
    public final String id;

    public String title = null;
    public Status status = null;

    public TaskUpdate(String id) {
        this.id = id;
    }

    public TaskUpdate withTitle(String title) {
        this.title = title;
        return this;
    }

    public TaskUpdate withStatus(String status) {
        return this.withStatus(Status.fromString(status));
    }

    public TaskUpdate withStatus(Status status) {
        this.status = status;
        return this;
    }
}
