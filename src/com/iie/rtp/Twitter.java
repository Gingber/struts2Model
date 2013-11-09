/**
 * 
 */
package com.iie.rtp;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.arthur.simhash.SimHash;
import com.ascent.dao.UserDAO;
import com.ascent.javabean.Message;
import com.iie.statistics.KeyUserInfo;
import com.iie.statistics.KeyWordInfo;

/**
 * @author Gingber
 *
 */

public class Twitter {
	
	public static List<Message> urList;

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, ParseException {
		// TODO Auto-generated method stub
		
		long startTime1 = System.currentTimeMillis();   //获取开始时间
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		UserDAO ud = new UserDAO();
		
		Content content =null; 
		Vector<Content> AllContents = new Vector<Content>();
		
		urList = new ArrayList<Message>();
		
		//List<String> SenWordsList = ud.findSensitiveWords();
		
		urList.addAll(ud.findAllMessage());

		SimHash simhash = new SimHash();
		
		Iterator<Message> itur = urList.iterator();
		while (itur.hasNext()) {
			Message urTemp = (Message) itur.next();
			String messageId = urTemp.getMessage_id();
			//System.out.println(messageId);
			String userId = urTemp.getUser_Name();
			String title = urTemp.getTitle();
			int fingerprint = simhash.getHashCode(title);
			Date createTime = urTemp.getCreate_time(); 
			content = new Content(messageId, userId, title, fingerprint, createTime);
			AllContents.add(content);
		}
		
		long endTime1 = System.currentTimeMillis();
		System.out.println("数据库读取数据时间： "+ (endTime1-startTime1) +"ms");
		
		System.out.println("数据量大小：" + AllContents.size());
		
		/**
		 * 用户推文信息统计
		 * @AllContents 全部数据信息
		 * @UserName    待统计关键用户名
		 * 
		 */
		String UserName =  "alayavijnana";
		KeyUserInfo kui = new KeyUserInfo();
		Map<Integer, Map<Date, String>>[] cdtUserMap = kui.UserTweetInfo(AllContents, UserName);
		/*for (int i = 0; i < cdtUserMap.length; i++){
			Iterator iter = cdtUserMap[i].entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next(); 
			    Object key = entry.getKey(); 
			    Object val = entry.getValue(); 
			    System.out.println(key + "\t" + val);
			} 
		}*/
		
		/**
		 * 关键词推文信息统计
		 */
		String KeyWord =  "致歉";
		KeyWordInfo kwi = new KeyWordInfo();
		Map<Integer, Map<Date, String>>[] cdtWordMap = kwi.KeyWordTweetInfo(AllContents, KeyWord);
		/*for (int i = 0; i < cdtWordMap.length; i++){ 	
			Iterator iter = cdtWordMap[i].entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next(); 
			    Object key = entry.getKey(); 
			    Object val = entry.getValue(); 
			    System.out.println(key + "\t" + val);
			} 
		}*/
		
		/**
		 * 获取指定话题转发推文
		 */
		SpecTopic st = new SpecTopic();
		Vector<Content> SpecContents = st.ChooseTopic(AllContents);
		
		long startTime2 = System.currentTimeMillis();   //获取开始时间
		/**
		 * 相似或相同推文分类
		 */
		Similarity sim = new Similarity();
		Vector<Content> ClusterContent = sim.SimilarityCluster(AllContents);	// 相同或相似项进行编号， 并将编号由小到大进行排序
		int cluster  = ClusterContent.get(ClusterContent.size()-1).getNum();		
		long endTime2 = System.currentTimeMillis();
		System.out.println("数据相似度计算时间： "+ (endTime2-startTime2) +"ms");
		
		long startTime3 = System.currentTimeMillis();   //获取开始时间
		
		/**
		 * Top K 推文信息的分析
		 */
		SelectTopK topK = new SelectTopK();
		Vector<Content> RtContent = topK.SelectTopK(ClusterContent);
	
		/**
		 * 提供接口
		 */
		int K = 50;
		Process proc = new Process();
		List<String> username = proc.TopKUserName(RtContent, K);
		Vector<Content> message = proc.TopKMessage(RtContent, K); 
		
		/*for (String strName: username) {
			System.out.println(strName);
		}*/
		
		
		if (ud.insertUserTop(username)) {
			System.out.println("Top K 用户名插入成功!");
		} else{
			System.out.println("Top K 用户名插入失败!");
		}
		
		if (ud.insertHotTweet(message)) {
			System.out.println("Top K 热点推文插入成功!");
		} else{
			System.out.println("Top K 热点推文插入失败!");
		}
		
		/*if (ud.insertRetweetTrend(message)) {
			System.out.println("Top K 推文转发信息插入成功!");
		} else{
			System.out.println("Top K 推文转发信息插入失败!");
		}*/
		
		/*WriteTxtFile wtf = new WriteTxtFile();
		if (wtf.SQLWriter(message)) {
			System.out.println("SQL成功写入!");
		} else {
			System.out.println("SQL写入失败!");
		}*/
		
		/*for (int i = 0; i < rtContent.size(); i++) {
			WriteTxtFile wtf = new WriteTxtFile("D:\\tweet\\" + rtContent.get(i).getMessageId() + ".txt");
			wtf.write(rtContent.get(i).getTitle());
			
		}*/
		
		/*Map<String, HashMap<String, Integer>> normal = ReadFiles.NormalTFOfAll(filePath);
        for (String filename : normal.keySet()) {
            System.out.println("fileName " + filename);
            System.out.println("TF " + normal.get(filename).toString());
        }

        System.out.println("-----------------------------------------");

        Map<String, HashMap<String, Float>> notNarmal = ReadFiles.tfOfAll(filePath);
        for (String filename : notNarmal.keySet()) {
            System.out.println("fileName " + filename);
            System.out.println("TF " + notNarmal.get(filename).toString());
        }

        System.out.println("-----------------------------------------");

        Map<String, Float> idf = ReadFiles.idf(filePath);
        for (String word : idf.keySet()) {
            System.out.println("keyword :" + word + " idf: " + idf.get(word));
        }

        System.out.println("-----------------------------------------");

        Map<String, HashMap<String, Float>> tfidf = ReadFiles.tfidf(filePath);
        for (String filename : tfidf.keySet()) {
            System.out.println("fileName " + filename);
            System.out.println(tfidf.get(filename));
        }*/
		
		long endTime3 = System.currentTimeMillis(); //获取结束时间  
		System.out.println("选择TopK推文排序时间： "+ (endTime3-startTime3) +"ms");
		System.out.println("程序运行总时间： "+ (endTime3-startTime1) +"ms");
		
	}
}
