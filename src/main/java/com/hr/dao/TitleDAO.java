package com.hr.dao;

import com.hr.entity.Title;
import com.hr.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 职称数据访问类
 */
public class TitleDAO {

    /**
     * 获取所有职称列表
     */
    public List<Title> list() {
        List<Title> titles = new ArrayList<>();
        String sql = "SELECT title_id, title_name, title_level, title_desc, is_deleted, create_time, update_time " +
                     "FROM title WHERE is_deleted = 0 ORDER BY FIELD(title_level, '高级', '副高级', '中级', '初级'), title_id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Title title = extractTitle(rs);
                titles.add(title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return titles;
    }

    /**
     * 根据ID获取职称
     */
    public Title getById(Integer titleId) {
        String sql = "SELECT title_id, title_name, title_level, title_desc, is_deleted, create_time, update_time " +
                     "FROM title WHERE title_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, titleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractTitle(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加职称
     */
    public boolean add(Title title) {
        String sql = "INSERT INTO title (title_name, title_level, title_desc) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title.getTitleName());
            pstmt.setString(2, title.getTitleLevel());
            pstmt.setString(3, title.getTitleDesc());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新职称
     */
    public boolean update(Title title) {
        String sql = "UPDATE title SET title_name = ?, title_level = ?, title_desc = ? WHERE title_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title.getTitleName());
            pstmt.setString(2, title.getTitleLevel());
            pstmt.setString(3, title.getTitleDesc());
            pstmt.setInt(4, title.getTitleId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除职称（逻辑删除）
     */
    public boolean delete(Integer titleId) {
        String sql = "UPDATE title SET is_deleted = 1 WHERE title_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, titleId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检查职称名称是否存在
     */
    public boolean existsByName(String titleName) {
        String sql = "SELECT COUNT(*) FROM title WHERE title_name = ? AND is_deleted = 0";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, titleName);
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
     * 从ResultSet中提取职称对象
     */
    private Title extractTitle(ResultSet rs) throws SQLException {
        Title title = new Title();
        title.setTitleId(rs.getInt("title_id"));
        title.setTitleName(rs.getString("title_name"));
        title.setTitleLevel(rs.getString("title_level"));
        title.setTitleDesc(rs.getString("title_desc"));
        title.setStatus(rs.getInt("is_deleted") == 0 ? 1 : 0);
        title.setCreateTime(rs.getString("create_time"));
        title.setUpdateTime(rs.getString("update_time"));
        return title;
    }
}