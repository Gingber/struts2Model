/**
 * 
 */
package com.ascent.javabean;

import java.io.Serializable;

/**
 * @author zy
 * 实现O-R Mapping映射
 * 类名   对应  表名
 * 类属性   对应   表字段
 * 类属性类型  对应   表字段类型
 * 类关系    对应  表关系
 * 
 */
public class User implements Serializable {
	private int id ;
	private String username ;
	private String password ;
	private int age ;
	private String email ;
	
	
	public User(){}
	
	
	/**
	 * @param id
	 * @param username
	 * @param password
	 * @param age
	 * @param email
	 */
	public User(int id, String username, String password, int age, String email) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.age = age;
		this.email = email;
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
