package com.gremlin.todo.service;

import com.gremlin.todo.dto.ToDoDto;
import com.gremlin.todo.exception.NotFoundException;
import com.gremlin.todo.model.ToDo;
import com.gremlin.todo.repository.ToDoRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Optional;

@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;

    @Autowired
    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    public Collection<ToDoDto> findAllDtoedBy() {
        return toDoRepository.findAllDtoedBy();
    }

    public Collection<ToDo> findAll() {
        return toDoRepository.findAll();
    }

    public ToDoDto findOneDtoedById(ObjectId id) {
        ToDoDto toDoDto = toDoRepository.findOneDtoedById(id);
        if (toDoDto == null) throw new NotFoundException(String.format("ToDo with id %s not found!", id));
        return toDoRepository.findOneDtoedById(id);
    }

    public ToDo save(ToDo toDo) {
        Assert.hasText(toDo.getTitle(), "title must not be null");
        Assert.hasText(toDo.getDescription(), "description must not be null");
        toDo =  toDoRepository.save(toDo);
        if (toDo == null) throw new IllegalStateException("Unable to persist ToDo to the database");
        return toDo;
    }

    public ToDo update(ObjectId id, ToDo toDoDto) {
        Optional<ToDo> optionalToDo = toDoRepository.findById(id);
//        ToDo toDo = optionalToDo.orElseThrow(IllegalStateException::new);
//        BeanUtils.copyProperties(toDoDto, toDo, "id");
//        toDo = toDoRepository.save(toDo);
//        BeanUtils.copyProperties(toDo, toDoDto);
        return toDoRepository.save(toDoDto);
    }

    public void deleteAll() {
        toDoRepository.deleteAll();
    }

    public void delete(ObjectId id) {
        Optional<ToDo> optionalToDo = toDoRepository.findById(id);
        ToDo toDo = optionalToDo.orElseThrow(IllegalStateException::new);
        toDoRepository.delete(toDo);
    }

    public Collection<ToDo> findAllIncomplete() {
        return toDoRepository.findByDoneFalse();
    }
}
