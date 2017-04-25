package com.deltaa.app.todo.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.deltaa.app.todo.entity.TodoList;

/**
 * @author malaka
 */
public interface TodoListRepository extends PagingAndSortingRepository<TodoList, Long> {

}
