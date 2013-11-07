/**
 * 
 */
package com.iie.rtp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Gingber
 *
 */

class ComparatorImpl implements Comparator<Content> {
	public int compare(Content s1,Content s2) { // 转发量由大到小排列
		int num1 = s1.getForwardnum();
		int num2 = s2.getForwardnum();
		if(num1 > num2) {
			return -1;
		}else if(num1 < num2){
			return 1;
		}else{
			return 0;
		}
	}
}

class ComparatorDate implements Comparator{  
	public int compare(Object obj1, Object obj2) {  	// 转发量由小到大排列
        Date begin=(Date)obj1;  
        Date end=(Date)obj2;  
        if(begin.after(end)){  
            return 1;  
        }  
        else{  
            return -1;  
        }     
	}  
}  

public final class Process {
	
	public List<String> TopKUserName(Vector<Content> clustercontent, int K) {	
		
		List<String> topKUserName = new ArrayList<String>();
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		
		if (clustercontent.size() != 0) {
			for (int i = 0, size = clustercontent.size(); i < size; i++) {
				map.put(clustercontent.get(i).getRtlink(), clustercontent.get(i).getForwardnum());
			}
		}
		
		ValueComparator bvc =  new ValueComparator(map);
        TreeMap<String, Integer> sortmap = new TreeMap<String,Integer>(bvc);

        /*System.out.println("Top " + K + "排序:");*/
        sortmap.putAll(map);
        
		int counter = 0 ;
		Iterator iter = sortmap.entrySet().iterator(); 
        while (iter.hasNext()) { 
        	counter++;
        	if(counter <= K ) {
        		Map.Entry entry = (Map.Entry) iter.next(); 
                Object key = entry.getKey(); 
                Object val = entry.getValue(); 
                /*System.out.println(val + "\t" + key);*/
                
                Pattern pattern = Pattern.compile("\\((.*?)\\)");
                Matcher matcher = pattern.matcher(key.toString());
                while(matcher.find()){
                	String[] username = matcher.group(1).split("->");
                	for (String str : username) {
                		if (!topKUserName.contains(str)) {	// topK用户名加入List中
                			topKUserName.add(str);
                		}
                		
                	}
                }
        	}
        	else break;
        } 
		
		return topKUserName;
		
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Content> TopKMessage(Vector<Content> clustercontent, int K) {
		
		Comparator comp = new ComparatorImpl();
		Collections.sort(clustercontent, comp);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Content content =null; 
		Vector<Content> contents = new Vector<Content>();
		
		List<String> topKUserName = new ArrayList<String>();
		int usernum = 0;
		if (clustercontent.size() != 0) {
			for (int i = 0, size = clustercontent.size(); i < K && i < size; i++) {
				System.out.println("转发数forwarnum = " + clustercontent.get(i).getForwardnum());
				System.out.println("转发链: " + clustercontent.get(i).getRtlink());
				
				Pattern pattern = Pattern.compile("\\((.*?)\\)");
                Matcher matcher = pattern.matcher(clustercontent.get(i).getRtlink());
                while(matcher.find()){
                	String[] username = matcher.group(1).split("->");
                	for (String str : username) {
                		if (!topKUserName.contains(str)) {	// topK用户名加入List中
                			topKUserName.add(str);
                			usernum++;
                		}
                		
                	}
                }
                
                System.out.println("参与转发人数： " + usernum);   
				
                List<Date> list = new ArrayList<Date>();
                String strcon = null;
			    
				HashMap<String, Date> hashmap = clustercontent.get(i).getHashmap();
				Iterator iter = hashmap.entrySet().iterator(); 
				while (iter.hasNext()) { 
				    Map.Entry entry = (Map.Entry) iter.next(); 
				    Object key = entry.getKey(); 
				    Object val = entry.getValue(); 
				    System.out.println(key + "\t" + sdf.format(val));
				    
				    if (strcon == null) {
				    	strcon = key.toString();
				    }

				    list.add((Date)val);   
				}
				
				Comparator c = new ComparatorDate();  
		        Collections.sort(list,c);  
				
				content = new Content(clustercontent.get(i).getForwardnum(), usernum, topKUserName.get(0),
						strcon, list.get(0), list.get(list.size()-1));
				contents.add(content);
				
				usernum = 0;
                topKUserName = new ArrayList<String>();
                list = new ArrayList<Date>();
		        strcon = null;
                
				System.out.println("");
			}
		}	
		
		return contents;
	}

	/**
	　* 私有的构造方法
	*/
	public Process() {
		super();
		// TODO Auto-generated constructor stub
	}
}
