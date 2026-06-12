package com.hr.servlet;

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");
        if (idParam != null) {
            Department dept = dao.getById(Integer.parseInt(idParam));
            if (dept != null) {
                out.print(deptToJson(dept));
            } else {
                resp.setStatus(404);
                out.print("{\"error\":\"Department not found\"}");
            }
        } else {
            List<Department> list = dao.list();
            out.print(deptListToJson(list));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        Department dept = new Department();
        dept.setDeptName(req.getParameter("deptName"));
        dept.setDeptLocation(req.getParameter("deptLocation"));
        dept.setDeptPhone(req.getParameter("deptPhone"));

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
        PrintWriter out = resp.getWriter();

        Department dept = new Department();
        dept.setDeptId(Integer.parseInt(req.getParameter("deptId")));
        dept.setDeptName(req.getParameter("deptName"));
        dept.setDeptLocation(req.getParameter("deptLocation"));
        dept.setDeptPhone(req.getParameter("deptPhone"));

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

    private String deptToJson(Department dept) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"deptId\":").append(dept.getDeptId()).append(",");
        sb.append("\"deptName\":\"").append(escapeJson(dept.getDeptName())).append("\",");
        sb.append("\"deptLocation\":\"").append(escapeJson(dept.getDeptLocation())).append("\",");
        sb.append("\"deptPhone\":\"").append(escapeJson(dept.getDeptPhone())).append("\"");
        sb.append("}");
        return sb.toString();
    }

    private String deptListToJson(List<Department> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(deptToJson(list.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
