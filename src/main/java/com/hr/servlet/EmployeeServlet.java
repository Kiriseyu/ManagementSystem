package com.hr.servlet;

import com.hr.dao.EmployeeDAO;
import com.hr.entity.Employee;
import com.hr.util.ExcelUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

@WebServlet("/api/employee")
@MultipartConfig  // 支持文件上传
public class EmployeeServlet extends HttpServlet {
    private EmployeeDAO dao = new EmployeeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // 导出Excel
        if ("export".equals(action)) {
            List<Employee> list = dao.list();
            // 设置响应头，提示浏览器下载文件
            resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            resp.setHeader("Content-Disposition", "attachment;filename=employees.xlsx");

            try {
                ExcelUtil.exportEmployees(list, resp.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                resp.setStatus(500);
                resp.getWriter().print("{\"error\":\"导出失败\"}");
            }
            return;
        }

        // 原有的doGet逻辑 - 查询员工
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");
        String deptIdParam = req.getParameter("deptId");

        if (idParam != null) {
            Employee emp = dao.getById(Integer.parseInt(idParam));
            if (emp != null) {
                out.print(empToJson(emp));
            } else {
                resp.setStatus(404);
                out.print("{\"error\":\"Employee not found\"}");
            }
        } else if (deptIdParam != null) {
            List<Employee> list = dao.listByDeptId(Integer.parseInt(deptIdParam));
            out.print(empListToJson(list));
        } else {
            List<Employee> list = dao.list();
            out.print(empListToJson(list));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // 导入Excel
        if ("import".equals(action)) {
            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();

            try {
                // 获取上传的文件
                Part filePart = req.getPart("file");
                if (filePart == null) {
                    out.print("{\"success\":false,\"message\":\"请选择要上传的文件\"}");
                    return;
                }

                String fileName = filePart.getSubmittedFileName();
                if (fileName == null || !fileName.matches(".*\\.(xlsx|xls)$")) {
                    out.print("{\"success\":false,\"message\":\"请上传Excel文件(.xlsx或.xls)\"}");
                    return;
                }

                // 解析Excel
                try (InputStream inputStream = filePart.getInputStream()) {
                    List<Employee> employees = ExcelUtil.importEmployees(inputStream);

                    int successCount = 0;
                    for (Employee emp : employees) {
                        // 跳过ID，让数据库自动生成
                        emp.setEmpId(null);
                        if (dao.add(emp)) {
                            successCount++;
                        }
                    }

                    out.print("{\"success\":true,\"message\":\"成功导入\" + successCount + \"条记录，总共\" + employees.size() + \"条\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.print("{\"success\":false,\"message\":\"导入失败:\" + e.getMessage() + \"\"}");
            }
            return;
        }

        // 原有的doPost逻辑 - 添加员工
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        Employee emp = new Employee();
        emp.setEmpName(req.getParameter("empName"));
        emp.setEmpGender(req.getParameter("empGender"));
        String birthdate = req.getParameter("empBirthdate");
        if (birthdate != null && !birthdate.isEmpty()) {
            emp.setEmpBirthdate(Date.valueOf(birthdate));
        }
        emp.setEmpPhone(req.getParameter("empPhone"));
        emp.setEmpEmail(req.getParameter("empEmail"));
        emp.setEmpAddress(req.getParameter("empAddress"));
        String deptId = req.getParameter("deptId");
        if (deptId != null && !deptId.isEmpty()) {
            emp.setDeptId(Integer.parseInt(deptId));
        }
        String hireDate = req.getParameter("hireDate");
        if (hireDate != null && !hireDate.isEmpty()) {
            emp.setHireDate(Date.valueOf(hireDate));
        }
        emp.setJobTitle(req.getParameter("jobTitle"));

        boolean success = dao.add(emp);
        if (success) {
            resp.setStatus(201);
            out.print("{\"success\":true}");
        } else {
            resp.setStatus(500);
            out.print("{\"success\":false,\"error\":\"Failed to add employee\"}");
        }
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        Employee emp = new Employee();
        emp.setEmpId(Integer.parseInt(req.getParameter("empId")));
        emp.setEmpName(req.getParameter("empName"));
        emp.setEmpGender(req.getParameter("empGender"));
        String birthdate = req.getParameter("empBirthdate");
        if (birthdate != null && !birthdate.isEmpty()) {
            emp.setEmpBirthdate(Date.valueOf(birthdate));
        }
        emp.setEmpPhone(req.getParameter("empPhone"));
        emp.setEmpEmail(req.getParameter("empEmail"));
        emp.setEmpAddress(req.getParameter("empAddress"));
        String deptId = req.getParameter("deptId");
        if (deptId != null && !deptId.isEmpty()) {
            emp.setDeptId(Integer.parseInt(deptId));
        }
        String hireDate = req.getParameter("hireDate");
        if (hireDate != null && !hireDate.isEmpty()) {
            emp.setHireDate(Date.valueOf(hireDate));
        }
        emp.setJobTitle(req.getParameter("jobTitle"));

        boolean success = dao.update(emp);
        if (success) {
            out.print("{\"success\":true}");
        } else {
            resp.setStatus(500);
            out.print("{\"success\":false,\"error\":\"Failed to update employee\"}");
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
                out.print("{\"success\":false,\"error\":\"Failed to delete employee\"}");
            }
        } else {
            resp.setStatus(400);
            out.print("{\"error\":\"Missing id parameter\"}");
        }
        out.flush();
    }

    private String empToJson(Employee emp) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"empId\":").append(emp.getEmpId()).append(",");
        sb.append("\"empName\":\"").append(escapeJson(emp.getEmpName())).append("\",");
        sb.append("\"empGender\":\"").append(escapeJson(emp.getEmpGender())).append("\",");
        sb.append("\"empBirthdate\":\"").append(emp.getEmpBirthdate() != null ? emp.getEmpBirthdate().toString() : "").append("\",");
        sb.append("\"empPhone\":\"").append(escapeJson(emp.getEmpPhone())).append("\",");
        sb.append("\"empEmail\":\"").append(escapeJson(emp.getEmpEmail())).append("\",");
        sb.append("\"empAddress\":\"").append(escapeJson(emp.getEmpAddress())).append("\",");
        sb.append("\"deptId\":").append(emp.getDeptId() != null ? emp.getDeptId() : "null").append(",");
        sb.append("\"hireDate\":\"").append(emp.getHireDate() != null ? emp.getHireDate().toString() : "").append("\",");
        sb.append("\"jobTitle\":\"").append(escapeJson(emp.getJobTitle())).append("\"");
        sb.append("}");
        return sb.toString();
    }

    private String empListToJson(List<Employee> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(empToJson(list.get(i)));
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
