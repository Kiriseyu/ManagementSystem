package com.hr.dao;

import com.hr.entity.Department;
import com.hr.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    /**
     * 添加部门
     */
    public boolean add(Department department) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            // 如果没有指定父部门，则默认为0（顶级部门）
            Integer parentId = department.getParentId() != null ? department.getParentId() : 0;
            String sql = "INSERT INTO department (dept_name, dept_location, dept_phone, parent_id) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, department.getDeptName());
            pstmt.setString(2, department.getDeptLocation());
            pstmt.setString(3, department.getDeptPhone());
            pstmt.setInt(4, parentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 更新部门
     */
    public boolean update(Department department) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            Integer parentId = department.getParentId() != null ? department.getParentId() : 0;
            String sql = "UPDATE department SET dept_name = ?, dept_location = ?, dept_phone = ?, parent_id = ? WHERE dept_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, department.getDeptName());
            pstmt.setString(2, department.getDeptLocation());
            pstmt.setString(3, department.getDeptPhone());
            pstmt.setInt(4, parentId);
            pstmt.setInt(5, department.getDeptId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 删除部门（逻辑删除）
     */
    public boolean delete(Integer deptId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE department SET is_deleted = 1 WHERE dept_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deptId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    /**
     * 根据ID获取部门
     */
    public Department getById(Integer deptId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM department WHERE dept_id = ? AND is_deleted = 0";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deptId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractDepartment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }

    /**
     * 获取所有部门列表
     */
    public List<Department> list() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Department> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM department WHERE is_deleted = 0 ORDER BY parent_id, dept_id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(extractDepartment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    /**
     * 获取顶级部门列表（parent_id = 0）
     */
    public List<Department> listTopLevel() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Department> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM department WHERE is_deleted = 0 AND (parent_id = 0 OR parent_id IS NULL) ORDER BY dept_id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(extractDepartment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    /**
     * 根据父ID获取子部门列表
     */
    public List<Department> listByParentId(Integer parentId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Department> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM department WHERE is_deleted = 0 AND parent_id = ? ORDER BY dept_id";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, parentId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(extractDepartment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    /**
     * 获取部门树形结构数据
     * 返回包含children属性的树形结构列表
     */
    public List<Department> getTree() {
        List<Department> allDepts = list();
        List<Department> tree = new ArrayList<>();

        // 构建树形结构
        for (Department dept : allDepts) {
            Integer parentId = dept.getParentId();
            if (parentId == null || parentId == 0) {
                // 顶级部门
                tree.add(dept);
            }
        }

        // 为每个顶级部门添加子部门
        for (Department dept : tree) {
            addChildren(dept, allDepts);
        }

        return tree;
    }

    /**
     * 递归为部门添加子部门
     */
    private void addChildren(Department parent, List<Department> allDepts) {
        for (Department dept : allDepts) {
            if (parent.getDeptId().equals(dept.getParentId())) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(dept);
                // 递归添加子部门的子部门
                addChildren(dept, allDepts);
            }
        }
    }

    private Department extractDepartment(ResultSet rs) throws SQLException {
        Department dept = new Department();
        dept.setDeptId(rs.getInt("dept_id"));
        dept.setDeptName(rs.getString("dept_name"));
        dept.setDeptLocation(rs.getString("dept_location"));
        dept.setDeptPhone(rs.getString("dept_phone"));
        // 尝试获取parent_id，如果不存在则默认为0
        try {
            dept.setParentId(rs.getInt("parent_id"));
        } catch (SQLException e) {
            dept.setParentId(0);
        }
        dept.setCreateTime(rs.getTimestamp("create_time"));
        dept.setUpdateTime(rs.getTimestamp("update_time"));
        return dept;
    }
}
