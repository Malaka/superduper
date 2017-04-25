package com.deltaa.app.todo.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.deltaa.app.todo.entity.TodoItem;
import com.deltaa.app.todo.entity.TodoList;

/**
 * Enable ID visible through JSON response
 *
 * @author: malaka
 */
@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(TodoList.class);
		config.exposeIdsFor(TodoItem.class);
	}
}
