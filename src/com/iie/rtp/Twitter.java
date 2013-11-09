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
		
		long startTime1 = System.currentTimeMillis();   //��ȡ��ʼʱ��
		
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
		System.out.println("���ݿ��ȡ����ʱ�䣺 "+ (endTime1-startTime1) +"ms");
		
		System.out.println("��������С��" + AllContents.size());
		
		/**
		 * �û�������Ϣͳ��
		 * @AllContents ȫ��������Ϣ
		 * @UserName    ��ͳ�ƹؼ��û���
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
		 * �ؼ���������Ϣͳ��
		 */
		String KeyWord =  "��Ǹ";
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
		 * ��ȡָ������ת������
		 */
		SpecTopic st = new SpecTopic();
		Vector<Content> SpecContents = st.ChooseTopic(AllContents);
		
		long startTime2 = System.currentTimeMillis();   //��ȡ��ʼʱ��
		/**
		 * ���ƻ���ͬ���ķ���
		 */
		Similarity sim = new Similarity();
		Vector<Content> ClusterContent = sim.SimilarityCluster(AllContents);	// ��ͬ����������б�ţ� ���������С�����������
		int cluster  = ClusterContent.get(ClusterContent.size()-1).getNum();		
		long endTime2 = System.currentTimeMillis();
		System.out.println("�������ƶȼ���ʱ�䣺 "+ (endTime2-startTime2) +"ms");
		
		long startTime3 = System.currentTimeMillis();   //��ȡ��ʼʱ��
		
		/**
		 * Top K ������Ϣ�ķ���
		 */
		SelectTopK topK = new SelectTopK();
		Vector<Content> RtContent = topK.SelectTopK(ClusterContent);
	
		/**
		 * �ṩ�ӿ�
		 */
		int K = 50;
		Process proc = new Process();
		List<String> username = proc.TopKUserName(RtContent, K);
		Vector<Content> message = proc.TopKMessage(RtContent, K); 
		
		/*for (String strName: username) {
			System.out.println(strName);
		}*/
		
		
		if (ud.insertUserTop(username)) {
			System.out.println("Top K �û�������ɹ�!");
		} else{
			System.out.println("Top K �û�������ʧ��!");
		}
		
		if (ud.insertHotTweet(message)) {
			System.out.println("Top K �ȵ����Ĳ���ɹ�!");
		} else{
			System.out.println("Top K �ȵ����Ĳ���ʧ��!");
		}
		
		/*if (ud.insertRetweetTrend(message)) {
			System.out.println("Top K ����ת����Ϣ����ɹ�!");
		} else{
			System.out.println("Top K ����ת����Ϣ����ʧ��!");
		}*/
		
		/*WriteTxtFile wtf = new WriteTxtFile();
		if (wtf.SQLWriter(message)) {
			System.out.println("SQL�ɹ�д��!");
		} else {
			System.out.println("SQLд��ʧ��!");
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
		
		long endTime3 = System.currentTimeMillis(); //��ȡ����ʱ��  
		System.out.println("ѡ��TopK��������ʱ�䣺 "+ (endTime3-startTime3) +"ms");
		System.out.println("����������ʱ�䣺 "+ (endTime3-startTime1) +"ms");
		
	}
}
