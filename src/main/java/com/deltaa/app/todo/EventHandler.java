package com.deltaa.app.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.deltaa.app.todo.entity.TodoList;

/**
 * Web socket support to push the updates to the F/E
 *
 * @author malaka
 */
@Component
@RepositoryEventHandler(TodoList.class)
public class EventHandler {

	private final SimpMessagingTemplate websocket;

	private final EntityLinks entityLinks;

	@Autowired
	public EventHandler(SimpMessagingTemplate websocket, EntityLinks entityLinks) {
		this.websocket = websocket;
		this.entityLinks = entityLinks;
	}

	@HandleAfterCreate
	public void newEmployee(TodoList todoList) {
		this.websocket.convertAndSend(
			WebSocketConfiguration.MESSAGE_PREFIX + "/newTodoList", getPath(todoList));
	}

	@HandleAfterDelete
	public void deleteEmployee(TodoList todoList) {
		this.websocket.convertAndSend(
			WebSocketConfiguration.MESSAGE_PREFIX + "/deleteTodoList", getPath(todoList));
	}

	@HandleAfterSave
	public void updateEmployee(TodoList todoList) {
		this.websocket.convertAndSend(
			WebSocketConfiguration.MESSAGE_PREFIX + "/updateTodoList", getPath(todoList));
	}

	/**
	 * Take an {@link TodoList} and get the URI using Spring Data REST's {@link EntityLinks}.
	 *
	 * @param todoList
	 */
	private String getPath(TodoList todoList) {
		return this.entityLinks.linkForSingleResource(todoList.getClass(),
			todoList.getId()).toUri().getPath();
	}

}
