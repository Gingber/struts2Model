/**
 * 
 */
package com.ascent.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ascent.javabean.Message;
import com.ascent.javabean.User;
import com.ascent.util.DataAccess;

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
		
		// �޳��ַ����������ַ������������֡���ĸ�ͺ��ֵ�
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~��@#��%����&*��������+|{}������������������������]";
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
	
	 //<<<<<<<---------------------ʱ���ʽͳһ����ģ��--------------------->>>>>>>>>>>>>
	 String NormTimeFormat(String create_time) throws ParseException
     {
         String NormCreateTime = null;
         Date date = new Date();
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);  // ������ʾʱ���ʽ
         
         if (create_time.contains("����") | create_time.contains("����")) {
        	 String substr = create_time.substring(Math.max(create_time.length() - 2, 0)); // ��ȡβ���ַ�
        	 if (substr.contains("����") | substr.contains("����")) { //�ж��Ƿ�Ϊ"����"��"����"
        		 SimpleDateFormat cformatter = new SimpleDateFormat("yy��MM��dd��, hh:mm a", Locale.CHINA); // a��hh:mm����
                 // Parse the date
            	 try { 
              		date = cformatter.parse(create_time);	
              		//System.out.println("������ȷת������: " + sdf.format(date));
              		NormCreateTime = sdf.format(date);
              	} catch (ParseException e) {
              		e.printStackTrace();
              	}  
        	 } else {
        		 SimpleDateFormat cformatter = new SimpleDateFormat("yy��MM��dd��, a hh:mm", Locale.CHINA); // a��hh:mmǰ��
                 // Parse the date
            	 try { 
              		date = cformatter.parse(create_time);	
              		//System.out.println("������ȷת������: " + sdf.format(date));
              		NormCreateTime = sdf.format(date);
              	} catch (ParseException e) {
              		e.printStackTrace();
              	} 
        	 }
         } else { 
         	SimpleDateFormat eformatter = new SimpleDateFormat("hh:mm aa - dd MMM yy", Locale.ENGLISH); // ʱ����ʾ����
            // Parse the date
         	try {	 
         		date = eformatter.parse(create_time);
         		//System.out.println("Ӣ����ȷת������: " + sdf.format(date));
         		NormCreateTime = sdf.format(date);
         	} catch (ParseException e) {
         		e.printStackTrace();
         	}
         }
         
         return NormCreateTime;
     }
	 
	 //<<<<<<<---------------------ʱ���ʽͳһ����ģ��--------------------->>>>>>>>>>>>>
	
	public List<Message> findAllMessage() throws ClassNotFoundException, ParseException{
		Connection con = null ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Message message = null ;
		List<Message> messages = new ArrayList<Message>();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		
		try {
			con = DataAccess.getConnection();
			pst = con.prepareStatement("SELECT message_id, title, user_id, create_time FROM message LIMIT 0, 100000");
			rs = pst.executeQuery();
			while(rs.next()){
				String message_id = rs.getString("message_id");
				String user_name = rs.getString("user_id");
				//Date create_time = rs.getTimestamp("create_time");
				String create_time = rs.getString("create_time").toString();
				String NormTime = NormTimeFormat(create_time);
				Date ProTime = sdf.parse(NormTime);
				String title= rs.getString("title");
				if (isChinese(title) != 0){
					message = new Message(message_id, title, user_name, ProTime);
					messages.add(message);
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
		}
		return messages ;
	}
	
	public boolean insertUserTop(List<String> username) throws ClassNotFoundException{
		
		Connection con = null ;
		PreparedStatement pst = null ;
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			//con = DriverManager.getConnection("jdbc:mysql://localhost:3306/strutsmodel","root","hadoop");
			con = DataAccess.getConnection();
			pst = con.prepareStatement("insert into usertop9(userTopName) values(?)");
			for (String name : username) {
				pst.setString(1, name);
				// ��һ��SQL������������б�  
			    pst.addBatch();
			}
			// ִ����������  
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
