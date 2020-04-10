package com.gremlin.todo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.util.Assert;

@Getter
@Setter
public class ToDoDto {
    public ObjectId id;
    public  String title;
    public  String description;
    public boolean done;

    public ToDoDto(String title, String description) {
        Assert.hasText(title, "title must not be null");
        Assert.hasText(description, "description must not be null");
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id.toString();
    }
}
