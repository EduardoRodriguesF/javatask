package org.example.task;

public enum Status {
    TODO("todo"),
    IN_PROGRESS("in-progress"),
    DONE("done");

    String text;

    Status(String text) {
        this.text = text;
    }

    public static Status fromString(String value) {
        for (Status status : Status.values()) {
            if (value.equals(value)) {
                return status;
            }
        }

        return null;
    }
}
