package com.hr.dao;

import com.hr.entity.Attendance;
import com.hr.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {
    public boolean add(Attendance attendance) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO attendance (emp_id, att_date, check_in_time, check_out_time, status, remark) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, attendance.getEmpId());
            pstmt.setDate(2, attendance.getAttDate());
            pstmt.setTimestamp(3, attendance.getCheckInTime());
            pstmt.setTimestamp(4, attendance.getCheckOutTime());
            pstmt.setString(5, attendance.getStatus());
            pstmt.setString(6, attendance.getRemark());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public boolean update(Attendance attendance) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE attendance SET emp_id = ?, att_date = ?, check_in_time = ?, check_out_time = ?, status = ?, remark = ? WHERE att_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, attendance.getEmpId());
            pstmt.setDate(2, attendance.getAttDate());
            pstmt.setTimestamp(3, attendance.getCheckInTime());
            pstmt.setTimestamp(4, attendance.getCheckOutTime());
            pstmt.setString(5, attendance.getStatus());
            pstmt.setString(6, attendance.getRemark());
            pstmt.setInt(7, attendance.getAttId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public boolean delete(Integer attId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE attendance SET is_deleted = 1 WHERE att_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, attId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public Attendance getById(Integer attId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM attendance WHERE att_id = ? AND is_deleted = 0";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, attId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractAttendance(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }

    public List<Attendance> list() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Attendance> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM attendance WHERE is_deleted = 0 ORDER BY att_date DESC, att_id DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(extractAttendance(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    public List<Attendance> listByEmpId(Integer empId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Attendance> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM attendance WHERE emp_id = ? AND is_deleted = 0 ORDER BY att_date DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(extractAttendance(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    private Attendance extractAttendance(ResultSet rs) throws SQLException {
        Attendance att = new Attendance();
        att.setAttId(rs.getInt("att_id"));
        att.setEmpId(rs.getInt("emp_id"));
        att.setAttDate(rs.getDate("att_date"));
        att.setCheckInTime(rs.getTimestamp("check_in_time"));
        att.setCheckOutTime(rs.getTimestamp("check_out_time"));
        att.setStatus(rs.getString("status"));
        att.setRemark(rs.getString("remark"));
        att.setCreateTime(rs.getTimestamp("create_time"));
        att.setUpdateTime(rs.getTimestamp("update_time"));
        return att;
    }
}
