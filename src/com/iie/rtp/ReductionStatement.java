/**
 * 
 */
package com.iie.rtp;

/**
 * @author Gingber
 *
 */
import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.*;


class SelectionSorter {
    private int min;
    public void Sort(int[] arr) {
        for (int i = 0, iLen = arr.length - 1; i < iLen; ++i) {
            min = i;
            for (int j = i + 1, jLen = arr.length; j < jLen; ++j) {
                if (arr[j] < arr[min])
                    min = j;
            }
            
            int t = arr[min];
            arr[min] = arr[i];
            arr[i] = t;
        }
    }
}


public final class ReductionStatement {
	
    private String message_id;
    private String user_id;
    private String title;
    private Date create_time; 
	
	
	/**
	 * 
	 */
	public ReductionStatement() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param messageId
	 * @param userId
	 * @param title
	 * @param createTime
	 */
	public List<String> ExtractForward(String messageId, String userId, String title) {

		this.message_id = messageId;
		this.user_id = userId;
		this.title = title;
		
		List<String> matchstr = new ArrayList<String>();
	    List<String> match_RemoveRT = new ArrayList<String>();
	    List<String> match_RTUserName = new ArrayList<String>();
		
		// 去掉字符串中转发符号,注意顺序不可颠倒
	    String str = "RT @[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}:{0,1}";
	    matchstr.add(str);
	    str = "RT:@[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}:{0,1}";
	    matchstr.add(str);
	    str = "RT@[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}:{0,1}";
	    matchstr.add(str);
	    str = "RT: @[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}:{0,1}";
	    matchstr.add(str);
	    str = "Retweet @[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}:{0,1}";
	    matchstr.add(str);
	    str = "Retweeting @[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}:{0,1}";
	    matchstr.add(str);
	    str = "via @[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}";
	    matchstr.add(str);
	    str = "thx @[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}";
	    matchstr.add(str);
	    str = "thx@[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}";
	    matchstr.add(str);
	    str = "Retweeted by [\u4e00-\u9fa5a-zA-Z0-9_]{1,15}";
	    matchstr.add(str);
	    str = "@[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}:{0,1}";
	    matchstr.add(str);

	    // 匹配剔除用户名前的转发符号 + @，结果存入RT表中
	    str = "RT @";
	    match_RemoveRT.add(str);
	    str = "RT:@";
	    match_RemoveRT.add(str);
	    str = "RT@";
	    match_RemoveRT.add(str);
	    str = "RT: @";
	    match_RemoveRT.add(str);
	    str = "Retweet @";
	    match_RemoveRT.add(str);
	    str = "Retweeting @";
	    match_RemoveRT.add(str);
	    str = "Retweeted by ";
	    match_RemoveRT.add(str);
	    str = "via @";
	    match_RemoveRT.add(str);
	    str = "thx @";
	    match_RemoveRT.add(str);
	    str = "thx@";
	    match_RemoveRT.add(str);

	    // 匹配转发符号 + @ +　用户名　
	    str = "RT @[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}";
	    match_RTUserName.add(str);
	    str = "RT:@[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}";
	    match_RTUserName.add(str);
	    str = "RT@[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}";
	    match_RTUserName.add(str);
	    str = "RT: @[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}";
	    match_RTUserName.add(str);
	    str = "Retweet @[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}";
	    match_RTUserName.add(str);
	    str = "Retweeting @[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}";
	    match_RTUserName.add(str);
	    str = "Retweeted by [\u4e00-\u9fa5a-zA-Z0-9_]{1,15}";
	    match_RTUserName.add(str);
	    str = "via @[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}";
	    match_RTUserName.add(str);
	    str = "thx @[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}";
	    match_RTUserName.add(str);
	    str = "thx@[\u4e00-\u9fa5a-zA-Z0-9_]{1,15}";
	    match_RTUserName.add(str);
		
	    ArrayList fields = new ArrayList(); 
		Map lmap = new HashMap(); 
		
		HashMap<Integer, String> hashmap = new HashMap<Integer, String>();
		List<String> map = new ArrayList<String>();		
		List<String> RtList = new ArrayList<String>();
		String output = null;
		
		// 提取数据集中含有转发符号(如RT、Retweeted by等)推文
		for (int i = 0, size = match_RTUserName.size(); i < size; i++) {
			Pattern pattern = Pattern.compile(match_RTUserName.get(i));   
			Matcher matcher = pattern.matcher(title); 
			
			while(matcher.find()) {
				hashmap.put(matcher.start(), matcher.group());
			}
		}
		
		// title中含有转发符号
		if (hashmap.size() != 0) {
			int min = 0;
	        int arraynum = 0;
	        arraynum = hashmap.size();
	        SelectionSorter ss = new SelectionSorter();
	        int[] array = new int[arraynum];
	        Set<Entry<Integer, String>> sets = hashmap.entrySet();
	        for (Entry<Integer, String> entry : sets) {
	            array[min] = entry.getKey();
	            min++;
	        }
	        
	        min = 0;
	        ss.Sort(array);
	        
	        for (int j = 0, len = array.length; j < len; j++) {
	            map.add(hashmap.get(array[j]));
	        }
	        
	        ArrayList<String> al=new ArrayList();
	        for (int j = 0, size = map.size(); j < size; j++) {
	        	String temp = map.get(j);
	        	
	            for (String tempmatch : match_RemoveRT) {
	                output = temp.replace(tempmatch, "");
	                temp = output;
	            }
	            al.add(temp);
	        }
	        
	        // 构建推文转发关系链，转发者 -> 发布者
        	if (al.size() > 0) {
        		RtList.add(this.user_id);
	        	try {
	            	for (int j = 0, size = al.size(); j < size; j++) {
	            		RtList.add(al.get(j));
	            	}   
	            }
	            catch (Exception ex) {
	                System.out.println("Retweet Link Error:" + ex.toString());
	            }
	        }
	        
	        return RtList;
			
		}
		else {
			return RtList;
		}
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
		this.message_id = messageId;
	}

	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * @param userId the user_id to set
	 */
	public void setUser_id(String userId) {
		this.user_id = userId;
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
	 * @return the create_time
	 */
	public Date getCreate_time() {
		return create_time;
	}

	/**
	 * @param createTime the create_time to set
	 */
	public void setCreate_time(Date createTime) {
		this.create_time = createTime;
	}
	
}

