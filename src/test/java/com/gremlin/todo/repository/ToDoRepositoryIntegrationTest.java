package com.gremlin.todo.repository;

import com.gremlin.todo.dto.ToDoDto;
import com.gremlin.todo.model.ToDo;
import com.gremlin.todo.projection.ToDoProjection;
import com.gremlin.todo.projection.ToDoSummary;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.projection.TargetAware;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ToDoRepositoryIntegrationTest {

	@Configuration
	@EnableAutoConfiguration
	static class Config {}

	@Autowired
	private ToDoRepository todos;

	private ToDo thingOne, thingTwo;
	private final String thingOneTitle = "Thing One";
	private final String thingTwoTitle = "Thing Two";
	private final String thingOneDescription = "Description for Thing One";
	private final String thingTwoDescription = "Description for Thing Two";

	@Before
	public void setUp() {
		todos.deleteAll();
		this.thingOne = todos.save(new ToDo(thingOneTitle, thingOneDescription));
		this.thingTwo = todos.save(new ToDo(thingTwoTitle, thingTwoDescription));
	}

	@Test
	public void projectsEntityIntoInterface() {
		Collection<ToDoProjection> result = todos.findAllProjectedBy();
		assertThat(result, hasSize(2));
	}

	@Test
	public void projectsToDto() {
		Collection<ToDoDto> result = todos.findAllDtoedBy();
		assertThat(result, hasSize(2));
	}

	@Test
	public void projectsDynamically() {
		Collection<ToDoProjection> result = todos.findByTitle(thingOneTitle, ToDoProjection.class);
		assertThat(result, hasSize(1));
		assertThat(result.iterator().next().getTitle(), is(thingOneTitle));
	}

	@Test
	public void projectsIndividualDynamically() {
		ToDoSummary result = todos.findProjectedById(new ObjectId(thingOne.getId()), ToDoSummary.class);
		assertThat(result, is(notNullValue()));
		assertThat(result.getToDoSummary(), containsString(String.format("%s\\n%s\\n%s", this.thingOne.getTitle(), this.thingOne.getDescription(), this.thingOne.isDone())));
	}

	@Test
	public void projectIndividualInstance() {
		ToDoProjection result = todos.findProjectedById(new ObjectId(thingOne.getId()));
		assertThat(result, is(notNullValue()));
		assertThat(result.getTitle(), is(this.thingOneTitle));
		assertThat(((TargetAware) result).getTarget(), is(instanceOf(ToDo.class)));
	}

	@Test
	public void supportsProjectionInCombinationWithPagination() {
		Page<ToDoProjection> page = todos.findPagedProjectedBy(PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "description")));
		assertThat(page.getContent().get(0).getTitle(),  is(this.thingOneTitle));
	}

}
