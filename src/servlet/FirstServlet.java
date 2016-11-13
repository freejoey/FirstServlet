package servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by mx on 16/11/9.
 */
public class FirstServlet extends HttpServlet {
    public final String CheckNetMessage = "isNetOk";
    public final String MessageName = "tag";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String receive = req.getParameter(MessageName);
        String response = "-1";
        if (null != receive && receive.equals(CheckNetMessage)) {
            response = "0";
        }

        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();
        pw.write(response);
        pw.flush();
        pw.close();

        //super.doGet(req, resp);
    }
}
