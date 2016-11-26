package servlet;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
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
    private final int PAGE_SIZE = 30;
    private final static String SPLIT_TAG = "&#&#";
    private final int IMAGE_SIZE = 256;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String receive = request.getParameter(MessageName);
        String re = "";
        InetAddress netAddress = InetAddress.getLocalHost();
        String serverAddr = "http://"
                + netAddress.getHostAddress()
                + ":8080/"
                + "FirstServlet/thumbnail/";

        int sumPic = 0;
        int page = 0;
        if (null != receive && !receive.equals("")) {
            page = Integer.parseInt(receive);
        }

        Properties pro = new Properties();
        String fileName = request.getRealPath("") + "config.properties";
        boolean existProperty = true;
        try {
            //读取配置文件
            //InputStream in = request.getSession().getServletContext().getResourceAsStream(fileName);
            //BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            FileInputStream in = new FileInputStream(fileName);
            pro.load(in);
        } catch (FileNotFoundException e) {
            existProperty = false;
            System.out.println(e);
        } catch (IOException e) {
            existProperty = false;
            System.out.println(e);
        }

        String storePath = pro.getProperty("path");
        if (null == storePath || storePath.equals("") && !existProperty) {
            storePath = request.getRealPath("") + "/image/";
        }

        File thumbFile = new File(request.getRealPath("") + "/thumbnail/");
        File ff = new File(storePath);
        File[] files = ff.listFiles();
        if (ff.exists() && null != files && files.length > 0) {
            sumPic = files.length;
            StringBuilder sb = new StringBuilder();
            int count = 0;

            for (int i = page * PAGE_SIZE; i < sumPic && count < PAGE_SIZE; i++) {
                if (files[i].isDirectory() || !isImage(files[i])) {
                    continue;
                }

                String imageName = files[i].getName().replace(" ", "");

                //如果该图片的缩略图不存在,存一张缩略图
                File imageFile = new File(thumbFile, imageName);
                if (!(imageFile.exists())) {
                    try {
                        BufferedImage input = ImageIO.read(files[i]);
                        BufferedImage inputbig = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_BGR);
                        Graphics2D g = (Graphics2D) inputbig.getGraphics();
                        g.drawImage(input, 0, 0, IMAGE_SIZE, IMAGE_SIZE, null); //画图
                        g.dispose();
                        inputbig.flush();

                        String tail = imageName.substring(imageName.lastIndexOf(".") + 1);
                        ImageIO.write(inputbig, tail, imageFile);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

                //生成图片地址
                sb.append(serverAddr + imageName);
                sb.append(SPLIT_TAG);
                count++;
            }
            re = sb.toString();
        }

        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        pw.write(re);
        pw.flush();
        pw.close();
    }

    private boolean isImage(File file) {
        boolean flag = false;
        try {
            ImageInputStream is = ImageIO.createImageInputStream(file);
            if (null == is) {
                return false;
            }
            String pureName = file.getName().substring(0, file.getName().lastIndexOf("."));
            if (pureName.equals("")) {
                return false;
            }
            is.close();
            flag = true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return flag;
    }
}
