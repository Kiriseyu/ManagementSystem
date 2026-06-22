package com.hr.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 注销Servlet
 * 处理用户退出登录，清除session
 */
@WebServlet("/api/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();  // 使session失效，清除所有session属性
        }
        // 返回JSON响应
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().print("{\"success\":true,\"message\":\"退出成功\"}");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // GET请求也支持注销
        doPost(req, resp);
    }
}
