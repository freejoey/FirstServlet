<%@ page import="java.io.*" %>
<%@ page import="org.w3c.dom.Node" %>
<%@ page import="org.w3c.dom.Document" %>
<%@ page import="javax.xml.parsers.DocumentBuilder" %>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory" %>
<%@ page import="org.w3c.dom.Element" %>
<%@ page import="javax.xml.transform.stream.StreamResult" %>
<%@ page import="javax.xml.transform.dom.DOMSource" %>
<%@ page import="javax.xml.transform.OutputKeys" %>
<%@ page import="javax.xml.transform.Transformer" %>
<%@ page import="javax.xml.transform.TransformerFactory" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    //String realpath = request.getRealPath("/WEB-INF/classes");
    String realpath = request.getRealPath("");
    String fileName = realpath + "config.properties";

    request.setCharacterEncoding("utf-8");
    String txtMsg = request.getParameter("test2");
    if (null != txtMsg && !txtMsg.equals("")) {
        //统一windows路径符号
        txtMsg = txtMsg.replace("\\", "/");
        txtMsg += "/";

        //覆盖properties文件以前内容
        File f = new File(fileName);
        if (!f.exists()) {
            f.createNewFile();
        }
        FileWriter writer = new FileWriter(f, false);
        writer.write("path=" + txtMsg);
        writer.flush();
        writer.close();

        //更新server.xml文件的虚拟路径配置
        try {
            String tomcatPath = new File(request.getRealPath("")).getParentFile().getParent();
            String filename = tomcatPath + "/conf/" + "server.xml";
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new File(filename));
            Element rootElem = document.getDocumentElement();
            Node node = rootElem.getElementsByTagName("Host").item(0);

            Element eltName = document.createElement("Context");
            eltName.setAttribute("docBase", txtMsg);
            eltName.setAttribute("reloadable", "true");
            eltName.setAttribute("debug", "0");
            eltName.setAttribute("path", "/setPath");
            node.replaceChild(eltName, rootElem.getElementsByTagName("Context").item(0));

            //写入conf/server.xml配置文件
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            /** 编码 */
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);

        } catch (Exception e) {
            out.println(e);
        }
    }
%>

<html>
<head>
    <title>设置成功</title>
</head>
<body>
<p>设置成功,请重启服务器!</p>
</body>
</html>
