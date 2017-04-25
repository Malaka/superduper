
package com.deltaa.app.todo.entity;

import lombok.Data;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;

/**
 * To-do  item collection
 *
 * @author malaka
 */
@Data
@Entity
public class TodoList {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@Column(length = 100, unique = true, nullable = false)
	private String name;

	@Column(length = 400)
	private String description;

	@Version
	@JsonIgnore
	private Long version;

	@OneToMany(mappedBy = "todoList", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<TodoItem> todoItems = Sets.newHashSet();

	public TodoList() {

	}

	public TodoList(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<TodoItem> getTodoItems() {
		return todoItems;
	}

	public void addTodoItem(TodoItem todoItem) {
		todoItem.setTodoList(this);
		getTodoItems().add(todoItem);
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TodoList)) return false;

		TodoList todoList = (TodoList) o;

		return name.equals(todoList.name);

	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", id)
			.add("name", name)
			.add("description", description)
			.add("version", version)
			.toString();
	}
}