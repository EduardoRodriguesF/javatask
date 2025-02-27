package org.example.task.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.example.task.Status;
import org.example.task.Task;
import org.json.*;

public class JSONFileStorage extends Storage {
    HashMap<String, Task> tasks;
    Path path;

    public JSONFileStorage(Path path) throws IOException {
        this.path = path;
        this.tasks = new HashMap<String, Task>();

        System.out.println(path);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }

        Stream<String> content = Files.lines(path);
        JSONObject map;
        try {
            var str = content.collect(Collectors.joining(","));
            map = new JSONObject(str);
        } catch (JSONException e) {
            content.close();
            return;
        } finally {
            content.close();
        }

        for (var keys = map.keys(); keys.hasNext();) {
            var k = keys.next();
            var obj = map.getJSONObject(k);

            Task task = new Task(k, obj.getString("title"));
            task.setStatus(Status.fromString(obj.getString("status")));

            this.tasks.putIfAbsent(k, task);
            tasks.put(k, task);
        }
    }

    @Override
    public Task create(String title) {
        var task = new Task(this.newId(), title);
        tasks.put(task.getId(), task);

        this.saveFile();

        return task;
    }

    @Override
    public void delete(String id) {
        this.tasks.remove(id);

        this.saveFile();
    }

    @Override
    public void update(Task task) {
        // TODO check if it is already updated when pulled for updating.
        this.tasks.put(task.getId(), task);

        this.saveFile();
    }

    @Override
    public List<Task> list() {
        return new ArrayList<Task>(this.tasks.values());
    }

    @Override
    public List<Task> listBy(Status status) {
        var filtered = new ArrayList<Task>();

        for (var i = 0; i < this.tasks.size(); i++) {
            Task task = this.tasks.get(String.valueOf(i));

            if (task.getStatus() == status) {
                filtered.add(task);
            }
        }

        return filtered;
    }

    private String newId() {
        int id = 0;

        var it = this.tasks.keySet().iterator();
        while (it.hasNext()) {
            int compare = Integer.parseInt(it.next());
            if (compare > id) id = compare;
        }

        return String.valueOf(id + 1);
    }

    private void saveFile() {
        try {
            var map = new JSONObject(this.tasks);
            Files.write(this.path, map.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
