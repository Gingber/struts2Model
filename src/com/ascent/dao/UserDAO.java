/**
 * 
 */
package com.ascent.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ascent.javabean.Message;
import com.ascent.javabean.User;
import com.ascent.util.DataAccess;
import com.ascent.util.TxtReader;
import com.iie.rtp.Content;

/**
 * @author zy
 *
 */
public final class UserDAO {
	public void register(String username,String password,int age ,String email) throws ClassNotFoundException{
		
		Connection con = null ;
		PreparedStatement pst = null ;
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/strutsmodel","root","hadoop");
			con = DataAccess.getConnection();
			pst = con.prepareStatement("insert into user(username,password,age,email) values(?,?,?,?)");
			pst.setString(1, username);
			pst.setString(2, password);
			pst.setInt(3,age);
			pst.setString(4, email);
			pst.execute();
		}catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pst!=null){
					pst.close();
				}
				if(con!=null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public User validate(String username,String password){
		Connection con = null ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		User user = null ;
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/strutsmodel","root","wjj");
			 con = DataAccess.getConnection();
			pst = con.prepareStatement("select * from user where username=? and password=?");
			pst.setString(1, username);
			pst.setString(2, password);
			rs = pst.executeQuery();
			if(rs.next()){
				int id = rs.getInt("id");
				String name= rs.getString("username");
				String word = rs.getString("password");
				int age = rs.getInt("age");
				String email = rs.getString("email");
				user = new User(id,name,word,age,email);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pst!=null){
					pst.close();
				}
				if(con!=null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user ;
	}
	
	
	public static final int isChinese(String title) {  
		
		// 剔除字符串中特殊字符，仅保留数字、字母和汉字等
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern   p  =  Pattern.compile(regEx);   
		Matcher   m  =  p.matcher(title); 
		
		int counter = 0;
        char[] ch = m.replaceAll("").trim().toCharArray();
        for (int i = 0; i < ch.length; i++) { 
            char c = ch[i];  
            if (c >= 0x4e00 && c <= 0x9fa5) {  
            	++counter;  
            }  
        }  
        return counter;  
    }  
	
	 //<<<<<<<---------------------时间格式统一处理模块--------------------->>>>>>>>>>>>>
	 String NormTimeFormat(String create_time) throws ParseException
     {
         String NormCreateTime = null;
         Date date = new Date();
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);  // 最终显示时间格式
         
         if (create_time.contains("上午") | create_time.contains("下午")) {
        	 String substr = create_time.substring(Math.max(create_time.length() - 2, 0)); // 提取尾部字符
        	 if (substr.contains("上午") | substr.contains("下午")) { //判断是否为"上午"或"下午"
        		 SimpleDateFormat cformatter = new SimpleDateFormat("yy年MM月dd日, hh:mm a", Locale.CHINA); // a在hh:mm后面
                 // Parse the date
            	 try { 
              		date = cformatter.parse(create_time);	
              		//System.out.println("中文正确转化数据: " + sdf.format(date));
              		NormCreateTime = sdf.format(date);
              	} catch (ParseException e) {
              		e.printStackTrace();
              	}  
        	 } else {
        		 SimpleDateFormat cformatter = new SimpleDateFormat("yy年MM月dd日, a hh:mm", Locale.CHINA); // a在hh:mm前面
                 // Parse the date
            	 try { 
              		date = cformatter.parse(create_time);	
              		//System.out.println("中文正确转化数据: " + sdf.format(date));
              		NormCreateTime = sdf.format(date);
              	} catch (ParseException e) {
              		e.printStackTrace();
              	} 
        	 }
         } else { 
         	SimpleDateFormat eformatter = new SimpleDateFormat("hh:mm aa - dd MMM yy", Locale.ENGLISH); // 时间显示地域
            // Parse the date
         	try {	 
         		date = eformatter.parse(create_time);
         		//System.out.println("英文正确转化数据: " + sdf.format(date));
         		NormCreateTime = sdf.format(date);
         	} catch (ParseException e) {
         		e.printStackTrace();
         	}
         }
         
         return NormCreateTime;
     }
	 
	 //<<<<<<<---------------------时间格式统一处理模块--------------------->>>>>>>>>>>>>
	 
	 public List<String> findSensitiveWords() throws ClassNotFoundException, ParseException{
		Connection con = null ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		List<String> SenWordsList = new ArrayList<String>();
		//敏感词表
		String SensitiveWords = "民主 人權 人权 自由 改革 選舉 选举 多黨 多党 平反 獨裁 独裁 法西斯 專政 专政 專制 专制 反共 反黨 反党 反革命 反動  " +
				"反动 共匪 共惨党 群體滅絕 群体灭绝 鎮壓 镇压 推翻 政變 政变 打倒 維權 维权 封鎖 封锁 勞教 劳教 紅色恐佈 红色恐怖 邪惡 邪恶 流亡 红色的法拉利 " +
				"六四 天安門事件 天安门事件 民運 民運 疆獨 疆独 藏獨 藏独 達賴 达赖 江澤民 江泽民 江賊 江贼  賊民 贼民 江賊民 江流氓  法輪 法轮 法倫 法伦 輪功 轮功 輪大 轮大 大法 洪志 弟子 真善忍 明慧 " +
				"民進黨 民进党 臺灣團結聯盟 台湾团结联盟 泛綠 泛绿 中華民國 中华民国 臺獨 台独 國民黨 国民党 泛藍 泛蓝 陳水扁 陈水扁  呂秀蓮 吕秀莲 李登輝 李登辉 宋美齡 宋美龄 蔣經國 蒋经国 蔣方良 蒋方良 馬英九 马英九";
		String[] SenWordsArr = SensitiveWords.split(" ");
		
		for(int i  = 0; i < SenWordsArr.length; i++) {
			if(!SenWordsList.contains(SenWordsArr[i]))
				SenWordsList.add(SenWordsArr[i]);
		}
		
		/*try {
			con = DataAccess.getConnection();
			pst = con.prepareStatement("SELECT TaskParameter2 FROM inputtask");
			rs = pst.executeQuery();
			while(rs.next()){
				String[] SenUserArr = rs.getString("TaskParameter2").split(" ");
				for(int i  = 0; i < SenUserArr.length; i++) {
					if(!SenUserList.contains(SenUserArr[i]))
						SenUserList.add(SenUserArr[i]);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pst!=null){
					pst.close();
				}
				if(con!=null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}*/
		
		return SenWordsList;
	 }
	
	public List<Message> findAllMessage() throws ClassNotFoundException, ParseException, IOException{
		Connection con = null ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Message message = null ;
		List<Message> messages = new ArrayList<Message>();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		
		List<String> SensitiveWords = TxtReader.loadVectorFromFile(new File("file/sw.txt"), "utf-8");
		
		try {
			con = DataAccess.getConnection();
			/*pst = con.prepareStatement("CREATE INDEX idxMTUC ON message (message_id, title, user_id, create_time)");
			pst.executeUpdate();*/
			
			pst = con.prepareStatement("SELECT message_id, title, user_id, create_time FROM message LIMIT 0, 3000000");
			rs = pst.executeQuery();
			while(rs.next()){
				String title= rs.getString("title");
				if (isChinese(title) > 10) {
					for (int i = 0; i < SensitiveWords.size(); i++) {
						if (title.contains(SensitiveWords.get(i))) {
							String message_id = rs.getString("message_id");
							String user_name = rs.getString("user_id");
							Date create_time = rs.getTimestamp("create_time");
							
							message = new Message(message_id, title, user_name, create_time);
							messages.add(message);
							
							continue;
						}
					}
				}
				
				/*String message_id = rs.getString("message_id");
				String user_name = rs.getString("user_id");
				Date create_time = rs.getTimestamp("create_time");
				String create_time = rs.getString("create_time").toString();
				String NormTime = NormTimeFormat(create_time);
				Date ProTime = sdf.parse(NormTime);
				String title= rs.getString("title");
				if (isChinese(title) > 10) {
					message = new Message(message_id, title, user_name, create_time);
					messages.add(message);
				}*/
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pst!=null){
					pst.close();
				}
				if(con!=null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return messages ;
	}
	
	public boolean insertUserTop(List<String> username) throws ClassNotFoundException{
		
		Connection con = null ;
		PreparedStatement pst = null ;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/twitter_api_0424?user=root&password=hadoop&useUnicode=true&characterEncoding=utf-8");
			//con = DataAccess.getConnection();
			pst = con.prepareStatement("delete from usertop");
			pst.executeUpdate();
			
			pst = con.prepareStatement("insert into usertop(userTopName) values(?)");
			for (String name : username) {
				pst.setString(1, name);
				// 把一个SQL命令加入命令列表  j
			    pst.addBatch();
			}
			// 执行批量更新  
			pst.executeBatch();    
			
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally{
			try {
				if(pst!=null){
					pst.close();
				}
				if(con!=null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public boolean insertHotTweet(Vector<Content> message) throws ClassNotFoundException{
		
		Connection con = null ;
		PreparedStatement pst = null ;
		try { 
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/twitter_api_0424?user=root&password=hadoop&useUnicode=true&characterEncoding=utf-8");
			//con = DataAccess.getConnection();
			pst = con.prepareStatement("delete from hot_tweet");
			pst.executeUpdate();
			
			pst = con.prepareStatement("insert into hot_tweet(username, retweet, forward, friends, create_time, popularity) values(?,?,?,?,?,?)");
			int AllRtNum = 0;
			for (int i = 0; i < message.size(); i++) {
				AllRtNum += message.get(i).getForwardnum();
			}
			for (int i = 0; i < message.size(); i++) {
				pst.setString(1, message.get(i).getFirstuser());
				pst.setString(2, message.get(i).getTitle());
				pst.setInt(3, message.get(i).getForwardnum());
				pst.setInt(4, 100);
				pst.setTimestamp(5, new Timestamp(message.get(i).getStart_time().getTime()));
				pst.setFloat(6, (float) message.get(i).getForwardnum()/AllRtNum);
				
				// 把一个SQL命令加入命令列表  
			    pst.addBatch();
			}
			// 执行批量更新  
			pst.executeBatch();    
			
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally{
			try {
				if(pst!=null){
					pst.close();
				}
				if(con!=null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public boolean insertRetweetTrend(Vector<Content> message) throws ClassNotFoundException{
		
		Connection con = null ;
		PreparedStatement pst = null ;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/twitter_api_0424?user=root&password=hadoop&useUnicode=true&characterEncoding=utf-8");
			//con = DataAccess.getConnection();
			pst = con.prepareStatement("delete from retweet_trend");
			pst.executeUpdate();
			
			pst = con.prepareStatement("insert into retweet_trend(twitterID, tranNum, userNum, " +
					"tOrig, tContent, tStartTime, tLastTime, keywords) values(?,?,?,?,?,?,?,?)");
			for (int i = 0; i < message.size(); i++) {
				pst.setInt(1, i+1);
				pst.setInt(2, message.get(i).getForwardnum());
				pst.setInt(3, message.get(i).getUsernum());
				pst.setString(4, message.get(i).getFirstuser());
				pst.setString(5, message.get(i).getTitle());
				pst.setTimestamp(6, new Timestamp(message.get(i).getStart_time().getTime()));
				pst.setTimestamp(7, new Timestamp(message.get(i).getEnd_time().getTime()));
				pst.setString(8, "测试中...");
				
				// 把一个SQL命令加入命令列表  
			    pst.addBatch();
				
				
			}
			
			// 执行批量更新  
			pst.executeBatch();
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally{
			try {
				if(pst!=null){
					pst.close();
				}
				if(con!=null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	
	public List<User> findAll(){
		Connection con = null ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		User user = null ;
		List<User> users = new ArrayList<User>();
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/strutsmodel","root","wjj");
			 con = DataAccess.getConnection();
			pst = con.prepareStatement("select * from user");
			rs = pst.executeQuery();
			while(rs.next()){
				int id = rs.getInt("id");
				String name= rs.getString("username");
				String word = rs.getString("password");
				int age = rs.getInt("age");
				String email = rs.getString("email");
				user = new User(id,name,word,age,email);
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pst!=null){
					pst.close();
				}
				if(con!=null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return users ;
	}
	
	public void deleteUser(int id){
		Connection con = null ;
		PreparedStatement pst = null ;
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/strutsmodel","root","wjj");
			 con = DataAccess.getConnection();
			pst = con.prepareStatement("delete from user where id=? ");
			pst.setInt(1, id);
			pst.execute();
		}catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pst!=null){
					pst.close();
				}
				if(con!=null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public User findUserByid(int id){
		Connection con = null ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		User user = null ;
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/strutsmodel","root","wjj");
			 con = DataAccess.getConnection();
			pst = con.prepareStatement("select * from user where id=?");
			pst.setInt(1, id);
			rs = pst.executeQuery();
			if(rs.next()){
				int idd = rs.getInt("id");
				String name= rs.getString("username");
				String word = rs.getString("password");
				int age = rs.getInt("age");
				String email = rs.getString("email");
				user = new User(idd,name,word,age,email);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pst!=null){
					pst.close();
				}
				if(con!=null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user ;
	}
	
	public void updateUser(int id,String username,String password,int age ,String email){
		Connection con = null ;
		PreparedStatement pst = null ;
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/strutsmodel","root","wjj");
			 con = DataAccess.getConnection();
			pst = con.prepareStatement("update user set username=? , password=?,age=?,email=? where id=?");
			pst.setString(1, username);
			pst.setString(2, password);
			pst.setInt(3,age);
			pst.setString(4, email);
			pst.setInt(5, id);
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(pst!=null){
					pst.close();
				}
				if(con!=null){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
