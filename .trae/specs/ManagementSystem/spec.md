# 企业人力资源管理系统 - 产品需求文档

## Overview
- **Summary**: 构建一个完整的企业人力资源管理系统，采用前后端分离架构，后端使用Java开发，前端基于Web页面，MySQL数据库存储数据，使用Tomcat作为Web服务器。系统将包含员工管理、部门管理、考勤管理、薪资管理等核心功能。
- **Purpose**: 为企业提供高效的人力资源管理解决方案，简化员工信息管理流程，提升HR部门工作效率。
- **Target Users**: 企业HR管理人员、部门经理、普通员工。

## Goals
- 实现员工信息的增删改查功能
- 实现部门层级管理
- 实现考勤记录管理
- 实现薪资管理功能
- 提供友好的用户界面
- 使用前后端分离架构，便于维护和扩展

## Non-Goals (Out of Scope)
- 不包含OA审批流程
- 不包含复杂的绩效评估系统
- 不包含移动端APP
- 不包含多租户支持
- 不包含复杂的权限管理（仅基础角色区分）

## Background & Context
- 采用Eclipse兼容的Java Web项目结构
- 使用Servlet + JDBC作为后端技术栈
- 前端使用HTML/CSS/JavaScript
- MySQL 8.0+ 作为数据库
- Tomcat 9.0+ 作为Web服务器

## Functional Requirements
- **FR-1**: 员工信息管理
  - 添加新员工信息
  - 编辑员工信息
  - 删除员工信息
  - 查看员工列表和详情
- **FR-2**: 部门管理
  - 添加部门
  - 编辑部门信息
  - 删除部门
  - 查看部门层级结构
- **FR-3**: 考勤管理
  - 记录员工考勤
  - 查看考勤记录
  - 统计考勤情况
- **FR-4**: 薪资管理
  - 设置员工薪资
  - 查看薪资记录
- **FR-5**: 数据持久化
  - 所有数据存储在MySQL数据库中
  - 包含示例数据

## Non-Functional Requirements
- **NFR-1**: 系统响应时间在3秒以内
- **NFR-2**: 页面布局清晰，操作便捷
- **NFR-3**: 代码结构清晰，便于维护
- **NFR-4**: 使用标准的Eclipse项目结构

## Constraints
- **Technical**: Java 8+, MySQL 8.0+, Tomcat 9.0+, 纯前端（HTML/CSS/JS）
- **Business**: 单部门管理场景
- **Dependencies**: MySQL JDBC驱动

## Assumptions
- 数据库连接信息将通过配置文件管理
- 用户已经安装并配置好MySQL数据库
- 用户有Tomcat服务器环境
- 基础的CRUD操作已足够满足需求

## Acceptance Criteria

### AC-1: 员工信息管理
- **Given**: 系统已启动，数据库已连接
- **When**: HR用户进行员工信息的增删改查操作
- **Then**: 操作成功，数据正确保存到数据库
- **Verification**: `programmatic`
- **Notes**: 需要验证字段完整性和数据准确性

### AC-2: 部门管理
- **Given**: 系统已启动
- **When**: 用户管理部门信息
- **Then**: 部门信息正确展示和存储，支持层级关系
- **Verification**: `programmatic`

### AC-3: 数据库设计
- **Given**: 系统初始化
- **When**: 创建数据库和表结构
- **Then**: 表结构合理，包含示例数据
- **Verification**: `programmatic`

### AC-4: 前端页面
- **Given**: 浏览器访问系统
- **When**: 用户操作页面
- **Then**: 页面响应正常，数据正确展示
- **Verification**: `human-judgment`

### AC-5: 项目结构
- **Given**: 项目代码
- **When**: 查看项目结构
- **Then**: 符合Eclipse Web项目标准结构
- **Verification**: `human-judgment`

## Open Questions
- [ ] 是否需要登录认证功能？
- [ ] 薪资计算是否需要复杂公式？
- [ ] 是否需要数据导出功能？
