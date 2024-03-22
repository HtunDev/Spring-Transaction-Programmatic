package com.HAH.demo.repo;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class HeaderRepository {

	private SimpleJdbcInsert jdbcInsert;

	public HeaderRepository(DataSource dataSource) {
		jdbcInsert = new SimpleJdbcInsert(dataSource);
		jdbcInsert.setTableName("header_tbl");
		jdbcInsert.setGeneratedKeyName("id");
		jdbcInsert.setColumnNames(List.of("name"));
	}

	public int create(String name) throws RuntimeException{
		return jdbcInsert.executeAndReturnKey(Map.of("name", name)).intValue();
	}
}
