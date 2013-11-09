/**
 * 
 */
package com.iie.rtp;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.arthur.simhash.SimHash;

/**
 * @author hadoop
 *
 */
public class Content {
	
	private String messageId;
	private String userId;
	private String title;
	private SimHash hash;
	private Date create_time;
	private int num;	// 消息编号
	private int forwardnum;	// 转发数
	private int usernum; 	// 用户数
	private String rtlink;
	private HashMap<String, Date> hashmap;
	private String firstuser;
	private Date start_time;
	private Date end_time;
	
	
	
	
	/**
	 * @param messageId
	 * @param userId
	 * @param title
	 * @param fingerprint
	 * @param create_time
	 */
	public Content(String messageId, String userId, String title,
			SimHash hash, Date create_time) {
		super();
		this.messageId = messageId;
		this.userId = userId;
		this.title = title;
		this.hash = hash;
		this.create_time = create_time;
	}
	/**
	 * @param title
	 * @param createTime
	 * @param num
	 * @param forwardnum
	 * @param usernum
	 * @param rtlink
	 */
	public Content(int forwardnum, String messageId, String rtlink, HashMap<String, Date> hashmap) {
		super();
		this.forwardnum = forwardnum;
		this.messageId = messageId;
		this.rtlink = rtlink;
/*		this.title = title;
		this.create_time = create_time;*/
		this.hashmap = hashmap;
	}
	/**
	 * 
	 */
	public Content() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param messageId
	 * @param userId
	 * @param title
	 * @param num
	 */
	public Content(String messageId, String userId, String title, int num) {
		super();
		this.messageId = messageId;
		this.userId = userId;
		this.title = title;
		this.num = num;
	}
	/**
	 * @param messageId
	 * @param userId
	 * @param title
	 * @param createTime
	 * @param num
	 */
	public Content(String messageId, String userId, String title,
			Date createTime, int num) {
		super();
		this.messageId = messageId;
		this.userId = userId;
		this.title = title;
		this.create_time = createTime;
		this.num = num;
	}
	/**
	 * @param messageId
	 * @param title
	 */
	public Content(String messageId, String userId, String title) {
		super();
		this.messageId = messageId;
		this.userId = userId;
		this.title = title;
	}
	/**
	 * @param messageId
	 * @param userId
	 * @param title
	 * @param createTime
	 */
	public Content(String messageId, String userId, String title,
			Date createTime) {
		super();
		this.messageId = messageId;
		this.userId = userId;
		this.title = title;
		this.create_time = createTime;
	}
	/**
	 * @param title
	 * @param forwardnum
	 * @param usernum
	 * @param firstuser
	 * @param startTime
	 * @param endTime
	 */
	public Content(int forwardnum, int usernum, String firstuser, String title,
			Date startTime, Date endTime) {
		super();
		this.title = title;
		this.forwardnum = forwardnum;
		this.usernum = usernum;
		this.firstuser = firstuser;
		this.start_time = startTime;
		this.end_time = endTime;
	}
	/**
	 * @return the firstuser
	 */
	public String getFirstuser() {
		return firstuser;
	}
	/**
	 * @param firstuser the firstuser to set
	 */
	public void setFirstuser(String firstuser) {
		this.firstuser = firstuser;
	}
	/**
	 * @return the start_time
	 */
	public Date getStart_time() {
		return start_time;
	}
	/**
	 * @param startTime the start_time to set
	 */
	public void setStart_time(Date startTime) {
		start_time = startTime;
	}
	/**
	 * @return the end_time
	 */
	public Date getEnd_time() {
		return end_time;
	}
	/**
	 * @param endTime the end_time to set
	 */
	public void setEnd_time(Date endTime) {
		end_time = endTime;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}
	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the fingerprint
	 */
	public SimHash getHash() {
		return hash;
	}
	/**
	 * @param fingerprint the fingerprint to set
	 */
	public void setHash(SimHash hash) {
		this.hash = hash;
	}
	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}
	/**
	 * @param num the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}
	/**
	 * @return the create_time
	 */
	public Date getCreate_time() {
		return create_time;
	}
	/**
	 * @param createTime the create_time to set
	 */
	public void setCreate_time(Date createTime) {
		create_time = createTime;
	}
	/**
	 * @return the forwardnum
	 */
	public int getForwardnum() {
		return forwardnum;
	}
	/**
	 * @param forwardnum the forwardnum to set
	 */
	public void setForwardnum(int forwardnum) {
		this.forwardnum = forwardnum;
	}
	/**
	 * @return the usernum
	 */
	public int getUsernum() {
		return usernum;
	}
	/**
	 * @param usernum the usernum to set
	 */
	public void setUsernum(int usernum) {
		this.usernum = usernum;
	}
	/**
	 * @return the rtlink
	 */
	public String getRtlink() {
		return rtlink;
	}
	/**
	 * @param rtlink the rtlink to set
	 */
	public void setRtlink(String rtlink) {
		this.rtlink = rtlink;
	}
	/**
	 * @return the hashmap
	 */
	public HashMap<String, Date> getHashmap() {
		return hashmap;
	}
	/**
	 * @param hashmap the hashmap to set
	 */
	public void setHashmap(HashMap<String, Date> hashmap) {
		this.hashmap = hashmap;
	}
}
