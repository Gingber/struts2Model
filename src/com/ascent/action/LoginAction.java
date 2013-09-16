/**
 * 
 */
package com.ascent.action;

import java.util.Map;

import com.ascent.dao.UserDAO;
import com.ascent.javabean.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zy
 *
 */
public class LoginAction extends ActionSupport {
	private String username ;
	private String password ;
	
	@Override
	public String execute() throws Exception {
		UserDAO d = new UserDAO();
		User user = d.validate(username, password);
		if(user!=null){
			ActionContext ac = ActionContext.getContext();
			Map session = ac.getSession();
			session.put("user", user);
			session.put("isLogin", "true");
			return this.SUCCESS;
		}else{
			return this.INPUT;
		}
		
		
		
		
	}
	
	@Override
	public void validate() {
		// TODO Auto-generated method stub
		super.validate();
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
	
	
	
	
}
