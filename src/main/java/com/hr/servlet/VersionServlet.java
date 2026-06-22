package com.hr.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 版本信息Servlet
 * 用于检查项目是否为最新版本，返回构建时间戳
 */
@WebServlet("/api/version")
public class VersionServlet extends HttpServlet {
    
    // 项目版本号
    private static final String VERSION = "1.0.0";
    
    // 构建时间（每次部署时更新）
    private static final String BUILD_TIMESTAMP = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        String response = String.format(
            "{\"version\":\"%s\",\"buildTime\":\"%s\",\"serverTime\":\"%s\"}",
            VERSION,
            BUILD_TIMESTAMP,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        
        out.print(response);
        out.flush();
    }
}