package com.hr.dao;

import com.hr.entity.Permission;
import com.hr.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限数据访问类
 */
public class PermissionDAO {

    /**
     * 获取所有权限列表
     */
    public List<Permission> list() {
        List<Permission> permissions = new ArrayList<>();
        String sql = "SELECT perm_id, perm_name, perm_code, perm_type, perm_url, " +
                     "parent_id, sort_order, status, create_time, update_time " +
                     "FROM sys_permission WHERE status = 1 ORDER BY parent_id, sort_order";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Permission perm = extractPermission(rs);
                permissions.add(perm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permissions;
    }

    /**
     * 获取菜单权限（树形结构）
     */
    public List<Permission> getMenuPermissions() {
        List<Permission> permissions = new ArrayList<>();
        String sql = "SELECT perm_id, perm_name, perm_code, perm_type, perm_url, " +
                     "parent_id, sort_order, status, create_time, update_time " +
                     "FROM sys_permission WHERE status = 1 AND perm_type = 'menu' ORDER BY sort_order";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Permission perm = extractPermission(rs);
                permissions.add(perm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permissions;
    }

    /**
     * 根据角色ID获取权限列表
     */
    public List<Permission> getPermissionsByRoleId(Integer roleId) {
        List<Permission> permissions = new ArrayList<>();
        String sql = "SELECT p.perm_id, p.perm_name, p.perm_code, p.perm_type, p.perm_url, " +
                     "p.parent_id, p.sort_order, p.status, p.create_time, p.update_time " +
                     "FROM sys_permission p " +
                     "INNER JOIN sys_role_permission rp ON p.perm_id = rp.perm_id " +
                     "WHERE rp.role_id = ? AND p.status = 1 " +
                     "ORDER BY p.parent_id, p.sort_order";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Permission perm = extractPermission(rs);
                    permissions.add(perm);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permissions;
    }

    /**
     * 根据用户ID获取权限代码列表
     */
    public List<String> getPermissionCodesByUserId(Integer userId) {
        List<String> codes = new ArrayList<>();
        String sql = "SELECT DISTINCT p.perm_code " +
                     "FROM sys_permission p " +
                     "INNER JOIN sys_role_permission rp ON p.perm_id = rp.perm_id " +
                     "INNER JOIN sys_user_role ur ON rp.role_id = ur.role_id " +
                     "WHERE ur.user_id = ? AND p.status = 1 AND p.perm_code IS NOT NULL";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    codes.add(rs.getString("perm_code"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return codes;
    }

    /**
     * 根据ID获取权限
     */
    public Permission getById(Integer permId) {
        String sql = "SELECT perm_id, perm_name, perm_code, perm_type, perm_url, " +
                     "parent_id, sort_order, status, create_time, update_time " +
                     "FROM sys_permission WHERE perm_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, permId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractPermission(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加权限
     */
    public boolean add(Permission permission) {
        String sql = "INSERT INTO sys_permission (perm_name, perm_code, perm_type, perm_url, parent_id, sort_order, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, permission.getPermName());
            pstmt.setString(2, permission.getPermCode());
            pstmt.setString(3, permission.getPermType());
            pstmt.setString(4, permission.getPermUrl());
            pstmt.setInt(5, permission.getParentId() != null ? permission.getParentId() : 0);
            pstmt.setInt(6, permission.getSortOrder() != null ? permission.getSortOrder() : 0);
            pstmt.setInt(7, permission.getStatus() != null ? permission.getStatus() : 1);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新权限
     */
    public boolean update(Permission permission) {
        String sql = "UPDATE sys_permission SET perm_name = ?, perm_code = ?, perm_type = ?, " +
                     "perm_url = ?, parent_id = ?, sort_order = ?, status = ? WHERE perm_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, permission.getPermName());
            pstmt.setString(2, permission.getPermCode());
            pstmt.setString(3, permission.getPermType());
            pstmt.setString(4, permission.getPermUrl());
            pstmt.setInt(5, permission.getParentId() != null ? permission.getParentId() : 0);
            pstmt.setInt(6, permission.getSortOrder() != null ? permission.getSortOrder() : 0);
            pstmt.setInt(7, permission.getStatus());
            pstmt.setInt(8, permission.getPermId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除权限（逻辑删除）
     */
    public boolean delete(Integer permId) {
        String sql = "UPDATE sys_permission SET status = 0 WHERE perm_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, permId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从ResultSet中提取权限对象
     */
    private Permission extractPermission(ResultSet rs) throws SQLException {
        Permission perm = new Permission();
        perm.setPermId(rs.getInt("perm_id"));
        perm.setPermName(rs.getString("perm_name"));
        perm.setPermCode(rs.getString("perm_code"));
        perm.setPermType(rs.getString("perm_type"));
        perm.setPermUrl(rs.getString("perm_url"));
        perm.setParentId(rs.getInt("parent_id"));
        perm.setSortOrder(rs.getInt("sort_order"));
        perm.setStatus(rs.getInt("status"));
        perm.setCreateTime(rs.getString("create_time"));
        perm.setUpdateTime(rs.getString("update_time"));
        return perm;
    }
}