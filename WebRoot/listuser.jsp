<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%@ taglib prefix="s" uri="/struts-tags"  %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
  	欢迎 ${sessionScope.user.username}，登陆系统：|| <s:a href="./logout.action">注销</s:a>
    <hr>
    <center>
    所有用户信息为：<br>
    <table border="1">
    	<tr><th>id</th><th>username</th><th>age</th><th>email</th><th>修改</th><th>删除</th></tr>
    	<s:iterator value="%{users}">
    		<tr><td><s:property value="id"/> </td><td><s:property value="username"/></td><td><s:property value="age"/></th><th><s:property value="email"/></td><td><s:a href="update.action?id=%{id}">修改</s:a>  </td><td><s:a href="delete.action?id=%{id}">删除</s:a></td></tr>
    	</s:iterator>
    
    </table>
    </center>
  </body>
</html>
