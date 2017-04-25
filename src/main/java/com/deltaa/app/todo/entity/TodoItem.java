package com.deltaa.app.todo.entity;

import lombok.Data;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.deltaa.app.todo.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;

/**
 * To-do item
 *
 * @author: malaka
 */
@Data
@Entity
public class TodoItem {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "todoList_id", nullable = false)
	private TodoList todoList;

	@NotNull
	@Column(length = 100, nullable = false)
	private String name;

	@NotNull
	@Column(length = 400, nullable = false)
	private String description;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TodoStatus status;

	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonDeserialize(using = ZonedDateTimeDeserializer.class)
	private ZonedDateTime dateOfCompletion;

	/**
	 * comma separated values
	 */
	private String tags;

	public TodoItem() {
	}

	public TodoItem(String name, String description, TodoStatus status, ZonedDateTime dateOfCompletion) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.dateOfCompletion = dateOfCompletion;
	}

	public Long getId() {
		return id;
	}

	public void setTodoList(TodoList todoList) {
		this.todoList = todoList;
	}

	public TodoList getTodoList() {
		return todoList;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public TodoStatus getStatus() {
		return status;
	}

	public ZonedDateTime getDateOfCompletion() {
		return dateOfCompletion;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TodoItem)) return false;

		TodoItem todoItem = (TodoItem) o;

		if (!name.equals(todoItem.name)) return false;
		return description.equals(todoItem.description);

	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + description.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", id)
			.add("name", name)
			.add("description", description)
			.add("status", status)
			.add("dateOfCompletion", dateOfCompletion)
			.add("tags", tags)
			.toString();
	}
}
