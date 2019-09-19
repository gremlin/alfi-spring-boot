package com.gremlin.todo.dto;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.util.Assert;

@Data
public class ToDoDto {
    public ObjectId id = null;
    public  String title;
    public  String description;
    public boolean done = false;

    public ToDoDto(String title, String description) {
        Assert.hasText(title, "title must not be null");
        Assert.hasText(description, "description must not be null");
        this.title = title;
        this.description = description;
    }
}
