package com.gremlin.todo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gremlin.todo.dto.ToDoDto;
import com.gremlin.todo.model.ToDo;
import com.gremlin.todo.service.ToDoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ToDoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ToDoService toDoService;
    @Autowired
    private ObjectMapper objectMapper;

    private final String firstToDoTitle = "First ToDo title";
    private final String secondToDoTitle = "Second ToDo title";
    private final String firstToDoDescription = "First ToDo description";
    private final String secondToDoDescription = "Second ToDo description";

    @Before
    public void setUp() {
        toDoService.deleteAll();
    }

    @Test
    public void postShouldCreateNewToDo() throws Exception {
        String todoString = "{\"title\": \"a todo title\", \"description\": \"a todo description\", \"done\": false}";
        mockMvc.perform(post("/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(todoString))
        .andExpect(status().isCreated()).andDo(print());
    }

    @Test
    public void putShouldUpdateExistingToDo() throws Exception {
        String todoString = "{\"title\": \"a todo title\", \"description\": \"a todo description\", \"done\": false}";
        ToDoDto toDoDto = objectMapper.readValue(mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoString))
                .andReturn().getResponse().getContentAsString(), ToDoDto.class);

        todoString = "{\"title\": \"a todo title\", \"description\": \"a todo description\", \"done\": true}";

        toDoDto = objectMapper.readValue(mockMvc.perform(put("/"+toDoDto.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoString))
                .andReturn().getResponse().getContentAsString(), ToDoDto.class);

        assertThat(toDoDto.isDone(), is(true));

    }

    @Test
    public void getShouldReturnAllToDos() throws Exception {
        List<ToDoDto> todoList = new ArrayList<>();
        todoList.add(new ToDoDto(firstToDoTitle, firstToDoDescription));
        todoList.add(new ToDoDto(secondToDoTitle, secondToDoDescription));

        toDoService.save(todoList.get(0));
        toDoService.save(todoList.get(1));
        MvcResult result = this.mockMvc.perform(get("/")).andDo(print()).andReturn();

        List<ToDo> actual = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<ToDo>>() {});

        assertThat(actual.get(0).getTitle(), is(todoList.get(0).getTitle()));
        assertThat(actual.get(1).getDescription(), is(todoList.get(1).getDescription()));
    }

    @Test
    public void deleteShouldRemoveToDo() throws Exception {
        String todoString = "{\"title\": \"a todo title\", \"description\": \"a todo description\", \"done\": false}";
        ToDoDto toDoDto = objectMapper.readValue(mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoString))
                .andReturn().getResponse().getContentAsString(), ToDoDto.class);

        mockMvc.perform(delete("/"+toDoDto.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoString))
                .andExpect(status().isOk()).andDo(print());

        mockMvc.perform(get("/"+toDoDto.getId().toString())).andExpect(status().isNotFound()).andDo(print());

    }

}
