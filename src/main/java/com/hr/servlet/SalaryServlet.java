package com.hr.servlet;

import com.hr.dao.SalaryDAO;
import com.hr.entity.Salary;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/api/salary")
public class SalaryServlet extends HttpServlet {
    private SalaryDAO dao = new SalaryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");
        String empIdParam = req.getParameter("empId");

        if (idParam != null) {
            Salary sal = dao.getById(Integer.parseInt(idParam));
            if (sal != null) {
                out.print(salToJson(sal));
            } else {
                resp.setStatus(404);
                out.print("{\"error\":\"Salary not found\"}");
            }
        } else if (empIdParam != null) {
            List<Salary> list = dao.listByEmpId(Integer.parseInt(empIdParam));
            out.print(salListToJson(list));
        } else {
            List<Salary> list = dao.list();
            out.print(salListToJson(list));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        Salary sal = new Salary();
        sal.setEmpId(Integer.parseInt(req.getParameter("empId")));
        sal.setSalMonth(req.getParameter("salMonth"));
        String baseSalary = req.getParameter("baseSalary");
        if (baseSalary != null && !baseSalary.isEmpty()) {
            sal.setBaseSalary(new BigDecimal(baseSalary));
        }
        String bonus = req.getParameter("bonus");
        if (bonus != null && !bonus.isEmpty()) {
            sal.setBonus(new BigDecimal(bonus));
        }
        String allowance = req.getParameter("allowance");
        if (allowance != null && !allowance.isEmpty()) {
            sal.setAllowance(new BigDecimal(allowance));
        }
        String deduction = req.getParameter("deduction");
        if (deduction != null && !deduction.isEmpty()) {
            sal.setDeduction(new BigDecimal(deduction));
        }

        boolean success = dao.add(sal);
        if (success) {
            resp.setStatus(201);
            out.print("{\"success\":true}");
        } else {
            resp.setStatus(500);
            out.print("{\"success\":false,\"error\":\"Failed to add salary\"}");
        }
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        Salary sal = new Salary();
        sal.setSalId(Integer.parseInt(req.getParameter("salId")));
        sal.setEmpId(Integer.parseInt(req.getParameter("empId")));
        sal.setSalMonth(req.getParameter("salMonth"));
        String baseSalary = req.getParameter("baseSalary");
        if (baseSalary != null && !baseSalary.isEmpty()) {
            sal.setBaseSalary(new BigDecimal(baseSalary));
        }
        String bonus = req.getParameter("bonus");
        if (bonus != null && !bonus.isEmpty()) {
            sal.setBonus(new BigDecimal(bonus));
        }
        String allowance = req.getParameter("allowance");
        if (allowance != null && !allowance.isEmpty()) {
            sal.setAllowance(new BigDecimal(allowance));
        }
        String deduction = req.getParameter("deduction");
        if (deduction != null && !deduction.isEmpty()) {
            sal.setDeduction(new BigDecimal(deduction));
        }

        boolean success = dao.update(sal);
        if (success) {
            out.print("{\"success\":true}");
        } else {
            resp.setStatus(500);
            out.print("{\"success\":false,\"error\":\"Failed to update salary\"}");
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
                out.print("{\"success\":false,\"error\":\"Failed to delete salary\"}");
            }
        } else {
            resp.setStatus(400);
            out.print("{\"error\":\"Missing id parameter\"}");
        }
        out.flush();
    }

    private String salToJson(Salary sal) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"salId\":").append(sal.getSalId()).append(",");
        sb.append("\"empId\":").append(sal.getEmpId()).append(",");
        sb.append("\"salMonth\":\"").append(escapeJson(sal.getSalMonth())).append("\",");
        sb.append("\"baseSalary\":").append(sal.getBaseSalary()).append(",");
        sb.append("\"bonus\":").append(sal.getBonus()).append(",");
        sb.append("\"allowance\":").append(sal.getAllowance()).append(",");
        sb.append("\"deduction\":").append(sal.getDeduction()).append(",");
        sb.append("\"totalSalary\":").append(sal.getTotalSalary());
        sb.append("}");
        return sb.toString();
    }

    private String salListToJson(List<Salary> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(salToJson(list.get(i)));
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
