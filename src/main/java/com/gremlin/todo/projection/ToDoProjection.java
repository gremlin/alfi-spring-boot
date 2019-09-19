package com.gremlin.todo.projection;

import org.bson.types.ObjectId;

public interface ToDoProjection {
    ObjectId getId();
    String getTitle();
    String getDescription();
    boolean isDone();
}
