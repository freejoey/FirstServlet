package servlet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mx on 16/11/9.
 */
@WebServlet(name = "ImageReceiverServlet")
public class ImageReceiverServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String re = "-1";

        String path = request.getSession().getServletContext().getRealPath("/images");
        List piclist = new ArrayList();  //放上传的图片名

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload sfu = new ServletFileUpload(factory);
        sfu.setHeaderEncoding("UTF-8");  //处理中文问题
        sfu.setSizeMax(20 * 1024 * 1024);   //限制文件大小

        try {
            List<FileItem> fileItems = sfu.parseRequest(request);  //解码请求 得到所有表单元素

            for (FileItem fi : fileItems) {
                //有可能是 文件，也可能是普通文字
                if (fi.isFormField()) { //这个选项是 文字
                    System.out.println("表单值为：" + fi.getString());
                } else {
                    // 是文件
                    String fn = fi.getName();
                    System.out.println("文件名是：" + fn);  //文件名
                    // fn 是可能是这样的 c:\abc\de\tt\fish.jpg
                    fi.write(new File(path, fn));

                    //if (fn.endsWith(".jpg")) {
                    piclist.add(fn);  //把图片放入集合
                    //}
                }
            }

            re = toJason(piclist);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回上传完成消息
        response.setContentType("text/json");
        PrintWriter pw = response.getWriter();
        pw.write(re);
        pw.flush();
        pw.close();
    }

    private static String toJason(List<String> list) {
        JSONArray jsonArray = new JSONArray();
        for (String s : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("image", s);
            jsonArray.add(jsonObject);
        }

        return jsonArray.toString();
    }
}
