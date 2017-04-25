package com.deltaa.app.todo.repo;

import java.time.ZonedDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.deltaa.app.todo.SuperDuperApplication;
import com.deltaa.app.todo.entity.TodoItem;
import com.deltaa.app.todo.entity.TodoList;
import com.deltaa.app.todo.entity.TodoStatus;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author: malaka
 * Date: 4/25/17
 * Time: 11:24 AM
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SuperDuperApplication.class)
public class TodoListRepositoryTest {

	@Autowired
	private TodoListRepository todoListRepo;

	@Before
	@After
	public void clearDb() {
		todoListRepo.deleteAll();
	}

	@Test
	public void testTodoListSaving() {

		TodoList listOne = new TodoList("education", "things to do in education");
		TodoItem item1 = new TodoItem("java", "study java", TodoStatus.PENDING, ZonedDateTime.now());
		TodoItem item2 = new TodoItem("spring", "learn spring", TodoStatus.PENDING, ZonedDateTime.now());
		listOne.addTodoItem(item1);
		listOne.addTodoItem(item2);

		todoListRepo.save(listOne);

		Iterable<TodoList> result = todoListRepo.findAll();
		assertThat(result, is(iterableWithSize(1)));

		TodoList fromDB = result.iterator().next();
		assertEquals(listOne, fromDB);

		assertNotNull("id should be populated for fromDB object", fromDB.getId());
		assertEquals(listOne.getTodoItems().size(), 2);


		listOne.getTodoItems().remove(item1);
		TodoList ref = todoListRepo.save(listOne);

		TodoList againFromDB = todoListRepo.findOne(ref.getId());
		assertEquals(againFromDB.getTodoItems().size(), 1);
	}

	@Test
	public void testTodoListValidations() {

		try {
			todoListRepo.save(new TodoList(null, "things to do in education"));
			fail("name should not be null");
		} catch (Exception e) {
			// expected
		}

		TodoList listOne = new TodoList("education", "things to do in education");
		todoListRepo.save(listOne);
		Iterable<TodoList> result = todoListRepo.findAll();
		assertThat(result, is(iterableWithSize(1)));

		TodoList listDup = new TodoList("education", "my second education todo List");
		try {
			todoListRepo.save(listDup);
			fail("duplicate names are not allowed for TodoList");
		} catch (Exception e) {
			// nothing to do Exception expected
		}
	}
}