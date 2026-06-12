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
    public boolean add(Department department) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO department (dept_name, dept_location, dept_phone) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, department.getDeptName());
            pstmt.setString(2, department.getDeptLocation());
            pstmt.setString(3, department.getDeptPhone());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public boolean update(Department department) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE department SET dept_name = ?, dept_location = ?, dept_phone = ? WHERE dept_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, department.getDeptName());
            pstmt.setString(2, department.getDeptLocation());
            pstmt.setString(3, department.getDeptPhone());
            pstmt.setInt(4, department.getDeptId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

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

    public List<Department> list() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Department> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM department WHERE is_deleted = 0 ORDER BY dept_id";
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

    private Department extractDepartment(ResultSet rs) throws SQLException {
        Department dept = new Department();
        dept.setDeptId(rs.getInt("dept_id"));
        dept.setDeptName(rs.getString("dept_name"));
        dept.setDeptLocation(rs.getString("dept_location"));
        dept.setDeptPhone(rs.getString("dept_phone"));
        dept.setCreateTime(rs.getTimestamp("create_time"));
        dept.setUpdateTime(rs.getTimestamp("update_time"));
        return dept;
    }
}
