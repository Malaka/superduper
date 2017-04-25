package com.deltaa.app.todo;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.deltaa.app.todo.entity.TodoItem;
import com.deltaa.app.todo.entity.TodoList;
import com.deltaa.app.todo.entity.TodoStatus;
import com.deltaa.app.todo.repo.TodoListRepository;

/**
 * Initial data loader
 *
 * @author malaka
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

	private final TodoListRepository repository;

	@Autowired
	public DatabaseLoader(TodoListRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(String... strings) throws Exception {

		TodoList listOne = new TodoList("education", "things to do in education");
		listOne.addTodoItem(new TodoItem("java", "study java", TodoStatus.PENDING, ZonedDateTime.now()));
		listOne.addTodoItem(new TodoItem("spring", "learn spring", TodoStatus.PENDING, ZonedDateTime.now()));

		this.repository.save(listOne);
		this.repository.save(new TodoList("Shopping", "Monthly Shopping"));
	}
}