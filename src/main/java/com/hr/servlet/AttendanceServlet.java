package com.hr.servlet;

import com.hr.dao.AttendanceDAO;
import com.hr.entity.Attendance;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/api/attendance")
public class AttendanceServlet extends HttpServlet {
    private AttendanceDAO dao = new AttendanceDAO();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");
        String empIdParam = req.getParameter("empId");

        if (idParam != null) {
            Attendance att = dao.getById(Integer.parseInt(idParam));
            if (att != null) {
                out.print(attToJson(att));
            } else {
                resp.setStatus(404);
                out.print("{\"error\":\"Attendance not found\"}");
            }
        } else if (empIdParam != null) {
            List<Attendance> list = dao.listByEmpId(Integer.parseInt(empIdParam));
            out.print(attListToJson(list));
        } else {
            List<Attendance> list = dao.list();
            out.print(attListToJson(list));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        Attendance att = new Attendance();
        att.setEmpId(Integer.parseInt(req.getParameter("empId")));
        String attDate = req.getParameter("attDate");
        if (attDate != null && !attDate.isEmpty()) {
            att.setAttDate(Date.valueOf(attDate));
        }
        String checkIn = req.getParameter("checkInTime");
        if (checkIn != null && !checkIn.isEmpty()) {
            try {
                att.setCheckInTime(new Timestamp(sdf.parse(checkIn).getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String checkOut = req.getParameter("checkOutTime");
        if (checkOut != null && !checkOut.isEmpty()) {
            try {
                att.setCheckOutTime(new Timestamp(sdf.parse(checkOut).getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        att.setStatus(req.getParameter("status"));
        att.setRemark(req.getParameter("remark"));

        boolean success = dao.add(att);
        if (success) {
            resp.setStatus(201);
            out.print("{\"success\":true}");
        } else {
            resp.setStatus(500);
            out.print("{\"success\":false,\"error\":\"Failed to add attendance\"}");
        }
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        Attendance att = new Attendance();
        att.setAttId(Integer.parseInt(req.getParameter("attId")));
        att.setEmpId(Integer.parseInt(req.getParameter("empId")));
        String attDate = req.getParameter("attDate");
        if (attDate != null && !attDate.isEmpty()) {
            att.setAttDate(Date.valueOf(attDate));
        }
        String checkIn = req.getParameter("checkInTime");
        if (checkIn != null && !checkIn.isEmpty()) {
            try {
                att.setCheckInTime(new Timestamp(sdf.parse(checkIn).getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String checkOut = req.getParameter("checkOutTime");
        if (checkOut != null && !checkOut.isEmpty()) {
            try {
                att.setCheckOutTime(new Timestamp(sdf.parse(checkOut).getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        att.setStatus(req.getParameter("status"));
        att.setRemark(req.getParameter("remark"));

        boolean success = dao.update(att);
        if (success) {
            out.print("{\"success\":true}");
        } else {
            resp.setStatus(500);
            out.print("{\"success\":false,\"error\":\"Failed to update attendance\"}");
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
                out.print("{\"success\":false,\"error\":\"Failed to delete attendance\"}");
            }
        } else {
            resp.setStatus(400);
            out.print("{\"error\":\"Missing id parameter\"}");
        }
        out.flush();
    }

    private String attToJson(Attendance att) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"attId\":").append(att.getAttId()).append(",");
        sb.append("\"empId\":").append(att.getEmpId()).append(",");
        sb.append("\"attDate\":\"").append(att.getAttDate() != null ? att.getAttDate().toString() : "").append("\",");
        sb.append("\"checkInTime\":\"").append(att.getCheckInTime() != null ? sdf.format(att.getCheckInTime()) : "").append("\",");
        sb.append("\"checkOutTime\":\"").append(att.getCheckOutTime() != null ? sdf.format(att.getCheckOutTime()) : "").append("\",");
        sb.append("\"status\":\"").append(escapeJson(att.getStatus())).append("\",");
        sb.append("\"remark\":\"").append(escapeJson(att.getRemark())).append("\"");
        sb.append("}");
        return sb.toString();
    }

    private String attListToJson(List<Attendance> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(attToJson(list.get(i)));
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
