package com.hr.servlet;

import com.hr.dao.PositionDAO;
import com.hr.entity.Position;
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
 * 职位管理Servlet
 */
@WebServlet("/api/position")
public class PositionServlet extends HttpServlet {
    private PositionDAO positionDAO = new PositionDAO();
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");
        if (idParam != null) {
            Position position = positionDAO.getById(Integer.parseInt(idParam));
            if (position != null) {
                out.print(gson.toJson(position));
            } else {
                resp.setStatus(404);
                out.print("{\"error\":\"Position not found\"}");
            }
        } else {
            List<Position> positions = positionDAO.list();
            out.print(gson.toJson(positions));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        String posName = req.getParameter("posName");
        String posLevelStr = req.getParameter("posLevel");
        
        // 检查名称是否已存在
        if (positionDAO.existsByName(posName)) {
            out.print("{\"success\":false,\"error\":\"职位名称已存在\"}");
            out.flush();
            return;
        }

        Position position = new Position();
        position.setPosName(posName);
        position.setPosLevel(posLevelStr != null ? Integer.parseInt(posLevelStr) : 1);
        position.setStatus(1);

        Map<String, Object> result = new HashMap<>();
        boolean success = positionDAO.add(position);
        result.put("success", success);
        result.put("message", success ? "职位添加成功" : "职位添加失败");
        
        out.print(gson.toJson(result));
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        Position position = new Position();
        position.setPosId(Integer.parseInt(req.getParameter("posId")));
        position.setPosName(req.getParameter("posName"));
        position.setPosLevel(Integer.parseInt(req.getParameter("posLevel")));
        position.setStatus(Integer.parseInt(req.getParameter("status")));

        Map<String, Object> result = new HashMap<>();
        boolean success = positionDAO.update(position);
        result.put("success", success);
        result.put("message", success ? "职位更新成功" : "职位更新失败");
        
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
            boolean success = positionDAO.delete(Integer.parseInt(idParam));
            result.put("success", success);
            result.put("message", success ? "职位删除成功" : "职位删除失败");
        } else {
            result.put("success", false);
            result.put("error", "缺少职位ID参数");
        }
        
        out.print(gson.toJson(result));
        out.flush();
    }
}