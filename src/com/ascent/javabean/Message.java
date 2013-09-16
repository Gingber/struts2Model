/**
 * 
 */
package com.ascent.javabean;

import java.util.*;

/**
 * @author Gingber
 *
 */
public class Message {
	private int channel_id;
	private String message_id;
	private String url;
	private String title;
	private String user_name;
	private int receiver_id;
	private String source;
	private Date create_time;
	private Date crawl_time;
	private int comments_times;
	private int click_times;
	private int citation_times;
	private int forward_times;
	private int finger_print;
	private Date last_gather_time;
	private String keywords;
	private String raw_file_location;
	private int raw_data_offset;
	private int raw_data_length;
	private int fx_dxzx;
	private int sz_dxzx;
	private String other1;
	private String other2;
	private String other3;
	
	
	/**
	 * 
	 */
	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param messageId
	 * @param title
	 * @param userId
	 * @param createTime
	 */
	public Message(String messageId, String title, String userName,
			Date createTime) {
		super();
		this.message_id = messageId;
		this.title = title;
		this.user_name = userName;
		this.create_time = createTime;
	}
	/**
	 * @return the channel_id
	 */
	public int getChannel_id() {
		return channel_id;
	}
	/**
	 * @param channelId the channel_id to set
	 */
	public void setChannel_id(int channelId) {
		channel_id = channelId;
	}
	/**
	 * @return the message_id
	 */
	public String getMessage_id() {
		return message_id;
	}
	/**
	 * @param messageId the message_id to set
	 */
	public void setMessage_id(String messageId) {
		message_id = messageId;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
	 * @return the user_id
	 */
	public String getUser_Name() {
		return user_name;
	}
	/**
	 * @param userId the user_id to set
	 */
	public void setUser_Name(String userId) {
		user_name = userId;
	}
	/**
	 * @return the receiver_id
	 */
	public int getReceiver_id() {
		return receiver_id;
	}
	/**
	 * @param receiverId the receiver_id to set
	 */
	public void setReceiver_id(int receiverId) {
		receiver_id = receiverId;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
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
	 * @return the crawl_time
	 */
	public Date getCrawl_time() {
		return crawl_time;
	}
	/**
	 * @param crawlTime the crawl_time to set
	 */
	public void setCrawl_time(Date crawlTime) {
		crawl_time = crawlTime;
	}
	/**
	 * @return the comments_times
	 */
	public int getComments_times() {
		return comments_times;
	}
	/**
	 * @param commentsTimes the comments_times to set
	 */
	public void setComments_times(int commentsTimes) {
		comments_times = commentsTimes;
	}
	/**
	 * @return the click_times
	 */
	public int getClick_times() {
		return click_times;
	}
	/**
	 * @param clickTimes the click_times to set
	 */
	public void setClick_times(int clickTimes) {
		click_times = clickTimes;
	}
	/**
	 * @return the citation_times
	 */
	public int getCitation_times() {
		return citation_times;
	}
	/**
	 * @param citationTimes the citation_times to set
	 */
	public void setCitation_times(int citationTimes) {
		citation_times = citationTimes;
	}
	/**
	 * @return the forward_times
	 */
	public int getForward_times() {
		return forward_times;
	}
	/**
	 * @param forwardTimes the forward_times to set
	 */
	public void setForward_times(int forwardTimes) {
		forward_times = forwardTimes;
	}
	/**
	 * @return the finger_print
	 */
	public int getFinger_print() {
		return finger_print;
	}
	/**
	 * @param fingerPrint the finger_print to set
	 */
	public void setFinger_print(int fingerPrint) {
		finger_print = fingerPrint;
	}
	/**
	 * @return the last_gather_time
	 */
	public Date getLast_gather_time() {
		return last_gather_time;
	}
	/**
	 * @param lastGatherTime the last_gather_time to set
	 */
	public void setLast_gather_time(Date lastGatherTime) {
		last_gather_time = lastGatherTime;
	}
	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}
	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	/**
	 * @return the raw_file_location
	 */
	public String getRaw_file_location() {
		return raw_file_location;
	}
	/**
	 * @param rawFileLocation the raw_file_location to set
	 */
	public void setRaw_file_location(String rawFileLocation) {
		raw_file_location = rawFileLocation;
	}
	/**
	 * @return the raw_data_offset
	 */
	public int getRaw_data_offset() {
		return raw_data_offset;
	}
	/**
	 * @param rawDataOffset the raw_data_offset to set
	 */
	public void setRaw_data_offset(int rawDataOffset) {
		raw_data_offset = rawDataOffset;
	}
	/**
	 * @return the raw_data_length
	 */
	public int getRaw_data_length() {
		return raw_data_length;
	}
	/**
	 * @param rawDataLength the raw_data_length to set
	 */
	public void setRaw_data_length(int rawDataLength) {
		raw_data_length = rawDataLength;
	}
	/**
	 * @return the fx_dxzx
	 */
	public int getFx_dxzx() {
		return fx_dxzx;
	}
	/**
	 * @param fxDxzx the fx_dxzx to set
	 */
	public void setFx_dxzx(int fxDxzx) {
		fx_dxzx = fxDxzx;
	}
	/**
	 * @return the sz_dxzx
	 */
	public int getSz_dxzx() {
		return sz_dxzx;
	}
	/**
	 * @param szDxzx the sz_dxzx to set
	 */
	public void setSz_dxzx(int szDxzx) {
		sz_dxzx = szDxzx;
	}
	/**
	 * @return the other1
	 */
	public String getOther1() {
		return other1;
	}
	/**
	 * @param other1 the other1 to set
	 */
	public void setOther1(String other1) {
		this.other1 = other1;
	}
	/**
	 * @return the other2
	 */
	public String getOther2() {
		return other2;
	}
	/**
	 * @param other2 the other2 to set
	 */
	public void setOther2(String other2) {
		this.other2 = other2;
	}
	/**
	 * @return the other3
	 */
	public String getOther3() {
		return other3;
	}
	/**
	 * @param other3 the other3 to set
	 */
	public void setOther3(String other3) {
		this.other3 = other3;
	}

}
