/*****************************************************
 * 
 * This is a class of reading txt-file. 
 *
 *****************************************************
 * 
 * @author: Ginger
 * @Date: Tuesday 04-02 2013
 *
 *****************************************************
 */

package com.iie.rtp;

import java.text.SimpleDateFormat;
import java.util.Vector;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import com.google.common.base.Charsets;

public final class WriteTxtFile {
	
	/**
	 * 
	 */
	public WriteTxtFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String wrfile = null;
	
	public boolean FileCreator(String filePath) throws IOException {
		
		File file =new File(filePath); 
		
		if (file.exists()) {
			
			file.delete();			// �ļ����ڣ���ɾ��
			file.createNewFile();	// �����ļ�
		} else {
			
			file.createNewFile();	// �����ļ�
		}
		
		//FileWriter wrf = new FileWriter(file,true);
		this.wrfile = filePath;
		
		return true;
	}
	
	
	public  Vector<String> FileWriter(String data) {
		
		Vector<String> res = new Vector<String>(10);
		BufferedWriter bw = null;
		
		try {
			// ��1024*1024*5 = 5M�Ļ����ȡ�ı��ļ�,��������д���ٶ���
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(wrfile, true), Charsets.UTF_8));  
			
		} catch (FileNotFoundException fe) {
			
			fe.printStackTrace();
			
		}
		
		try {
			
			bw.write(data);
			bw.flush();
			bw.close();
			
		} catch (IOException ioe) {
			
			ioe.printStackTrace();
			
		} 
		
		return res;
		
	}
	
	/**
	 * SQLд�뱾�ش���
	 * @message ת����Ϣͳ��
	 * @return
	 * @throws IOException 
	 */
	public Boolean SQLWriter(Vector<Content> message) throws IOException {
		
		// ѡ����������������.
		if (FileCreator("D:\\ls.sql")) {
			System.out.println("�ļ������ɹ�!");
		}
		else {
			System.out.println("�ļ�����ʧ��!");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String SqlDefinition = "DROP TABLE IF EXISTS `toptwitter_twitter_201309096_10_1_Ginger`;\n\n" +
		"CREATE TABLE `toptwitter_twitter_201309096_10_1_Ginger` (\n" +
		"`twitterID` int(11) NOT NULL,\n" +
		"`tranNum` int(11) DEFAULT NULL,\n" +
		"`uerNum` int(11) DEFAULT NULL,\n" +
		"`tOrig` varchar(45) DEFAULT NULL,\n" +
		"`tContent` varchar(300) DEFAULT NULL,\n" +
		"`tStartTime` datetime DEFAULT NULL,\n" +
		"`tLastTime` datetime DEFAULT NULL,\n" +
		"PRIMARY KEY (`twitterID`)\n" +
		") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n" +
		"LOCK TABLES `toptwitter_twitter_201309096_10_1_Ginger` WRITE;\n\n";
		
		FileWriter(SqlDefinition);
		
		String SqlInsert = "INSERT INTO `toptwitter_twitter_201309096_10_1_Ginger` VALUES (";
		
		for (int i = 0; i < message.size(); i++) {
			System.out.println( "'" + message.get(i).getForwardnum() + "'," + 
								"'" + message.get(i).getUsernum() + "'," +
								"'" + message.get(i).getFirstuser() + "'," + 
								"'" + message.get(i).getTitle() + "'," + 
								"'" + message.get(i).getStart_time() + "'," +
								"'" + message.get(i).getEnd_time() + "'");
			// д���ļ�(�ṩ����ѧ��չʾ����)
			FileWriter(SqlInsert + (i+1) + "," +
					+ message.get(i).getForwardnum() + "," + 
					+ message.get(i).getUsernum() + "," +
					"'" + message.get(i).getFirstuser() + "'," + 
					"'" + message.get(i).getTitle() + "'," + 
					"'" + sdf.format(message.get(i).getStart_time()) + "'," +
					"'" + sdf.format(message.get(i).getEnd_time()) + "');\n");
		}

		FileWriter("\nUNLOCK TABLES;");
		
		return true;
		
	}

}

