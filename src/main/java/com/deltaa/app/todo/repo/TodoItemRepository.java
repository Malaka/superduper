package com.deltaa.app.todo.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.deltaa.app.todo.entity.TodoItem;

/**
 * @author malaka
 */
public interface TodoItemRepository extends PagingAndSortingRepository<TodoItem, Long> {

}
