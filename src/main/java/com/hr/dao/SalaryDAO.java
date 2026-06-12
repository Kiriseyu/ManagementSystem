package com.hr.dao;

import com.hr.entity.Salary;
import com.hr.util.DBUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaryDAO {
    public boolean add(Salary salary) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO salary (emp_id, sal_month, base_salary, bonus, allowance, deduction) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, salary.getEmpId());
            pstmt.setString(2, salary.getSalMonth());
            pstmt.setBigDecimal(3, salary.getBaseSalary() != null ? salary.getBaseSalary() : BigDecimal.ZERO);
            pstmt.setBigDecimal(4, salary.getBonus() != null ? salary.getBonus() : BigDecimal.ZERO);
            pstmt.setBigDecimal(5, salary.getAllowance() != null ? salary.getAllowance() : BigDecimal.ZERO);
            pstmt.setBigDecimal(6, salary.getDeduction() != null ? salary.getDeduction() : BigDecimal.ZERO);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public boolean update(Salary salary) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE salary SET emp_id = ?, sal_month = ?, base_salary = ?, bonus = ?, allowance = ?, deduction = ? WHERE sal_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, salary.getEmpId());
            pstmt.setString(2, salary.getSalMonth());
            pstmt.setBigDecimal(3, salary.getBaseSalary() != null ? salary.getBaseSalary() : BigDecimal.ZERO);
            pstmt.setBigDecimal(4, salary.getBonus() != null ? salary.getBonus() : BigDecimal.ZERO);
            pstmt.setBigDecimal(5, salary.getAllowance() != null ? salary.getAllowance() : BigDecimal.ZERO);
            pstmt.setBigDecimal(6, salary.getDeduction() != null ? salary.getDeduction() : BigDecimal.ZERO);
            pstmt.setInt(7, salary.getSalId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public boolean delete(Integer salId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE salary SET is_deleted = 1 WHERE sal_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, salId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public Salary getById(Integer salId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM salary WHERE sal_id = ? AND is_deleted = 0";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, salId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractSalary(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }

    public List<Salary> list() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Salary> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM salary WHERE is_deleted = 0 ORDER BY sal_month DESC, sal_id DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(extractSalary(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    public List<Salary> listByEmpId(Integer empId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Salary> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM salary WHERE emp_id = ? AND is_deleted = 0 ORDER BY sal_month DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(extractSalary(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    private Salary extractSalary(ResultSet rs) throws SQLException {
        Salary sal = new Salary();
        sal.setSalId(rs.getInt("sal_id"));
        sal.setEmpId(rs.getInt("emp_id"));
        sal.setSalMonth(rs.getString("sal_month"));
        sal.setBaseSalary(rs.getBigDecimal("base_salary"));
        sal.setBonus(rs.getBigDecimal("bonus"));
        sal.setAllowance(rs.getBigDecimal("allowance"));
        sal.setDeduction(rs.getBigDecimal("deduction"));
        sal.setTotalSalary(rs.getBigDecimal("total_salary"));
        sal.setCreateTime(rs.getTimestamp("create_time"));
        sal.setUpdateTime(rs.getTimestamp("update_time"));
        return sal;
    }
}
