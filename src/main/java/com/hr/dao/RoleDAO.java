package com.hr.dao;

import com.hr.entity.Role;
import com.hr.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色数据访问类
 */
public class RoleDAO {

    /**
     * 获取所有角色列表
     */
    public List<Role> list() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT role_id, role_name, role_desc, status, create_time, update_time " +
                     "FROM sys_role WHERE status = 1 ORDER BY role_id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Role role = extractRole(rs);
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    /**
     * 根据ID获取角色
     */
    public Role getById(Integer roleId) {
        String sql = "SELECT role_id, role_name, role_desc, status, create_time, update_time " +
                     "FROM sys_role WHERE role_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractRole(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加角色
     */
    public boolean add(Role role) {
        String sql = "INSERT INTO sys_role (role_name, role_desc, status) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, role.getRoleName());
            pstmt.setString(2, role.getRoleDesc());
            pstmt.setInt(3, role.getStatus() != null ? role.getStatus() : 1);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新角色
     */
    public boolean update(Role role) {
        String sql = "UPDATE sys_role SET role_name = ?, role_desc = ?, status = ? WHERE role_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, role.getRoleName());
            pstmt.setString(2, role.getRoleDesc());
            pstmt.setInt(3, role.getStatus());
            pstmt.setInt(4, role.getRoleId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除角色（逻辑删除）
     */
    public boolean delete(Integer roleId) {
        String sql = "UPDATE sys_role SET status = 0 WHERE role_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roleId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 为角色分配权限
     */
    public boolean assignPermissions(Integer roleId, List<Integer> permIds) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // 删除原有权限
            String deleteSql = "DELETE FROM sys_role_permission WHERE role_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.setInt(1, roleId);
                pstmt.executeUpdate();
            }

            // 添加新权限
            if (permIds != null && !permIds.isEmpty()) {
                String insertSql = "INSERT INTO sys_role_permission (role_id, perm_id) VALUES (?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    for (Integer permId : permIds) {
                        pstmt.setInt(1, roleId);
                        pstmt.setInt(2, permId);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            DBUtil.close(conn, null, null);
        }
        return false;
    }

    /**
     * 获取角色已分配的权限ID列表
     */
    public List<Integer> getRolePermissionIds(Integer roleId) {
        List<Integer> permIds = new ArrayList<>();
        String sql = "SELECT perm_id FROM sys_role_permission WHERE role_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    permIds.add(rs.getInt("perm_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permIds;
    }

    /**
     * 从ResultSet中提取角色对象
     */
    private Role extractRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setRoleId(rs.getInt("role_id"));
        role.setRoleName(rs.getString("role_name"));
        role.setRoleDesc(rs.getString("role_desc"));
        role.setStatus(rs.getInt("status"));
        role.setCreateTime(rs.getString("create_time"));
        role.setUpdateTime(rs.getString("update_time"));
        return role;
    }
}