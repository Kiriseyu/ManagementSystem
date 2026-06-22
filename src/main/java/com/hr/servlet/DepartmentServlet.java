package com.hr.servlet;

import com.google.gson.Gson;
import com.hr.dao.DepartmentDAO;
import com.hr.entity.Department;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/department")
public class DepartmentServlet extends HttpServlet {
    private DepartmentDAO dao = new DepartmentDAO();
    private static final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String action = req.getParameter("action");
        String idParam = req.getParameter("id");

        // 获取树形结构
        if ("tree".equals(action)) {
            List<Department> tree = dao.getTree();
            out.print(gson.toJson(tree));
            out.flush();
            return;
        }

        // 获取所有部门列表
        if ("list".equals(action)) {
            List<Department> list = dao.list();
            out.print(gson.toJson(list));
            out.flush();
            return;
        }

        // 根据父ID获取子部门
        if ("children".equals(action) && idParam != null) {
            List<Department> list = dao.listByParentId(Integer.parseInt(idParam));
            out.print(gson.toJson(list));
            out.flush();
            return;
        }

        // 根据ID获取单个部门
        if (idParam != null) {
            Department dept = dao.getById(Integer.parseInt(idParam));
            if (dept != null) {
                out.print(gson.toJson(dept));
            } else {
                resp.setStatus(404);
                out.print("{\"error\":\"Department not found\"}");
            }
        } else {
            // 默认返回扁平化列表（保持向后兼容）
            List<Department> list = dao.list();
            out.print(gson.toJson(list));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        Department dept = new Department();
        dept.setDeptName(req.getParameter("deptName"));
        dept.setDeptLocation(req.getParameter("deptLocation"));
        dept.setDeptPhone(req.getParameter("deptPhone"));

        String parentIdStr = req.getParameter("parentId");
        if (parentIdStr != null && !parentIdStr.isEmpty()) {
            dept.setParentId(Integer.parseInt(parentIdStr));
        } else {
            dept.setParentId(0);  // 默认顶级部门
        }

        boolean success = dao.add(dept);
        if (success) {
            resp.setStatus(201);
            out.print("{\"success\":true}");
        } else {
            resp.setStatus(500);
            out.print("{\"success\":false,\"error\":\"Failed to add department\"}");
        }
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        Department dept = new Department();
        dept.setDeptId(Integer.parseInt(req.getParameter("deptId")));
        dept.setDeptName(req.getParameter("deptName"));
        dept.setDeptLocation(req.getParameter("deptLocation"));
        dept.setDeptPhone(req.getParameter("deptPhone"));

        String parentIdStr = req.getParameter("parentId");
        if (parentIdStr != null && !parentIdStr.isEmpty()) {
            dept.setParentId(Integer.parseInt(parentIdStr));
        } else {
            dept.setParentId(0);
        }

        boolean success = dao.update(dept);
        if (success) {
            out.print("{\"success\":true}");
        } else {
            resp.setStatus(500);
            out.print("{\"success\":false,\"error\":\"Failed to update department\"}");
        }
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");
        if (idParam != null) {
            boolean success = dao.delete(Integer.parseInt(idParam));
            if (success) {
                out.print("{\"success\":true}");
            } else {
                resp.setStatus(500);
                out.print("{\"success\":false,\"error\":\"Failed to delete department\"}");
            }
        } else {
            resp.setStatus(400);
            out.print("{\"error\":\"Missing id parameter\"}");
        }
        out.flush();
    }
}
