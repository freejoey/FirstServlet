package servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mx on 16/11/21.
 */
@WebServlet(name = "BroadcastServlet")
public class BroadcastServlet extends HttpServlet {
    private static final int BROADCAST_INT_PORT = 40003;
    private static final String BROADCAST_IP = "230.0.0.1";

    private Timer timer = null;
    private MulticastSocket broadSocket;
    private InetAddress broadAddress;
    private DatagramSocket sender;
    private String myIP;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.out.println("---------------init BroadcastServlet----------------");

        try {
            broadSocket = new MulticastSocket(BROADCAST_INT_PORT);
            broadAddress = InetAddress.getByName(BROADCAST_IP);
            sender = new DatagramSocket();
            broadSocket.joinGroup(broadAddress);

            InetAddress netAddress = InetAddress.getLocalHost();
            myIP = netAddress.getHostAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }

        timer = new Timer(true);
        //timer.schedule(new SenderTask(), 0, 1000 * 5);
        timer.schedule(new SenderAllTask(), 0, 1000 * 5);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void destroy() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }


    //组播
    class SenderTask extends TimerTask {

        @Override
        public void run() {
            System.out.println("***************Broadcast IP:" + myIP + "***************");

            DatagramPacket packet;
            try {
                byte[] b = myIP.getBytes();
                packet = new DatagramPacket(b, b.length, broadAddress, BROADCAST_INT_PORT);

                sender.send(packet);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //广播
    class SenderAllTask extends TimerTask {

        @Override
        public void run() {
            System.out.println("***************Broadcast IP:" + myIP + "***************");

            // 广播的实现 :由客户端发出广播，服务器端接收
            String host = "255.255.255.255";//广播地址  //可行:192.168.2.255
            int port = 9999;//广播的目的端口
            try {
                InetAddress adds = InetAddress.getByName(host);
                DatagramSocket ds = new DatagramSocket();
                DatagramPacket dp = new DatagramPacket(myIP.getBytes(), myIP.length(), adds, port);
                ds.send(dp);
                ds.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
