package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.util.Properties;

/**
 * Created by mx on 16/11/24.
 */
@WebServlet(name = "GetServerImagesServlet")
public class GetServerImagesServlet extends HttpServlet {
    private final String MessageName = "tag";
    private final int PAGE_SIZE = 10;
    private final static String SPLIT_TAG = "&#&#";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String receive = request.getParameter(MessageName);
        String re = "";
        InetAddress netAddress = InetAddress.getLocalHost();
        String serverAddr = "http://"
                + netAddress.getHostAddress()
                + ":8080/FirstServlet"
                + "/image/";

        int sumPic = 0;
        int page = 0;
        if (null != receive && !receive.equals("")) {
            page = Integer.parseInt(receive);
        }

        String path = request.getRealPath("") + "/image/";
        File ff = new File(path);
        File[] files = ff.listFiles();
        if (ff.exists() && null != files && files.length > 0) {
            sumPic = files.length;
            int end = Math.min(sumPic, PAGE_SIZE * (page + 1));
            StringBuilder sb = new StringBuilder();
            for (int i = (page * PAGE_SIZE + 1); i < end; i++) {
                sb.append(serverAddr + files[i].getName());
                sb.append(SPLIT_TAG);
            }
            re = sb.toString();
        }

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        pw.write(re);
        pw.flush();
        pw.close();
    }

    /**
     * @param im          原始图像
     * @param resizeTimes 倍数,比如0.5就是缩小一半,0.98等等double类型
     * @return 返回处理后的图像
     */
    private BufferedImage zoomImage(BufferedImage im, float resizeTimes) {
        /*原始图像的宽度和高度*/
        int width = im.getWidth();
        int height = im.getHeight();

        /*调整后的图片的宽度和高度*/
        int toWidth = (int) (Float.parseFloat(String.valueOf(width)) * resizeTimes);
        int toHeight = (int) (Float.parseFloat(String.valueOf(height)) * resizeTimes);

        /*新生成结果图片*/
        BufferedImage result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);

        result.getGraphics().drawImage(im.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        return result;
    }
}
