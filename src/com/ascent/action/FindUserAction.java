/**
 * 
 */
package com.ascent.action;

import com.ascent.dao.UserDAO;
import com.ascent.javabean.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author zy
 *
 */
public class FindUserAction extends ActionSupport {
	private int id ;

	@Override
	public String execute() throws Exception {
		UserDAO d = new UserDAO();
		User user = d.findUserByid(id);
		ActionContext ac = ActionContext.getContext();
		ac.put("u", user);
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
