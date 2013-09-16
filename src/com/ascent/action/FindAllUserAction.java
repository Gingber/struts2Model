/**
 * 
 */
package com.ascent.action;

import java.util.List;

import com.ascent.dao.UserDAO;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author WYL
 *
 */
public class FindAllUserAction extends ActionSupport {
	
	private List users ;
	
	/**
	 * @return the users
	 */
	public List getUsers() {
		return users;
	}
	
	@Override
	public String execute() throws Exception {
		UserDAO d = new UserDAO();
		users = d.findAll();
		return this.SUCCESS;
	}
	
}
