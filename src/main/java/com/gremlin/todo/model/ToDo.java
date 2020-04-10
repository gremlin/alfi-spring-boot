package com.gremlin.todo.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

@Data
@Document
public class ToDo {
    @Id
    private ObjectId id;
    private String title;
    private String description;
    private boolean done;

    public ToDo(String title, String description) {
        Assert.hasText(title, "title must not be null");
        Assert.hasText(description, "description must not be null");
        this.title = title;
        this.description = description;
    }

   public String getId() {
        return id != null ? id.toString() : null;
   }
}
