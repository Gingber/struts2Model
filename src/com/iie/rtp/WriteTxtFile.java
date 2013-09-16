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
			
			file.delete();			// 文件存在，则删除
			file.createNewFile();	// 创建文件
		} else {
			
			file.createNewFile();	// 创建文件
		}
		
		//FileWriter wrf = new FileWriter(file,true);
		this.wrfile = filePath;
		
		return true;
	}
	
	
	public  Vector<String> FileWriter(String data) {
		
		Vector<String> res = new Vector<String>(10);
		BufferedWriter bw = null;
		
		try {
			// 用1024*1024*5 = 5M的缓冲读取文本文件,导致数据写入速度慢
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
	 * SQL写入本地磁盘
	 * @message 转发信息统计
	 * @return
	 * @throws IOException 
	 */
	public Boolean SQLWriter(Vector<Content> message) throws IOException {
		
		// 选择案例，如西藏问题.
		if (FileCreator("D:\\ls.sql")) {
			System.out.println("文件创建成功!");
		}
		else {
			System.out.println("文件创建失败!");
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
			// 写入文件(提供给陈学敏展示数据)
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

