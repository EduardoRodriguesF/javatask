package org.example.task;

public class Task {
    final String id;
    String title;
    Status status = Status.TODO;

    public Task(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public Task(String id, String title, String status) {
        this.id = id;
        this.title = title;
        this.status = Status.fromString(status);
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
