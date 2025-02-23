package org.example.task;

public class Task {
    final int id;
    String title;
    Status status = Status.TODO;

    public Task(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
