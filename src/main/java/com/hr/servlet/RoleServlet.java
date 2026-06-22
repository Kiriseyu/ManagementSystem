package com.hr.servlet;

import com.hr.dao.PermissionDAO;
import com.hr.dao.RoleDAO;
import com.hr.entity.Permission;
import com.hr.entity.Role;

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
 * 角色管理Servlet
 */
@WebServlet("/api/role")
public class RoleServlet extends HttpServlet {
    private RoleDAO roleDAO = new RoleDAO();
    private PermissionDAO permissionDAO = new PermissionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String action = req.getParameter("action");
        if ("permissions".equals(action)) {
            // 获取所有权限
            List<Permission> permissions = permissionDAO.list();
            out.print(permissionListToJson(permissions));
        } else if ("rolePermissions".equals(action)) {
            // 获取角色已分配的权限
            String roleIdStr = req.getParameter("roleId");
            if (roleIdStr != null) {
                Integer roleId = Integer.parseInt(roleIdStr);
                List<Integer> permIds = roleDAO.getRolePermissionIds(roleId);
                out.print(permIdListToJson(permIds));
            }
        } else {
            // 获取所有角色
            List<Role> roles = roleDAO.list();
            out.print(roleListToJson(roles));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        String action = req.getParameter("action");
        Map<String, Object> result = new HashMap<>();

        if ("assignPermissions".equals(action)) {
            // 分配权限
            Integer roleId = Integer.parseInt(req.getParameter("roleId"));
            String permIdsStr = req.getParameter("permIds");
            List<Integer> permIds = parsePermIds(permIdsStr);

            boolean success = roleDAO.assignPermissions(roleId, permIds);
            if (success) {
                result.put("success", true);
                result.put("message", "权限分配成功");
            } else {
                result.put("success", false);
                result.put("error", "权限分配失败");
            }
        } else {
            // 添加角色
            Role role = new Role();
            role.setRoleName(req.getParameter("roleName"));
            role.setRoleDesc(req.getParameter("roleDesc"));
            role.setStatus(1);

            boolean success = roleDAO.add(role);
            if (success) {
                result.put("success", true);
                result.put("message", "角色添加成功");
            } else {
                result.put("success", false);
                result.put("error", "角色添加失败");
            }
        }
        out.print(mapToJson(result));
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        Role role = new Role();
        role.setRoleId(Integer.parseInt(req.getParameter("roleId")));
        role.setRoleName(req.getParameter("roleName"));
        role.setRoleDesc(req.getParameter("roleDesc"));
        role.setStatus(Integer.parseInt(req.getParameter("status")));

        Map<String, Object> result = new HashMap<>();
        boolean success = roleDAO.update(role);
        if (success) {
            result.put("success", true);
            result.put("message", "角色更新成功");
        } else {
            result.put("success", false);
            result.put("error", "角色更新失败");
        }
        out.print(mapToJson(result));
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String idParam = req.getParameter("id");
        Map<String, Object> result = new HashMap<>();

        if (idParam != null) {
            boolean success = roleDAO.delete(Integer.parseInt(idParam));
            if (success) {
                result.put("success", true);
                result.put("message", "角色删除成功");
            } else {
                result.put("success", false);
                result.put("error", "角色删除失败");
            }
        } else {
            result.put("success", false);
            result.put("error", "缺少角色ID参数");
        }
        out.print(mapToJson(result));
        out.flush();
    }

    private String roleListToJson(List<Role> roles) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < roles.size(); i++) {
            if (i > 0) sb.append(",");
            Role role = roles.get(i);
            sb.append("{");
            sb.append("\"roleId\":").append(role.getRoleId()).append(",");
            sb.append("\"roleName\":\"").append(escapeJson(role.getRoleName())).append("\",");
            sb.append("\"roleDesc\":\"").append(escapeJson(role.getRoleDesc() != null ? role.getRoleDesc() : "")).append("\",");
            sb.append("\"status\":").append(role.getStatus());
            sb.append("}");
        }
        sb.append("]");
        return sb.toString();
    }

    private String permissionListToJson(List<Permission> permissions) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < permissions.size(); i++) {
            if (i > 0) sb.append(",");
            Permission perm = permissions.get(i);
            sb.append("{");
            sb.append("\"permId\":").append(perm.getPermId()).append(",");
            sb.append("\"permName\":\"").append(escapeJson(perm.getPermName())).append("\",");
            sb.append("\"permCode\":\"").append(escapeJson(perm.getPermCode() != null ? perm.getPermCode() : "")).append("\",");
            sb.append("\"permType\":\"").append(escapeJson(perm.getPermType() != null ? perm.getPermType() : "")).append("\",");
            sb.append("\"parentId\":").append(perm.getParentId());
            sb.append("}");
        }
        sb.append("]");
        return sb.toString();
    }

    private String permIdListToJson(List<Integer> permIds) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < permIds.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(permIds.get(i));
        }
        sb.append("]");
        return sb.toString();
    }

    private List<Integer> parsePermIds(String permIdsStr) {
        java.util.ArrayList<Integer> permIds = new java.util.ArrayList<>();
        if (permIdsStr != null && !permIdsStr.isEmpty()) {
            String[] ids = permIdsStr.split(",");
            for (String id : ids) {
                if (!id.trim().isEmpty()) {
                    permIds.add(Integer.parseInt(id.trim()));
                }
            }
        }
        return permIds;
    }

    private String mapToJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            sb.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof Boolean) {
                sb.append(entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                sb.append(entry.getValue());
            } else {
                sb.append("\"").append(escapeJson(entry.getValue().toString())).append("\"");
            }
        }
        sb.append("}");
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