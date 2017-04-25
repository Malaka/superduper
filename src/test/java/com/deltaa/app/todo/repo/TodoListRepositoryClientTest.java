package com.deltaa.app.todo.repo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.deltaa.app.todo.SuperDuperApplication;
import com.deltaa.app.todo.entity.TodoItem;
import com.deltaa.app.todo.entity.TodoList;
import com.deltaa.app.todo.entity.TodoStatus;
import com.google.common.collect.Lists;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author: malaka
 * Date: 4/25/17
 * Time: 11:24 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SuperDuperApplication.class)
public class TodoListRepositoryClientTest {

	private MediaType contentType = new MediaType("application", "hal+json", Charset.forName("UTF-8"));

	private List<TodoList> todoLists = Lists.newArrayList();

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	private MockMvc mockMvc;

	@Autowired
	private TodoListRepository todoListRepo;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
			.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
			.findAny()
			.orElse(null);

		assertNotNull("the JSON message converter must not be null",
			this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void clearDb() {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		todoListRepo.deleteAll();

		TodoList listOne = new TodoList("education", "things to do in education");
		listOne.addTodoItem(new TodoItem("java", "study java", TodoStatus.PENDING, ZonedDateTime.now()));
		listOne.addTodoItem(new TodoItem("spring", "learn spring", TodoStatus.PENDING, ZonedDateTime.now()));
		todoLists.add(todoListRepo.save(listOne));
	}

	@Test
	public void userNotFound() throws Exception {
		mockMvc.perform(post("/george/bookmarks/")
			.content(this.json(new TodoItem()))
			.contentType(contentType))
			.andExpect(status().isNotFound());
	}

	@Test
	public void readTodoListCollection() throws Exception {
		mockMvc.perform(get("/api/todoLists"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$._embedded.todoLists", hasSize(1)))
			.andExpect(jsonPath("$._embedded.todoLists[0].id", is(this.todoLists.get(0).getId().intValue())))
			.andExpect(jsonPath("$._embedded.todoLists[0].name", is(this.todoLists.get(0).getName())))
			.andExpect(jsonPath("$._embedded.todoLists[0].description", is(this.todoLists.get(0).getDescription())));

		mockMvc.perform(get(String.format("/api/todoLists/%d/todoItems", this.todoLists.get(0).getId())))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$._embedded.todoItems", hasSize(2)))
			.andExpect(jsonPath("$._embedded.todoItems[0].name", is("java")))
			.andExpect(jsonPath("$._embedded.todoItems[1].name", is("spring")));
	}

	@Test
	public void addRemoveTodoList() throws Exception {
		String todoJson = json(new TodoList("newTodoList", "added for testing"));
		this.mockMvc.perform(post("/api/todoLists")
			.contentType(contentType)
			.content(todoJson))
			.andExpect(status().isCreated());

		// newly added item the count should be 2
		mockMvc.perform(get("/api/todoLists"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$._embedded.todoLists", hasSize(2)));


		this.mockMvc.perform(delete("/api/todoLists/" + this.todoLists.get(0).getId()))
			.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/todoLists"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$._embedded.todoLists", hasSize(1)));
	}


	@Test
	public void addRemoveTodoItem() throws Exception {

		String parentRef = "http://localhost:8080/api/todoLists/" + this.todoLists.get(0).getId();

		String todoJson = "{\n" +
			"  \"name\" : \"todItem\",\n" +
			"  \"todoList\" : \"" + parentRef + "\",\n" +
			"  \"description\" : \"desc\",\n" +
			"  \"status\" : \"PENDING\",\n" +
			"  \"dateOfCompletion\" : \"2017-04-25T19:23:18.347+01:00\",\n" +
			"  \"tags\" : null\n" +
			"}";

		this.mockMvc.perform(post("/api/todoItems")
			.contentType(contentType)
			.content(todoJson))
			.andExpect(status().isCreated());

		mockMvc.perform(get(String.format("/api/todoLists/%d/todoItems", this.todoLists.get(0).getId())))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$._embedded.todoItems", hasSize(3)));

		this.mockMvc.perform(delete("/api/todoItems/" + this.todoLists.get(0).getTodoItems().iterator().next().getId()))
			.andExpect(status().isNoContent());

		mockMvc.perform(get(String.format("/api/todoLists/%d/todoItems", this.todoLists.get(0).getId())))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$._embedded.todoItems", hasSize(2)));
	}

	private String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(
			o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}


}