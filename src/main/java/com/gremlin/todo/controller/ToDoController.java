package com.gremlin.todo.controller;

import com.gremlin.todo.aspect.AdvancedAttack;
import com.gremlin.todo.aspect.Attack;
import com.gremlin.todo.dto.ToDoDto;
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
    @GetMapping
    public Collection<ToDoDto> getAllToDos() {
        return toDoService.findAllDtoedBy();
    }
    @Attack(type = "ToDoController", fields = {"method=findToDoById"})
    @GetMapping(path = "{id}")
    public HttpEntity<ToDoDto> findToDoById(@PathVariable ObjectId id) {
        return new ResponseEntity<>(toDoService.findOneDtoedById(id), HttpStatus.OK);
    }

    @AdvancedAttack(type = "ToDoController", fields = {"method=createToDo"})
    @PostMapping
    public HttpEntity<ToDoDto> createToDo(@RequestBody ToDoDto toDoDto) {
        toDoDto = toDoService.save(toDoDto);
        return new ResponseEntity<>(toDoDto, HttpStatus.CREATED);

    }

    @PutMapping(path = "{id}")
    public HttpEntity<ToDoDto> updateToDo(@PathVariable ObjectId id, @RequestBody ToDoDto toDoDto) {
        toDoDto = toDoService.update(id, toDoDto);
        return new ResponseEntity<>(toDoDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public HttpEntity<String> deleteToDo(@PathVariable ObjectId id, @RequestBody ToDoDto toDoDto) {
        toDoService.delete(id, toDoDto);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
