package org.example.task.storage;

import java.util.LinkedList;
import java.util.List;

import org.example.task.Status;
import org.example.task.Task;
import org.example.task.TaskUpdate;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDBStorage extends Storage {
    Connection conn;

    public MariaDBStorage(String url, String user, String password) throws SQLException {
        this.conn = DriverManager.getConnection(url, user, password);
    }

    @Override
    public Task create(String title) throws Error {
        try (PreparedStatement statement = this.conn.prepareStatement(
                "INSERT INTO tasks (title, status) VALUES (?, ?) RETURNING id")) {

            statement.setString(1, title);
            statement.setString(2, Status.TODO.getText());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    return new Task(String.valueOf(id), title);
                }
            } catch (SQLException e) {
                throw new Error(e.getMessage());
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }

        return null;
    }

    @Override
    public void delete(String id) throws Error {
        try (PreparedStatement statement = this.conn.prepareStatement(
                "DELETE FROM tasks WHERE id = ?")) {
            statement.setInt(1, Integer.parseInt(id));
            statement.execute();
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    @Override
    public void update(TaskUpdate task) throws Error {
        var builder = new StringBuilder("UPDATE tasks SET");

        if (task.title != null) {
            builder.append(" title = ?");

            if (task.status != null) {
                builder.append(',');
            }
        }

        if (task.status != null) {
            builder.append(" status = ?");
        }

        builder.append(" WHERE id = ?");


        try (PreparedStatement statement = this.conn.prepareStatement(builder.toString())) {

            var i = 0;

            if (task.title != null)
                statement.setString(++i, task.title);

            if (task.status != null)
                statement.setString(++i, task.status.getText());

            statement.setInt(++i, Integer.parseInt(task.id));

            statement.executeQuery();
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }
    }

    @Override
    public Task get(String id) throws Error {
        try (PreparedStatement statement = this.conn.prepareStatement(
                "SELECT * FROM tasks WHERE id = ?")) {

            statement.setInt(1, Integer.parseInt(id));

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return taskFromResult(result);
                }
            } catch (SQLException e) {
                throw new Error(e.getMessage());
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }

        return null;
    }

    @Override
    public List<Task> list() throws Error {
        List<Task> tasks = new LinkedList<Task>();

        try (PreparedStatement statement = this.conn.prepareStatement(
                "SELECT * FROM tasks")) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    tasks.add(taskFromResult(result));
                }
            } catch (SQLException e) {
                throw new Error(e.getMessage());
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }

        return tasks;
    }

    @Override
    public List<Task> listBy(Status status) throws Error {
        List<Task> tasks = new LinkedList<Task>();

        try (PreparedStatement statement = this.conn.prepareStatement(
                "SELECT * FROM tasks WHERE status = ?")) {

            statement.setString(1, status.getText());
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    tasks.add(taskFromResult(result));
                }
            } catch (SQLException e) {
                throw new Error(e.getMessage());
            }
        } catch (SQLException e) {
            throw new Error(e.getMessage());
        }

        return tasks;
    }

    private Task taskFromResult(ResultSet result) throws SQLException {
        int id = result.getInt("id");
        String title = result.getString("title");
        String status = result.getString("status");

        Task task = new Task(String.valueOf(id), title);
        task.setStatus(Status.fromString(status));

        return task;
    }
}
