package com.hr.servlet;

import com.hr.dao.OperationLogDAO;
import com.hr.entity.OperationLog;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作日志Servlet
 */
@WebServlet("/api/log")
public class OperationLogServlet extends HttpServlet {
    private OperationLogDAO logDAO = new OperationLogDAO();
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String pageStr = req.getParameter("page");
        String sizeStr = req.getParameter("size");
        
        int page = pageStr != null ? Integer.parseInt(pageStr) : 1;
        int size = sizeStr != null ? Integer.parseInt(sizeStr) : 10;

        List<OperationLog> logs = logDAO.list(page, size);
        int total = logDAO.getTotalCount();

        Map<String, Object> result = new HashMap<>();
        result.put("data", logs);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);

        out.print(gson.toJson(result));
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        OperationLog log = new OperationLog();
        
        String userIdStr = req.getParameter("userId");
        if (userIdStr != null && !userIdStr.isEmpty()) {
            log.setUserId(Integer.parseInt(userIdStr));
        }
        log.setUsername(req.getParameter("username"));
        log.setOperation(req.getParameter("operation"));
        log.setMethod(req.getParameter("method"));
        log.setRequestUrl(req.getParameter("requestUrl"));
        log.setRequestMethod(req.getParameter("requestMethod"));
        log.setRequestParams(req.getParameter("requestParams"));
        log.setIpAddress(req.getParameter("ipAddress"));
        log.setStatus(Integer.parseInt(req.getParameter("status")));
        log.setErrorMessage(req.getParameter("errorMessage"));

        Map<String, Object> result = new HashMap<>();
        boolean success = logDAO.add(log);
        result.put("success", success);
        
        out.print(gson.toJson(result));
        out.flush();
    }
}