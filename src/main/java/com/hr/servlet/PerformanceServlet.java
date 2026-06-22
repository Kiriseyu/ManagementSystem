package com.hr.servlet;

import com.hr.dao.PerformanceDAO;
import com.hr.entity.Performance;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/performance")
public class PerformanceServlet extends HttpServlet {
    private PerformanceDAO dao = new PerformanceDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");

        if (idParam != null) {
            Performance perf = dao.getById(Integer.parseInt(idParam));
            if (perf != null) {
                out.print(gson.toJson(perf));
            } else {
                resp.setStatus(404);
                out.print("{\"error\":\"Performance not found\"}");
            }
        } else {
            List<Performance> list = dao.list();
            out.print(gson.toJson(list));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        int empId = Integer.parseInt(req.getParameter("empId"));
        String period = req.getParameter("period");
        double score = Double.parseDouble(req.getParameter("score"));
        String performanceGrade = req.getParameter("performanceGrade");
        String evaluator = req.getParameter("evaluator");
        String remark = req.getParameter("remark");

        Performance perf = new Performance();
        perf.setEmpId(empId);
        perf.setPeriod(period);
        perf.setScore(score);
        perf.setPerformanceGrade(performanceGrade);
        perf.setEvaluator(evaluator);
        perf.setRemark(remark);

        int result = dao.insert(perf);
        if (result > 0) {
            out.print("{\"success\":true,\"message\":\"添加成功\"}");
        } else {
            resp.setStatus(500);
            out.print("{\"success\":false,\"message\":\"添加失败\"}");
        }
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        int perfId = Integer.parseInt(req.getParameter("perfId"));
        int empId = Integer.parseInt(req.getParameter("empId"));
        String period = req.getParameter("period");
        double score = Double.parseDouble(req.getParameter("score"));
        String performanceGrade = req.getParameter("performanceGrade");
        String evaluator = req.getParameter("evaluator");
        String remark = req.getParameter("remark");

        Performance perf = new Performance();
        perf.setPerfId(perfId);
        perf.setEmpId(empId);
        perf.setPeriod(period);
        perf.setScore(score);
        perf.setPerformanceGrade(performanceGrade);
        perf.setEvaluator(evaluator);
        perf.setRemark(remark);

        int result = dao.update(perf);
        if (result > 0) {
            out.print("{\"success\":true,\"message\":\"更新成功\"}");
        } else {
            resp.setStatus(500);
            out.print("{\"success\":false,\"message\":\"更新失败\"}");
        }
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.setStatus(400);
            out.print("{\"success\":false,\"message\":\"缺少ID参数\"}");
            out.flush();
            return;
        }

        int result = dao.delete(Integer.parseInt(idParam));
        if (result > 0) {
            out.print("{\"success\":true,\"message\":\"删除成功\"}");
        } else {
            resp.setStatus(500);
            out.print("{\"success\":false,\"message\":\"删除失败\"}");
        }
        out.flush();
    }
}