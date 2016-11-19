<%@ page import="java.util.Properties" %>
<%@ page import="java.io.*" %>
<%
    //String realpath = request.getRealPath("/WEB-INF/classes");
    String realpath = request.getRealPath("");
    String fileName = realpath + "config.properties";

    request.setCharacterEncoding("utf-8");
    String txtMsg = request.getParameter("test2");
    if (null != txtMsg && !txtMsg.equals("")) {
        //统一windows路径符号
        txtMsg = txtMsg.replace("\\", "/");

        //覆盖properties文件以前内容
        File f = new File(fileName);
        if (!f.exists()) {
            f.createNewFile();
        }
        FileWriter writer = new FileWriter(f, false);
        writer.write("path=" + txtMsg);
        writer.flush();
        writer.close();
    }
%>
