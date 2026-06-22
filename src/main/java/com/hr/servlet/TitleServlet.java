package com.hr.servlet;

import com.hr.dao.TitleDAO;
import com.hr.entity.Title;
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
 * 职称管理Servlet
 */
@WebServlet("/api/title")
public class TitleServlet extends HttpServlet {
    private TitleDAO titleDAO = new TitleDAO();
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");
        if (idParam != null) {
            Title title = titleDAO.getById(Integer.parseInt(idParam));
            if (title != null) {
                out.print(gson.toJson(title));
            } else {
                resp.setStatus(404);
                out.print("{\"error\":\"Title not found\"}");
            }
        } else {
            List<Title> titles = titleDAO.list();
            out.print(gson.toJson(titles));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        String titleName = req.getParameter("titleName");
        
        // 检查名称是否已存在
        if (titleDAO.existsByName(titleName)) {
            out.print("{\"success\":false,\"error\":\"职称名称已存在\"}");
            out.flush();
            return;
        }

        Title title = new Title();
        title.setTitleName(titleName);
        title.setTitleLevel(req.getParameter("titleLevel"));
        title.setStatus(1);

        Map<String, Object> result = new HashMap<>();
        boolean success = titleDAO.add(title);
        result.put("success", success);
        result.put("message", success ? "职称添加成功" : "职称添加失败");
        
        out.print(gson.toJson(result));
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        Title title = new Title();
        title.setTitleId(Integer.parseInt(req.getParameter("titleId")));
        title.setTitleName(req.getParameter("titleName"));
        title.setTitleLevel(req.getParameter("titleLevel"));
        title.setStatus(Integer.parseInt(req.getParameter("status")));

        Map<String, Object> result = new HashMap<>();
        boolean success = titleDAO.update(title);
        result.put("success", success);
        result.put("message", success ? "职称更新成功" : "职称更新失败");
        
        out.print(gson.toJson(result));
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");
        Map<String, Object> result = new HashMap<>();

        if (idParam != null) {
            boolean success = titleDAO.delete(Integer.parseInt(idParam));
            result.put("success", success);
            result.put("message", success ? "职称删除成功" : "职称删除失败");
        } else {
            result.put("success", false);
            result.put("error", "缺少职称ID参数");
        }
        
        out.print(gson.toJson(result));
        out.flush();
    }
}