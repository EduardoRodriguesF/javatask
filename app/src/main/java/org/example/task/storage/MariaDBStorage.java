package org.example.task.storage;

import java.util.ArrayList;
import java.util.List;

import org.example.task.Status;
import org.example.task.Task;

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
    public Task create(String title) {
        try (PreparedStatement statement = this.conn.prepareStatement(
                "INSERT INTO tasks (title, status) VALUES (?, ?) RETURNING id")) {

            statement.setString(1, title);
            statement.setString(2, Status.TODO.getText());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    return new Task(String.valueOf(id), title);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void delete(String id) {
        try (PreparedStatement statement = this.conn.prepareStatement(
                "DELETE FROM tasks WHERE id = ?")) {
            statement.setInt(1, Integer.parseInt(id));
            statement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(Task task) {
        try (PreparedStatement statement = this.conn.prepareStatement(
                "UPDATE tasks SET title = ?, status = ? WHERE id = ?")) {

            statement.setString(1, task.getTitle());
            statement.setString(2, task.getStatus().getText());
            statement.setInt(3, Integer.parseInt(task.getId()));

            statement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Task get(String id) {
        try (PreparedStatement statement = this.conn.prepareStatement(
                "SELECT * FROM tasks WHERE id = ?")) {

            statement.setInt(1, Integer.parseInt(id));

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return taskFromResult(result);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Task> list() {
        ArrayList<Task> tasks = new ArrayList<Task>();

        try (PreparedStatement statement = this.conn.prepareStatement(
                "SELECT * FROM tasks")) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    tasks.add(taskFromResult(result));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return tasks;
    }

    @Override
    public List<Task> listBy(Status status) {
        ArrayList<Task> tasks = new ArrayList<Task>();

        try (PreparedStatement statement = this.conn.prepareStatement(
                "SELECT * FROM tasks WHERE status = ?")) {

            statement.setString(1, status.getText());
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    tasks.add(taskFromResult(result));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
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
