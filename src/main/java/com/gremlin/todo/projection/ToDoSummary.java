package com.gremlin.todo.projection;

import org.springframework.beans.factory.annotation.Value;

public interface ToDoSummary {

    @Value("#{target.id + '\\n' + target.title + '\\n' + target.description + '\\n' + target.done }")
    String getToDoSummary();
}
