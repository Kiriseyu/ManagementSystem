package com.hr.dao;

import com.hr.entity.Employee;
import com.hr.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    public boolean add(Employee employee) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO employee (emp_name, emp_gender, emp_birthdate, emp_phone, emp_email, emp_address, dept_id, hire_date, job_title) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, employee.getEmpName());
            pstmt.setString(2, employee.getEmpGender());
            pstmt.setDate(3, employee.getEmpBirthdate());
            pstmt.setString(4, employee.getEmpPhone());
            pstmt.setString(5, employee.getEmpEmail());
            pstmt.setString(6, employee.getEmpAddress());
            if (employee.getDeptId() != null) {
                pstmt.setInt(7, employee.getDeptId());
            } else {
                pstmt.setNull(7, java.sql.Types.INTEGER);
            }
            pstmt.setDate(8, employee.getHireDate());
            pstmt.setString(9, employee.getJobTitle());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public boolean update(Employee employee) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE employee SET emp_name = ?, emp_gender = ?, emp_birthdate = ?, emp_phone = ?, emp_email = ?, emp_address = ?, dept_id = ?, hire_date = ?, job_title = ? WHERE emp_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, employee.getEmpName());
            pstmt.setString(2, employee.getEmpGender());
            pstmt.setDate(3, employee.getEmpBirthdate());
            pstmt.setString(4, employee.getEmpPhone());
            pstmt.setString(5, employee.getEmpEmail());
            pstmt.setString(6, employee.getEmpAddress());
            if (employee.getDeptId() != null) {
                pstmt.setInt(7, employee.getDeptId());
            } else {
                pstmt.setNull(7, java.sql.Types.INTEGER);
            }
            pstmt.setDate(8, employee.getHireDate());
            pstmt.setString(9, employee.getJobTitle());
            pstmt.setInt(10, employee.getEmpId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public boolean delete(Integer empId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE employee SET is_deleted = 1 WHERE emp_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public Employee getById(Integer empId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM employee WHERE emp_id = ? AND is_deleted = 0";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractEmployee(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }

    public List<Employee> list() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Employee> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM employee WHERE is_deleted = 0 ORDER BY emp_id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(extractEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    public List<Employee> listByDeptId(Integer deptId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Employee> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM employee WHERE dept_id = ? AND is_deleted = 0 ORDER BY emp_id";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deptId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(extractEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    private Employee extractEmployee(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setEmpId(rs.getInt("emp_id"));
        emp.setEmpName(rs.getString("emp_name"));
        emp.setEmpGender(rs.getString("emp_gender"));
        emp.setEmpBirthdate(rs.getDate("emp_birthdate"));
        emp.setEmpPhone(rs.getString("emp_phone"));
        emp.setEmpEmail(rs.getString("emp_email"));
        emp.setEmpAddress(rs.getString("emp_address"));
        emp.setDeptId(rs.getObject("dept_id") != null ? rs.getInt("dept_id") : null);
        emp.setHireDate(rs.getDate("hire_date"));
        emp.setJobTitle(rs.getString("job_title"));
        emp.setCreateTime(rs.getTimestamp("create_time"));
        emp.setUpdateTime(rs.getTimestamp("update_time"));
        return emp;
    }
}
