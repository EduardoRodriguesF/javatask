package org.example.task.storage;

import java.util.LinkedList;
import java.util.List;

import org.example.task.Status;
import org.example.task.Task;
import org.example.task.TaskUpdate;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

public class DynamoDBStorage extends Storage {
    DynamoDB dynamoDB;
    Table table;

    public DynamoDBStorage(String tableName) {
        AWSCredentialsProvider credentials = new ProfileCredentialsProvider();

        var client = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentials)
                .withRegion(Regions.US_EAST_1)
                .build();

        dynamoDB = new DynamoDB(client);
        table = dynamoDB.getTable(tableName);
    }

    @Override
    public Task create(String title) throws Error {
        var id = generateIdFromCounter();
        var task = new Task(String.valueOf(id), title);

        Item item = new Item()
                .withPrimaryKey("id", task.getId())
                .withString("title", task.getTitle())
                .withString("status", task.getStatus().getText());

        this.table.putItem(item);

        return task;
    }

    @Override
    public void delete(String id) throws Error {
        this.table.deleteItem("id", id);
    }

    @Override
    public void update(TaskUpdate task) throws Error {
        var update = new UpdateItemSpec()
                .withPrimaryKey("id", task.id);

        if (task.title != null) {
            update = update.withAttributeUpdate(
                    new AttributeUpdate("title").put(task.title));
        }

        if (task.status != null) {
            update = update.withAttributeUpdate(
                    new AttributeUpdate("status").put(task.status.getText()));
        }

        this.table.updateItem(update);
    }

    @Override
    public Task get(String id) throws Error {
        Item item = this.table.getItem("id", id);

        String title = item.getString("title");
        String status = item.getString("status");

        var task = new Task(id, title);
        task.setStatus(Status.fromString(status));

        return task;
    }

    @Override
    public List<Task> list() throws Error {
        List<Task> list = new LinkedList<Task>();

        this.table.scan().forEach((Item item) -> {
            String id = item.getString("id");
            String title = item.getString("title");
            String statusStr = item.getString("status");

            var task = new Task(id, title, statusStr);
            list.add(task);
        });

        return list;
    }

    @Override
    public List<Task> listBy(Status status) throws Error {
        List<Task> list = new LinkedList<Task>();

        ScanFilter filter = new ScanFilter("status");
        filter = filter.eq(status.getText());

        Index index = this.table.getIndex("status-index");
        
        index.scan(filter).forEach((Item item) -> {
            String id = item.getString("id");
            String title = item.getString("title");
            String statusStr = item.getString("status");

            var task = new Task(id, title, statusStr);
            list.add(task);
        });

        return list;
    }

    private int generateIdFromCounter() {
        Table counterTable = this.dynamoDB.getTable("counters");

        var pk = new PrimaryKey("name", "tasks");
        var attrUpdate = new AttributeUpdate("value").addNumeric(1);

        var update = new UpdateItemSpec()
                .withPrimaryKey(pk)
                .withAttributeUpdate(attrUpdate)
                .withReturnValues(ReturnValue.UPDATED_NEW);

        UpdateItemOutcome outcome = counterTable.updateItem(update);

        return outcome.getItem().getInt("value");
    }
}
