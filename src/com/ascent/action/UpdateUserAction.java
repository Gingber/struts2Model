/**
 * 
 */
package com.ascent.action;

import com.ascent.dao.UserDAO;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zy
 *
 */
public class UpdateUserAction extends ActionSupport {
	private int id ;
	private String username ;
	private String password ;
	private int age ;
	private String email ;
	
	@Override
	public String execute() throws Exception {
		UserDAO d = new UserDAO() ;
		d.updateUser(id, username, password, age, email);
		
		return this.SUCCESS;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
