package com.hr.servlet;

import com.hr.util.DBUtil;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人中心Servlet
 * 提供当前用户信息查看、个人信息修改、密码修改等功能
 */
@WebServlet("/api/profile")
public class ProfileServlet extends HttpServlet {
    private static final Gson gson = new Gson();

    /**
     * 获取当前用户信息
     * GET请求，需要在session中存储userId
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.setStatus(401);
            out.print("{\"error\":\"请先登录\"}");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT user_id, username, email, role, status, create_time FROM sys_user WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("userId", rs.getInt("user_id"));
                user.put("username", rs.getString("username"));
                user.put("email", rs.getString("email"));
                user.put("role", rs.getString("role"));
                user.put("status", rs.getInt("status"));
                user.put("createTime", rs.getTimestamp("create_time"));
                out.print(gson.toJson(user));
            } else {
                resp.setStatus(404);
                out.print("{\"error\":\"用户不存在\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(500);
            out.print("{\"error\":\"数据库错误\"}");
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    /**
     * 更新当前用户信息
     * PUT请求，修改邮箱等个人信息
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.setStatus(401);
            out.print("{\"error\":\"请先登录\"}");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        String email = req.getParameter("email");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE sys_user SET email = ? WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setInt(2, userId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                out.print("{\"success\":true,\"message\":\"信息更新成功\"}");
            } else {
                resp.setStatus(400);
                out.print("{\"success\":false,\"error\":\"更新失败\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(500);
            out.print("{\"success\":false,\"error\":\"数据库错误\"}");
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
    }

    /**
     * 修改密码
     * POST请求，action=password
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        String action = req.getParameter("action");
        if (!"password".equals(action)) {
            resp.setStatus(400);
            out.print("{\"error\":\"无效的操作\"}");
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.setStatus(401);
            out.print("{\"error\":\"请先登录\"}");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");

        if (oldPassword == null || newPassword == null || oldPassword.isEmpty() || newPassword.isEmpty()) {
            out.print("{\"success\":false,\"error\":\"密码不能为空\"}");
            return;
        }

        if (newPassword.length() < 6) {
            out.print("{\"success\":false,\"error\":\"新密码长度不能少于6位\"}");
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            // 验证原密码
            String checkSql = "SELECT user_id FROM sys_user WHERE user_id = ? AND password = MD5(?)";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, oldPassword);
            rs = pstmt.executeQuery();

            if (!rs.next()) {
                out.print("{\"success\":false,\"error\":\"原密码错误\"}");
                return;
            }
            DBUtil.close(null, pstmt, rs);

            // 更新新密码
            String updateSql = "UPDATE sys_user SET password = MD5(?) WHERE user_id = ?";
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                out.print("{\"success\":true,\"message\":\"密码修改成功\"}");
            } else {
                out.print("{\"success\":false,\"error\":\"密码修改失败\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(500);
            out.print("{\"success\":false,\"error\":\"数据库错误\"}");
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
    }
}
