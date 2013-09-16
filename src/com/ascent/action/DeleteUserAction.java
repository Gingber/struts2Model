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
public class DeleteUserAction extends ActionSupport {
	private int id ;
	
	@Override
	public String execute() throws Exception {
		UserDAO d = new UserDAO();
		d.deleteUser(id);
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
	
}
