<%@ page import="java.net.InetAddress" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.FileNotFoundException" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.util.Properties" %><%--
  Created by IntelliJ IDEA.
  User: mx
  Date: 16/11/9
  Time: 上午10:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>图片管理</title>
</head>

<body>
<%
    InetAddress netAddress = InetAddress.getLocalHost();
    String ip = netAddress.getHostAddress();

    Properties pro = new Properties();
    String realpath = request.getRealPath("/WEB-INF/classes");
    try {
        //读取配置文件
        FileInputStream in = new FileInputStream(realpath + "/config.properties");
        pro.load(in);
    } catch (FileNotFoundException e) {
        out.println(e);
    } catch (IOException e) {
        out.println(e);
    }

    String path = "";
    //通过key获取配置文件
    if (null != pro) {
        path = pro.getProperty("path");
    }

    request.setCharacterEncoding("utf-8");
    String txtMsg = request.getParameter("test2");
    if (null != txtMsg && !txtMsg.equals("")) {
        path = txtMsg;

        pro.put("path", path);
    }
%>

<script>
    function changePath() {
        var v = document.getElementById("input_path").value;
        document.getElementById("td_path").innerHTML = v;

        document.getElementById("test2").value = v;
        var formObj = document.getElementById('passForm');
        formObj.submit();
    }
</script>

<table border="1" width="800">
    <tr>
        <th width="200">服务器IP</th>
        <td id="td_ip" align="center"><%=ip%>
        </td>
    </tr>
    <tr>
        <th width="200">图片目录</th>
        <td id="td_path" align="center"><%=path%>
        </td>
    </tr>

    <tr>
        <th width="200">图片总数</th>
        <td id="td_sum" align="center">1200</td>
    </tr>
</table>

<p style='margin-top:30px'>选择文件目录:</p>

<p>
    <input type="file" id="input_path"/>
    <button style='margin-left:10px' id="bt_change_path" onclick="changePath()">确定</button>
</p>

<button style='margin-top:20px' id="bt_check_image">查看图片</button>

<form method="post" action="index.jsp" id="passForm">
    <input id='test2' type='hidden' name="test2">
</form>
</body>
</html>
