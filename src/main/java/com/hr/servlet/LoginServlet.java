package com.hr.servlet;

import com.hr.util.DBUtil;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    private static final Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        Map<String, Object> result = new HashMap<>();

        if (username == null || password == null || role == null) {
            result.put("success", false);
            result.put("error", "参数不能为空");
            response.getWriter().write(gson.toJson(result));
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT user_id, username, role, status FROM sys_user WHERE username = ? AND password = MD5(?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int status = rs.getInt("status");
                if (status == 0) {
                    result.put("success", false);
                    result.put("error", "账号已被禁用");
                } else {
                    String userRole = rs.getString("role");
                    if (!userRole.equals(role)) {
                        result.put("success", false);
                        result.put("error", "角色不匹配");
                    } else {
                        result.put("success", true);
                        result.put("userId", rs.getInt("user_id"));
                        result.put("username", rs.getString("username"));
                        result.put("role", userRole);
                    }
                }
            } else {
                result.put("success", false);
                result.put("error", "用户名或密码错误");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", "数据库错误");
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        response.getWriter().write(gson.toJson(result));
    }
}