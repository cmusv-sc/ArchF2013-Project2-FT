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

import models.ContestUser;

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
	
	

}
