<%@ page import="java.net.InetAddress" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.io.*" %><%--
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
    //String realpath = request.getRealPath("/WEB-INF/classes");
    String fileName = request.getRealPath("") + "config.properties";
    try {
        //读取配置文件
        //InputStream in = request.getSession().getServletContext().getResourceAsStream(fileName);
        //BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        FileInputStream in = new FileInputStream(fileName);
        pro.load(in);
    } catch (FileNotFoundException e) {
        out.println(e);
    } catch (IOException e) {
        out.println(e);
    }

    String path = "";
    int sumPic = 0;
    //通过key获取配置文件
    if (null != pro) {
        path = pro.getProperty("path");

        if (null != path) {
            File ff = new File(path);
            if (ff.exists()) {
                sumPic = ff.listFiles().length;
            }
        }
    }
%>

<script>
    function changePath() {
        var v = document.getElementById("input_path").value;
        if (null == v || "" == v) {
            return;
        }
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
        <td id="td_sum" align="center"><%=sumPic%>
        </td>
    </tr>
</table>

<p style='margin-top:30px'>输入新的图片保存目录:</p>

<p>
    <input id="input_path" style='width:200px'/>
    <button style='margin-left:10px' id="bt_change_path" onclick="changePath()">重置路径</button>
</p>

<button style='margin-top:20px' id="bt_check_image">查看图片</button>

<%--<form method="post" action="write_properties.jsp" id="passForm" target="nm_iframe">--%>
<%--<input id='test2' type='hidden' name="test2">--%>
<%--</form>--%>
<form method="post" action="write_properties.jsp" id="passForm">
    <input id='test2' type='hidden' name="test2">
</form>

<iframe id="id_iframe" name="nm_iframe" style="display:none;"></iframe>
</body>
</html>
