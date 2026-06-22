package com.hr.dao;

import com.hr.entity.Performance;
import com.hr.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerformanceDAO {
    public List<Performance> list() {
        List<Performance> list = new ArrayList<>();
        String sql = "SELECT * FROM performance WHERE is_deleted = 0 ORDER BY evaluate_date DESC";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Performance perf = new Performance();
                perf.setPerfId(rs.getInt("perf_id"));
                perf.setEmpId(rs.getInt("emp_id"));
                perf.setPeriod(rs.getString("period"));
                perf.setScore(rs.getDouble("score"));
                perf.setPerformanceGrade(rs.getString("performance_grade"));
                perf.setEvaluator(rs.getString("evaluator"));
                perf.setEvaluateDate(rs.getDate("evaluate_date"));
                perf.setRemark(rs.getString("remark"));
                perf.setIsDeleted(rs.getInt("is_deleted"));
                perf.setCreateTime(rs.getTimestamp("create_time"));
                perf.setUpdateTime(rs.getTimestamp("update_time"));
                list.add(perf);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Performance getById(int id) {
        String sql = "SELECT * FROM performance WHERE perf_id = ? AND is_deleted = 0";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Performance perf = new Performance();
                    perf.setPerfId(rs.getInt("perf_id"));
                    perf.setEmpId(rs.getInt("emp_id"));
                    perf.setPeriod(rs.getString("period"));
                    perf.setScore(rs.getDouble("score"));
                    perf.setPerformanceGrade(rs.getString("performance_grade"));
                    perf.setEvaluator(rs.getString("evaluator"));
                    perf.setEvaluateDate(rs.getDate("evaluate_date"));
                    perf.setRemark(rs.getString("remark"));
                    perf.setIsDeleted(rs.getInt("is_deleted"));
                    perf.setCreateTime(rs.getTimestamp("create_time"));
                    perf.setUpdateTime(rs.getTimestamp("update_time"));
                    return perf;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int insert(Performance perf) {
        String sql = "INSERT INTO performance (emp_id, period, score, performance_grade, evaluator, remark) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, perf.getEmpId());
            pstmt.setString(2, perf.getPeriod());
            pstmt.setDouble(3, perf.getScore());
            pstmt.setString(4, perf.getPerformanceGrade());
            pstmt.setString(5, perf.getEvaluator());
            pstmt.setString(6, perf.getRemark());
            
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        perf.setPerfId(rs.getInt(1));
                    }
                }
            }
            return affected;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int update(Performance perf) {
        String sql = "UPDATE performance SET emp_id=?, period=?, score=?, performance_grade=?, evaluator=?, remark=? WHERE perf_id=?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, perf.getEmpId());
            pstmt.setString(2, perf.getPeriod());
            pstmt.setDouble(3, perf.getScore());
            pstmt.setString(4, perf.getPerformanceGrade());
            pstmt.setString(5, perf.getEvaluator());
            pstmt.setString(6, perf.getRemark());
            pstmt.setInt(7, perf.getPerfId());
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(int id) {
        String sql = "UPDATE performance SET is_deleted = 1 WHERE perf_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}