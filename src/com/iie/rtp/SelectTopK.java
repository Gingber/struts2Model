/**
 * 
 */
package com.iie.rtp;

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

import com.ascent.javabean.Message;

/**
 * @author hadoop
 *
 */
public final class SelectTopK {
	

	public static List<String> RtList;
	
	public Vector<Content> SelectTopK(Vector<Content> clustercontent) {
		
		RtList = new ArrayList<String>();
		// 抽取转发关系类
		ReductionStatement rs = new ReductionStatement();
		
		int num = 1;
		int usernum = 0;
		boolean numflag = false;
		int ForwardNum = 0;
		StringBuilder RtLink = new StringBuilder(); // 存储转发关系链
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		HashMap<String, Date> mapIdTime = new HashMap<String, Date>();
		
		Content content =null; 
		Vector<Content> contents = new Vector<Content>();
		
		Comparator comp = new ComparatorImplementation();
		Collections.sort(clustercontent, comp);
		
		HashMap<String, Date> hashmap = new HashMap<String, Date>();
		
		if (clustercontent.size() != 0) {
			for (int i = 0, size = clustercontent.size(); i < size; i++) {
				if (num == clustercontent.get(i).getNum() 
						&& num <= clustercontent.get(clustercontent.size()-1).getNum()) {
					RtList = rs.ExtractForward(clustercontent.get(i).getMessageId(), 
							clustercontent.get(i).getUserId(), clustercontent.get(i).getTitle());
					Collections.reverse(RtList); // 倒序排列
					if (RtList.size() > 0) {
						RtLink.append("(");
						for(int j=0, rtSize = RtList.size(); j < rtSize; j++) {   
						       RtLink.append(RtList.get(j));
						       RtLink.append("->");
						}
						RtLink.append(")");
						ForwardNum += RtList.size() - 1;	// 转发量增加
						usernum += RtList.size();
						
						hashmap.put(clustercontent.get(i).getTitle(), clustercontent.get(i).getCreate_time());
				
					}
				}
				else {
					// 一组相似或相同推文分析完毕，组号标识numflag至为真
					numflag = true;
					if (numflag && ForwardNum != 0){
						map.put(RtLink.toString(), ForwardNum);
						
						content = new Content(ForwardNum, clustercontent.get(i-1).getMessageId(), RtLink.toString(), 
								hashmap);
						contents.add(content);
						
						ForwardNum = 0;
						usernum = 0;
						numflag = false;
						RtLink = new StringBuilder();
						hashmap = new HashMap<String, Date>();
					}
					RtList = rs.ExtractForward(clustercontent.get(i).getMessageId(), 
							clustercontent.get(i).getUserId(), clustercontent.get(i).getTitle());
					if (RtList.size() > 0) {
						Collections.reverse(RtList); // 倒序排列
						RtLink.append("(");
						for(int j=0, rtSize = RtList.size(); j < rtSize; j++) {   
						       RtLink.append(RtList.get(j));
						       RtLink.append("->");
						}
						RtLink.append(")");
						ForwardNum += RtList.size() - 1;
						usernum += RtList.size();
						
						hashmap.put(clustercontent.get(i).getTitle(), clustercontent.get(i).getCreate_time());

					}
					
					if (num == clustercontent.get(clustercontent.size()-1).getNum() && ForwardNum != 0) {
						map.put(RtLink.toString(), ForwardNum);
						
						content = new Content(ForwardNum, clustercontent.get(clustercontent.size()-1).getMessageId(), RtLink.toString(), 
								hashmap);
						contents.add(content);
					}
					
					num++;
				}
			}
		}
	
		return contents;
	}	
	

	/**
	 * 
	 */
	public SelectTopK() {
		super();
		// TODO Auto-generated constructor stub
	}
}

class ComparatorImplementation implements Comparator<Content> {
	public int compare(Content s1,Content s2) {
		int num1 = s1.getNum();
		int num2 = s2.getNum();
		if(num1 > num2) {
			return 1;
		}else if(num1 < num2){
			return -1;
		}else{
			return 0;
		}
	}
}

