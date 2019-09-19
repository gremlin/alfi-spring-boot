package com.gremlin.todo.service;

import com.gremlin.todo.dto.ToDoDto;
import com.gremlin.todo.model.ToDo;
import com.gremlin.todo.repository.ToDoRepository;
import de.flapdoodle.embed.process.collections.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToDoServiceTest {

    @MockBean(name = "toDoRepository")
    private ToDoRepository toDoRepository;

    @Autowired
    private ToDoService toDoService;

    @Test
    public void findAllDtoedBy() {
        Collection<ToDoDto> toDoDtos = Collections.newArrayList(new ToDoDto("First Title", "First Description"), new ToDoDto("Second Title", "Second Description"));
        Mockito.when(toDoRepository.findAllDtoedBy()).thenReturn(toDoDtos);
        assertThat(toDoService.findAllDtoedBy(), is(toDoDtos));
        verify(toDoRepository, times(1)).findAllDtoedBy();
    }

    @Test
    public void findOneDtoedById() {
        ToDo toDo = new ToDo("A Title", "A Description");
        ToDoDto toDoDto = new ToDoDto(toDo.getTitle(), toDo.getDescription());
        BeanUtils.copyProperties(toDo, toDoDto);
        Mockito.when(toDoRepository.findOneDtoedById(toDo.getId())).thenReturn(toDoDto);

        assertThat(toDoService.findOneDtoedById(toDo.getId()), is(toDoDto));
        verify(toDoRepository, times(2)).findOneDtoedById(toDo.getId());
    }

    @Test
    public void save() {
        ToDo toDo = new ToDo("A Title", "A Description");
        ToDoDto toDoDto = new ToDoDto(toDo.getTitle(), toDo.getDescription());
        BeanUtils.copyProperties(toDo, toDoDto);
        Mockito.when(toDoRepository.save(any())).thenReturn(toDo);

        assertThat(toDoService.save(toDoDto), is(toDoDto));
        verify(toDoRepository, times(1)).save(any());
    }

    @Test
    public void update() {
        ToDo toDo = new ToDo("A Title", "A Description");
        ToDoDto toDoDto = new ToDoDto(toDo.getTitle(), toDo.getDescription());
        BeanUtils.copyProperties(toDo, toDoDto);
        Mockito.when(toDoRepository.save(any())).thenReturn(toDo);
        Mockito.when(toDoRepository.findById(any())).thenReturn(Optional.of(toDo));

        assertThat(toDoService.update(toDo.getId(), toDoDto), is(toDoDto));
        verify(toDoRepository, times(1)).findById(any());
        verify(toDoRepository, times(1)).save(any());
    }

    @Test
    public void deleteAll() {
        Mockito.doNothing().when(toDoRepository).deleteAll();
        ToDo toDo = new ToDo("A Title", "A Description");
        ToDoDto toDoDto = new ToDoDto(toDo.getTitle(), toDo.getDescription());
        BeanUtils.copyProperties(toDo, toDoDto);
        Mockito.when(toDoRepository.save(any())).thenReturn(toDo);
        toDoService.save(toDoDto);
        toDoService.deleteAll();
        verify(toDoRepository, times(1)).deleteAll();

    }

    @Test
    public void delete() {
        Mockito.doNothing().when(toDoRepository).delete(any());
        ToDo toDo = new ToDo("A Title", "A Description");
        ToDoDto toDoDto = new ToDoDto(toDo.getTitle(), toDo.getDescription());
        BeanUtils.copyProperties(toDo, toDoDto);
        Mockito.when(toDoRepository.save(any())).thenReturn(toDo);
        Mockito.when(toDoRepository.findById(any())).thenReturn(Optional.of(toDo));
        toDoService.save(toDoDto);
        toDoService.delete(toDoDto.getId(), toDoDto);
        verify(toDoRepository, times(1)).delete(any());

    }
}