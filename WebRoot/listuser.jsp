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
  	��ӭ ${sessionScope.user.username}����½ϵͳ��|| <s:a href="./logout.action">ע��</s:a>
    <hr>
    <center>
    �����û���ϢΪ��<br>
    <table border="1">
    	<tr><th>id</th><th>username</th><th>age</th><th>email</th><th>�޸�</th><th>ɾ��</th></tr>
    	<s:iterator value="%{users}">
    		<tr><td><s:property value="id"/> </td><td><s:property value="username"/></td><td><s:property value="age"/></th><th><s:property value="email"/></td><td><s:a href="update.action?id=%{id}">�޸�</s:a>  </td><td><s:a href="delete.action?id=%{id}">ɾ��</s:a></td></tr>
    	</s:iterator>
    
    </table>
    </center>
  </body>
</html>
