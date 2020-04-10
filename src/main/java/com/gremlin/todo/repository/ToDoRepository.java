package com.gremlin.todo.repository;

import com.gremlin.todo.dto.ToDoDto;
import com.gremlin.todo.model.ToDo;
import com.gremlin.todo.projection.ToDoProjection;
import com.gremlin.todo.projection.ToDoSummary;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface ToDoRepository extends CrudRepository<ToDo, ObjectId> {

    Collection<ToDoProjection> findAllProjectedBy();
    Collection<ToDoSummary> findAllSummarizedBy();
    Collection<ToDoDto> findAllDtoedBy();
    Collection<ToDo> findAll();
    Collection<ToDo> findByDoneFalse();
    ToDoDto findOneDtoedById(ObjectId id);
    <T> Collection<T> findByTitle(String title, Class<T> projection);
    ToDoProjection findProjectedById(ObjectId id);
    <T> T findProjectedById(ObjectId id, Class<T> projection);
    Page<ToDoProjection> findPagedProjectedBy(Pageable pageable);

}
