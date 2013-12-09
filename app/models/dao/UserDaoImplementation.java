/*******************************************************************************
 * Copyright (c) 2013 Carnegie Mellon University Silicon Valley. 
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available
 * under the terms of dual licensing(GPL V2 for Research/Education
 * purposes). GNU Public License v2.0 which accompanies this distribution
 * is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * 
 * Please contact http://www.cmu.edu/silicon-valley/ if you have any 
 * questions.
 ******************************************************************************/
package models.dao;

import models.User;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class UserDaoImplementation implements UserDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

	@Override
	public boolean addUser(String userName, String profile) {
		final String ADD_USER = "insert into cmu.course_user values (cmu.course_user_id_seq.nextval, ?, ?)";
		try {
			simpleJdbcTemplate.update(ADD_USER, userName, profile);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public User getUser(String userName) {
		final String GET_USER = "select * from cmu.course_user where user_name = ?";
		try {
			User user = simpleJdbcTemplate.queryForObject(GET_USER, ParameterizedBeanPropertyRowMapper.newInstance(User.class), userName);
			return user;
		}catch(Exception e){
			return null;
		}
	}

}
