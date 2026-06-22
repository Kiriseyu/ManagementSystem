package com.hr.dao;

import com.hr.entity.Position;
import com.hr.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 职位数据访问类
 */
public class PositionDAO {

    /**
     * 获取所有职位列表
     */
    public List<Position> list() {
        List<Position> positions = new ArrayList<>();
        String sql = "SELECT position_id, position_name, position_level, position_desc, is_deleted, create_time, update_time " +
                     "FROM `position` WHERE is_deleted = 0 ORDER BY position_level DESC, position_id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Position pos = extractPosition(rs);
                positions.add(pos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return positions;
    }

    /**
     * 根据ID获取职位
     */
    public Position getById(Integer posId) {
        String sql = "SELECT position_id, position_name, position_level, position_desc, is_deleted, create_time, update_time " +
                     "FROM `position` WHERE position_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, posId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractPosition(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加职位
     */
    public boolean add(Position position) {
        String sql = "INSERT INTO `position` (position_name, position_level, position_desc) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, position.getPosName());
            pstmt.setInt(2, position.getPosLevel() != null ? position.getPosLevel() : 1);
            pstmt.setString(3, position.getPosDesc());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新职位
     */
    public boolean update(Position position) {
        String sql = "UPDATE `position` SET position_name = ?, position_level = ?, position_desc = ? WHERE position_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, position.getPosName());
            pstmt.setInt(2, position.getPosLevel());
            pstmt.setString(3, position.getPosDesc());
            pstmt.setInt(4, position.getPosId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除职位（逻辑删除）
     */
    public boolean delete(Integer posId) {
        String sql = "UPDATE `position` SET is_deleted = 1 WHERE position_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, posId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检查职位名称是否存在
     */
    public boolean existsByName(String posName) {
        String sql = "SELECT COUNT(*) FROM `position` WHERE position_name = ? AND is_deleted = 0";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, posName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从ResultSet中提取职位对象
     */
    private Position extractPosition(ResultSet rs) throws SQLException {
        Position pos = new Position();
        pos.setPosId(rs.getInt("position_id"));
        pos.setPosName(rs.getString("position_name"));
        pos.setPosLevel(rs.getInt("position_level"));
        pos.setPosDesc(rs.getString("position_desc"));
        pos.setStatus(rs.getInt("is_deleted") == 0 ? 1 : 0);
        pos.setCreateTime(rs.getString("create_time"));
        pos.setUpdateTime(rs.getString("update_time"));
        return pos;
    }
}