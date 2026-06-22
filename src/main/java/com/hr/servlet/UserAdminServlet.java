package com.hr.servlet;

import com.hr.util.DBUtil;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/users")
public class UserAdminServlet extends HttpServlet {
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            if (idParam != null) {
                String sql = "SELECT user_id, username, email, role, status, create_time FROM sys_user WHERE user_id = ? AND is_deleted = 0";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(idParam));
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("userId", rs.getInt("user_id"));
                    user.put("username", rs.getString("username"));
                    user.put("email", rs.getString("email"));
                    user.put("role", rs.getString("role"));
                    user.put("status", rs.getInt("status"));
                    user.put("createTime", rs.getString("create_time"));
                    out.print(gson.toJson(user));
                } else {
                    resp.setStatus(404);
                    out.print("{\"error\":\"User not found\"}");
                }
            } else {
                String sql = "SELECT user_id, username, email, role, status, create_time FROM sys_user WHERE is_deleted = 0";
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();

                List<Map<String, Object>> users = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("userId", rs.getInt("user_id"));
                    user.put("username", rs.getString("username"));
                    user.put("email", rs.getString("email"));
                    user.put("role", rs.getString("role"));
                    user.put("status", rs.getInt("status"));
                    user.put("createTime", rs.getString("create_time"));
                    users.add(user);
                }
                out.print(gson.toJson(users));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(500);
            out.print("{\"error\":\"Database error\"}");
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String role = req.getParameter("role");

        Map<String, Object> result = new HashMap<>();

        if (username == null || password == null) {
            result.put("success", false);
            result.put("error", "用户名和密码不能为空");
            out.print(gson.toJson(result));
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();

            String checkSql = "SELECT COUNT(*) FROM sys_user WHERE username = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                result.put("success", false);
                result.put("error", "用户名已存在");
                out.print(gson.toJson(result));
                return;
            }

            String sql = "INSERT INTO sys_user (username, password, email, role, status) VALUES (?, MD5(?), ?, ?, 1)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, role != null ? role : "user");

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                result.put("success", true);
                result.put("message", "用户添加成功");
            } else {
                result.put("success", false);
                result.put("error", "添加失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", "数据库错误");
        } finally {
            DBUtil.close(conn, pstmt);
        }

        out.print(gson.toJson(result));
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String role = req.getParameter("role");
        String status = req.getParameter("status");

        Map<String, Object> result = new HashMap<>();

        if (idParam == null) {
            result.put("success", false);
            result.put("error", "用户ID不能为空");
            out.print(gson.toJson(result));
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();

            StringBuilder sql = new StringBuilder("UPDATE sys_user SET ");
            List<Object> params = new ArrayList<>();

            if (username != null && !username.isEmpty()) {
                sql.append("username = ?, ");
                params.add(username);
            }
            if (email != null) {
                sql.append("email = ?, ");
                params.add(email);
            }
            if (role != null) {
                sql.append("role = ?, ");
                params.add(role);
            }
            if (status != null) {
                sql.append("status = ?, ");
                params.add(Integer.parseInt(status));
            }

            sql.append("update_time = CURRENT_TIMESTAMP WHERE user_id = ?");
            params.add(Integer.parseInt(idParam));

            pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                result.put("success", true);
                result.put("message", "用户更新成功");
            } else {
                result.put("success", false);
                result.put("error", "更新失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", "数据库错误");
        } finally {
            DBUtil.close(conn, pstmt);
        }

        out.print(gson.toJson(result));
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");

        Map<String, Object> result = new HashMap<>();

        if (idParam == null) {
            result.put("success", false);
            result.put("error", "用户ID不能为空");
            out.print(gson.toJson(result));
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();

            String sql = "UPDATE sys_user SET is_deleted = 1 WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(idParam));

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                result.put("success", true);
                result.put("message", "用户删除成功");
            } else {
                result.put("success", false);
                result.put("error", "删除失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", "数据库错误");
        } finally {
            DBUtil.close(conn, pstmt);
        }

        out.print(gson.toJson(result));
        out.flush();
    }
}