<%@ page pageEncoding="utf-8" import="java.sql.*" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/6/27
  Time: 13:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>位置信息显示页面</title>
  </head>
  <body>

  <jsp:useBean id="db" class="DataBase.DbConn"></jsp:useBean>
  <%
    String querystr="select * from SqlHelpers";
    ResultSet rs=db.doQuery(querystr);
    out.println("<center><table border=1  align=center>");
    out.println("<tr>");
    out.println("<th>注册邮箱</th>");
    out.println("<th>用户姓名</th>");
    out.println("<th>性别</th>");
    out.println("<th>出生日期</th>");
    out.println("<th>联系电话</th>");
    out.println("<th>居住城市</th>");
    out.println("<th>个人说明</th>");
    out.println("</tr>");

    while(rs.next()){
      out.println("<tr>");
      out.println("<td bgcolor='#778899'>"+rs.getString("Username")+"</td>");

      out.println("<td>"+rs.getFloat("Locationx")+"</td>");
      out.println("<td>"+rs.getFloat("Locationy")+"</td>");
      out.println("<td>"+rs.getString("LogTime")+"</td>");

      out.println("</tr>");
    }
    out.println("</table></center>");
  %>

  </body>
</html>
