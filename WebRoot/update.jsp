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
  	更新用户信息：
  	<br>
<s:fielderror></s:fielderror>
     <s:form action="doupdate" method="post" >
   		<s:hidden name="id" value="%{#u.id}"></s:hidden>
   		<s:textfield name="username" value="%{#u.username}" label="用户名"></s:textfield>
   		<s:password name="password" value="%{#u.password}" label="密码"></s:password>
   		<s:textfield name="age" value="%{#u.age}" label="年龄"></s:textfield>
   		<s:textfield name="email" value="%{#u.email}" label="邮件地址"></s:textfield>
   		<s:submit value="更新"></s:submit>
     </s:form>
  </body>
</html>
