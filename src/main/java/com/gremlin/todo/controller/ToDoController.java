package com.gremlin.todo.controller;

import com.gremlin.todo.aspect.AdvancedAttack;
import com.gremlin.todo.aspect.Attack;
import com.gremlin.todo.dto.ToDoDto;
import com.gremlin.todo.model.ToDo;
import com.gremlin.todo.service.ToDoService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class ToDoController {

    private final ToDoService toDoService;

    @Autowired
    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @Attack(type = "ToDoController", fields = {"method=getAllToDos"})
    @GetMapping("/all")
    public Collection<ToDo> getAllToDos() {
        return toDoService.findAll();
    }

    @GetMapping
    public Collection<ToDo> findAllIncomplete() {
        return toDoService.findAllIncomplete();
    }

    @Attack(type = "ToDoController", fields = {"method=findToDoById"})
    @GetMapping(path = "{id}")
    public HttpEntity<ToDoDto> findToDoById(@PathVariable ObjectId id) {
        return new ResponseEntity<>(toDoService.findOneDtoedById(id), HttpStatus.OK);
    }

    @AdvancedAttack(type = "ToDoController", fields = {"method=createToDo"})
    @PostMapping
    public HttpEntity<ToDo> createToDo(@RequestBody ToDo toDo) {
        toDo = toDoService.save(toDo);
        return new ResponseEntity<>(toDo, HttpStatus.CREATED);

    }

    @PutMapping(path = "{id}")
    public HttpEntity<ToDo> updateToDo(@PathVariable ObjectId id, @RequestBody ToDo toDoDto) {
        toDoDto = toDoService.update(id, toDoDto);
        return new ResponseEntity<>(toDoDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public HttpEntity<String> deleteToDo(@PathVariable ObjectId id, @RequestBody(required = false) ToDo toDo) {
        toDoService.delete(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
