/**
 * 
 */
package com.iie.statistics;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import com.iie.rtp.Content;

/**
 * @author Gingber
 *
 */
public class KeyUserInfo {

	/**
	 * 
	 */
	public KeyUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	private static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
	
	/**
	 * 用户推文信息统计
	 * @throws ParseException 
	 * @AllContents 全部数据信息
	 * @UserName    待统计用户名
	 * 
	 */
	public Map<Integer, Map<Date, String>>[] UserTweetInfo(Vector<Content> content, String UserName) throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd"); //dd/MM/yyyy
		
		/**
		 * 获取当前时间之前一周的时间日期
		 */
		//Date date = new Date();
		Date date = sdfDate.parse("2010-04-03"); // example
		Date [] dates = new Date[7];
		String myDate = sdfDate.format(date);
		for (int j = 0; j < 7; j++) {
			dates[j] = addDays(sdfDate.parse(myDate), -j);
		}
		
		/**
		 * 统计一周内的推文个数
		 */
		int[] count = new int[8];
		Map<Date, String>[] map = new HashMap[8]; 
		
		for (int i = 0; i < 8; i++) {
			map[i] = new HashMap();
		}
		
		for (int i = 0, size = content.size(); i < size; i++) {
			if (UserName.equals(content.get(i).getUserId())) {
				/*System.out.print("UserName = " + content.get(i).getUserId());
				System.out.print("\tCreate_Time = " + sdf.format(content.get(i).getCreate_time()));
				System.out.println("\tTitle = " + content.get(i).getTitle());*/
				
				Date ctdate = content.get(i).getCreate_time();
				String title = content.get(i).getTitle();
				
				if(ctdate.compareTo(dates[0]) >= 0){
					//System.out.println("today");
					count[0]++;
					map[0].put(ctdate, title);
	        	}else if(ctdate.compareTo(dates[1]) >= 0){
	        		//System.out.println("yesterday");
	        		count[1]++;
	        		map[1].put(ctdate, title);
	        	}else if(ctdate.compareTo(dates[2])>= 0){
	        		//System.out.println("last3days");
	        		count[2]++;
	        		map[2].put(ctdate, title);
	        	}else if(ctdate.compareTo(dates[3])>= 0){
	        		//System.out.println("last4days");
	        		count[3]++;
	        		map[3].put(ctdate, title);
	        	}else if(ctdate.compareTo(dates[4])>= 0){
	        		//System.out.println("last5days");
	        		count[4]++;
	        		map[4].put(ctdate, title);
	        	}else if(ctdate.compareTo(dates[5])>= 0){
	        		//System.out.println("last6days");
	        		count[5]++;
	        		map[5].put(ctdate, title);
	        	}else if(ctdate.compareTo(dates[6])>= 0){
	        		//System.out.println("last7days");
	        		count[6]++;
	        		map[6].put(ctdate, title);
	        	} else {
	        		//System.out.println("morethan7days");
	        		count[7]++;
	        		map[7].put(ctdate, title);
	        	}				
			}
		}
		
		Map<Integer, Map<Date, String>>[] cdtUserMap = new HashMap[8]; // Count_Date_Title
		for (int i = 0; i < 8; i++) {
			cdtUserMap[i] = new HashMap();
		}
		
		for (int i = 0, len = count.length; i < len; i++ ) {
			cdtUserMap[i].put(Integer.valueOf(count[i]), map[i]);
		}

		return cdtUserMap;	
	}

}
