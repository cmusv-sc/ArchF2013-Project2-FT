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
