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

import java.util.List;

import models.ContestUser;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class ContestUserDaoImplementation implements ContestUserDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

	@Override
	public boolean addUser(ContestUser contestUser) {
		final String CONTEST_USER_COUNT = "select count(*) from cmu.course_contest_user";
		
		final String ADD_USER = "insert into cmu.course_contest_user values (cmu.course_user_id_seq.nextval, ?, ?,?,?, ?,?, ?,?, ?)";
		try {
			int count = simpleJdbcTemplate.queryForInt(CONTEST_USER_COUNT);
			if (count >= 200)
				return false;
			simpleJdbcTemplate.update(ADD_USER, contestUser.getUserName(), contestUser.getPassword(), contestUser.getFirstName(), contestUser.getLastName(), contestUser.getMiddleName(), contestUser.getAffiliation(), contestUser.getEmail(), contestUser.getResearchArea(), contestUser.getGoal());
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean updateUser(ContestUser contestUser) {
		final String SELECT_USER = "select * from cmu.course_contest_user where user_name = ? and password = ?";
		
		
		final String SQL = "update cmu.course_contest_user "
				+ "set first_name = ?, last_name = ?, middle_name = ?, affiliation  = ?, email = ?,research_area = ?, goal = ? " + "where user_name = ?";
		try {
			ContestUser user = simpleJdbcTemplate.queryForObject(SELECT_USER, ParameterizedBeanPropertyRowMapper.newInstance(ContestUser.class), contestUser.getUserName(), contestUser.getPassword());
			if (user == null)
				return false;
			simpleJdbcTemplate.update(SQL, contestUser.getFirstName(), contestUser.getLastName(), contestUser.getMiddleName(), contestUser.getAffiliation(), contestUser.getEmail(), contestUser.getResearchArea(), contestUser.getGoal(), contestUser.getUserName());
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean deleteUser(String userName, String pwd) {
		final String SQL = "delete from cmu.course_contest_user where user_name = ? and password = ? ";
		try{
			simpleJdbcTemplate.update(SQL, userName, pwd);
		}catch(Exception e){
			return false;
		}
		return true;
	}

	@Override
	public ContestUser getUser(String userName, String pwd) {
		final String GET_USER = "select * from cmu.course_contest_user where user_name = ? and password = ?";
		try {
			ContestUser contestUser = simpleJdbcTemplate.queryForObject(GET_USER, ParameterizedBeanPropertyRowMapper.newInstance(ContestUser.class), userName, pwd);
			return contestUser;
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<ContestUser> getAllUsers() {
		final String SQL = "SELECT * FROM CMU.COURSE_CONTEST_USER";
		try {
			List<ContestUser> contestUsers = simpleJdbcTemplate.query(
					SQL, ParameterizedBeanPropertyRowMapper
							.newInstance(ContestUser.class));
			return contestUsers;
		} catch (Exception e) {
			return null;
		}
	}

}
