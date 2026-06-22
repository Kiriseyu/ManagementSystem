package com.hr.dao;

import com.hr.entity.OperationLog;
import com.hr.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作日志数据访问类
 */
public class OperationLogDAO {

    /**
     * 获取所有日志列表（分页）
     */
    public List<OperationLog> list(int page, int size) {
        List<OperationLog> logs = new ArrayList<>();
        String sql = "SELECT log_id, user_id, username, operation, module, detail, ip_address, create_time " +
                     "FROM operation_log ORDER BY create_time DESC LIMIT ?, ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, (page - 1) * size);
            pstmt.setInt(2, size);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OperationLog log = extractLog(rs);
                    logs.add(log);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * 获取日志总数
     */
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM operation_log";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 添加日志
     */
    public boolean add(OperationLog log) {
        String sql = "INSERT INTO operation_log (user_id, username, operation, module, detail, ip_address) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, log.getUserId());
            pstmt.setString(2, log.getUsername());
            pstmt.setString(3, log.getOperation());
            pstmt.setString(4, log.getModule());
            pstmt.setString(5, log.getDetail());
            pstmt.setString(6, log.getIpAddress());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据用户ID获取日志
     */
    public List<OperationLog> getByUserId(Integer userId, int page, int size) {
        List<OperationLog> logs = new ArrayList<>();
        String sql = "SELECT log_id, user_id, username, operation, module, detail, ip_address, create_time " +
                     "FROM operation_log WHERE user_id = ? ORDER BY create_time DESC LIMIT ?, ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, (page - 1) * size);
            pstmt.setInt(3, size);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OperationLog log = extractLog(rs);
                    logs.add(log);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * 从ResultSet中提取日志对象
     */
    private OperationLog extractLog(ResultSet rs) throws SQLException {
        OperationLog log = new OperationLog();
        log.setLogId(rs.getInt("log_id"));
        log.setUserId(rs.getObject("user_id", Integer.class));
        log.setUsername(rs.getString("username"));
        log.setOperation(rs.getString("operation"));
        log.setModule(rs.getString("module"));
        log.setDetail(rs.getString("detail"));
        log.setIpAddress(rs.getString("ip_address"));
        log.setOperationTime(rs.getString("create_time"));
        return log;
    }
}