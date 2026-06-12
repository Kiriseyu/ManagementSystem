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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/register")
public class RegisterServlet extends HttpServlet {
    private static final Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        Map<String, Object> result = new HashMap<>();

        if (username == null || password == null || email == null) {
            result.put("success", false);
            result.put("error", "参数不能为空");
            response.getWriter().write(gson.toJson(result));
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            
            String checkSql = "SELECT COUNT(*) FROM sys_user WHERE username = ? OR email = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            var rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                result.put("success", false);
                result.put("error", "用户名或邮箱已存在");
                response.getWriter().write(gson.toJson(result));
                return;
            }

            String sql = "INSERT INTO sys_user (username, password, email, role) VALUES (?, MD5(?), ?, 'user')";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                result.put("success", true);
            } else {
                result.put("success", false);
                result.put("error", "注册失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", "数据库错误");
        } finally {
            DBUtil.close(conn, pstmt);
        }

        response.getWriter().write(gson.toJson(result));
    }
}