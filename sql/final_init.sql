SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS managementsys;
CREATE DATABASE managementsys DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE managementsys;

-- 1. 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    role ENUM('user', 'admin') DEFAULT 'user',
    status TINYINT(1) DEFAULT 1,
    is_deleted TINYINT(1) DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 部门表
CREATE TABLE IF NOT EXISTS department (
    dept_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL,
    dept_location VARCHAR(200),
    dept_phone VARCHAR(20),
    parent_id INT DEFAULT 0,
    is_deleted TINYINT(1) DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 职位表
CREATE TABLE IF NOT EXISTS `position` (
    position_id INT AUTO_INCREMENT PRIMARY KEY,
    position_name VARCHAR(100) NOT NULL UNIQUE,
    position_level INT DEFAULT 1,
    position_desc VARCHAR(200),
    is_deleted TINYINT(1) DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 职称表
CREATE TABLE IF NOT EXISTS title (
    title_id INT AUTO_INCREMENT PRIMARY KEY,
    title_name VARCHAR(100) NOT NULL UNIQUE,
    title_level ENUM('初级', '中级', '副高级', '高级') DEFAULT '初级',
    title_desc VARCHAR(200),
    is_deleted TINYINT(1) DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 绩效表
CREATE TABLE IF NOT EXISTS performance (
    perf_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id INT NOT NULL,
    period VARCHAR(20) NOT NULL,
    score DECIMAL(5,2) NOT NULL,
    performance_grade ENUM('A', 'B', 'C', 'D') DEFAULT 'C',
    evaluator VARCHAR(50),
    evaluate_date DATE,
    remark VARCHAR(200),
    is_deleted TINYINT(1) DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_emp_id (emp_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. 员工表
CREATE TABLE IF NOT EXISTS employee (
    emp_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_name VARCHAR(100) NOT NULL,
    emp_gender ENUM('M', 'F') NOT NULL,
    emp_birthdate DATE,
    emp_phone VARCHAR(20),
    emp_email VARCHAR(100),
    emp_address VARCHAR(200),
    dept_id INT,
    hire_date DATE NOT NULL,
    job_title VARCHAR(100),
    position_id INT,
    title_id INT,
    is_deleted TINYINT(1) DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_dept_id (dept_id),
    INDEX idx_position_id (position_id),
    INDEX idx_title_id (title_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. 考勤表
CREATE TABLE IF NOT EXISTS attendance (
    att_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id INT NOT NULL,
    att_date DATE NOT NULL,
    check_in_time DATETIME,
    check_out_time DATETIME,
    status ENUM('Normal', 'Late', 'Early', 'Absent', 'Leave') DEFAULT 'Normal',
    remark VARCHAR(200),
    is_deleted TINYINT(1) DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_emp_id (emp_id),
    INDEX idx_att_date (att_date),
    UNIQUE KEY uk_attendance (emp_id, att_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. 薪资表
CREATE TABLE IF NOT EXISTS salary (
    sal_id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id INT NOT NULL,
    sal_month VARCHAR(7) NOT NULL,
    base_salary DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    bonus DECIMAL(10,2) DEFAULT 0.00,
    allowance DECIMAL(10,2) DEFAULT 0.00,
    deduction DECIMAL(10,2) DEFAULT 0.00,
    total_salary DECIMAL(10,2) GENERATED ALWAYS AS (base_salary + bonus + allowance - deduction) STORED,
    is_deleted TINYINT(1) DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_emp_id (emp_id),
    UNIQUE KEY uk_salary (emp_id, sal_month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9. 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    role_desc VARCHAR(200),
    is_deleted TINYINT(1) DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10. 权限表
CREATE TABLE IF NOT EXISTS sys_permission (
    perm_id INT AUTO_INCREMENT PRIMARY KEY,
    perm_name VARCHAR(100) NOT NULL,
    perm_code VARCHAR(100) NOT NULL UNIQUE,
    perm_type ENUM('menu', 'button', 'api') DEFAULT 'button',
    perm_url VARCHAR(200),
    parent_id INT DEFAULT 0,
    sort_order INT DEFAULT 0,
    is_deleted TINYINT(1) DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 11. 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 12. 角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_id INT NOT NULL,
    perm_id INT NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_role_perm (role_id, perm_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 13. 操作日志表
CREATE TABLE IF NOT EXISTS operation_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    username VARCHAR(50),
    operation VARCHAR(100) NOT NULL,
    module VARCHAR(50),
    detail VARCHAR(500),
    ip_address VARCHAR(50),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入数据

-- 用户
INSERT INTO sys_user (username, password, email, role, status) VALUES
('admin', MD5('admin123'), 'admin@example.com', 'admin', 1),
('user', MD5('user123'), 'user@example.com', 'user', 1);

-- 部门
INSERT INTO department (dept_name, dept_location, dept_phone, parent_id) VALUES
('技术部', '3楼301', '010-88881111', 0),
('人事部', '2楼201', '010-88882222', 0),
('财务部', '2楼202', '010-88883333', 0),
('市场部', '1楼101', '010-88884444', 0),
('运营部', '1楼102', '010-88885555', 0),
('研发一组', '3楼302', '010-88881112', 1),
('研发二组', '3楼303', '010-88881113', 1);

-- 职位
INSERT INTO `position` (position_name, position_level, position_desc) VALUES
('初级工程师', 1, '初级技术岗位'),
('中级工程师', 2, '中级技术岗位'),
('高级工程师', 3, '高级技术岗位'),
('技术主管', 4, '技术团队负责人'),
('技术总监', 5, '技术部门负责人'),
('HR专员', 1, '人事专员'),
('HR经理', 3, '人事部门负责人'),
('财务专员', 1, '财务专员'),
('财务经理', 3, '财务部门负责人'),
('市场专员', 1, '市场专员'),
('市场经理', 3, '市场部门负责人'),
('运营专员', 1, '运营专员'),
('运营经理', 3, '运营部门负责人');

-- 职称
INSERT INTO title (title_name, title_level, title_desc) VALUES
('助理工程师', '初级', '初级技术职称'),
('工程师', '中级', '中级技术职称'),
('高级工程师', '副高级', '副高级职称'),
('技术专家', '高级', '高级职称'),
('经济师', '中级', '经济系列职称'),
('高级经济师', '高级', '经济系列高级职称'),
('会计师', '中级', '会计系列职称'),
('高级会计师', '高级', '会计系列高级职称');

-- 员工
INSERT INTO employee (emp_name, emp_gender, emp_birthdate, emp_phone, emp_email, emp_address, dept_id, hire_date, job_title, position_id, title_id) VALUES
('张三', 'M', '1990-05-15', '13800138001', 'zhangsan@example.com', '北京市朝阳区', 6, '2020-01-15', '高级工程师', 3, 3),
('李四', 'F', '1992-08-20', '13800138002', 'lisi@example.com', '北京市海淀区', 6, '2021-03-20', '中级工程师', 2, 2),
('王五', 'M', '1988-12-10', '13800138003', 'wangwu@example.com', '北京市西城区', 2, '2019-06-10', 'HR经理', 7, 4),
('赵六', 'F', '1995-03-25', '13800138004', 'zhaoliu@example.com', '北京市东城区', 3, '2022-01-05', '财务经理', 9, 8),
('钱七', 'M', '1993-11-30', '13800138005', 'qianqi@example.com', '北京市丰台区', 4, '2020-08-18', '市场经理', 11, 6),
('孙八', 'F', '1997-07-07', '13800138006', 'sunba@example.com', '北京市石景山区', 5, '2023-02-14', '运营专员', 12, 1),
('周九', 'M', '1989-04-18', '13800138007', 'zhoujiu@example.com', '北京市通州区', 7, '2018-11-20', '技术主管', 4, 4),
('吴十', 'F', '1994-09-30', '13800138008', 'wushi@example.com', '北京市顺义区', 2, '2021-07-08', 'HR专员', 6, 1);

-- 绩效
INSERT INTO performance (emp_id, period, score, performance_grade, evaluator, remark) VALUES
(1, '2026-Q2', 95.5, 'A', '周九', '季度表现优秀'),
(2, '2026-Q2', 82.0, 'B', '周九', '表现良好'),
(3, '2026-Q2', 88.5, 'B', 'admin', 'HR工作出色'),
(4, '2026-Q2', 78.0, 'C', 'admin', '财务工作稳定'),
(5, '2026-Q2', 92.0, 'A', 'admin', '市场拓展显著'),
(6, '2026-Q2', 75.0, 'C', 'admin', '需要提升效率');

-- 考勤
INSERT INTO attendance (emp_id, att_date, check_in_time, check_out_time, status, remark) VALUES
(1, '2026-06-17', '2026-06-17 08:55:00', '2026-06-17 18:05:00', 'Normal', NULL),
(1, '2026-06-18', '2026-06-18 09:10:00', '2026-06-18 18:10:00', 'Late', '交通拥堵'),
(1, '2026-06-19', '2026-06-19 08:45:00', '2026-06-19 18:00:00', 'Normal', NULL),
(2, '2026-06-17', '2026-06-17 08:58:00', '2026-06-17 18:02:00', 'Normal', NULL),
(2, '2026-06-18', '2026-06-18 08:50:00', '2026-06-18 17:30:00', 'Early', '提前离开'),
(3, '2026-06-17', '2026-06-17 08:30:00', '2026-06-17 18:30:00', 'Normal', NULL),
(3, '2026-06-18', NULL, NULL, 'Leave', '年假'),
(4, '2026-06-17', '2026-06-17 09:00:00', '2026-06-17 18:00:00', 'Normal', NULL),
(5, '2026-06-17', '2026-06-17 08:40:00', '2026-06-17 19:00:00', 'Normal', '加班'),
(6, '2026-06-17', '2026-06-17 08:55:00', '2026-06-17 18:00:00', 'Normal', NULL);

-- 薪资
INSERT INTO salary (emp_id, sal_month, base_salary, bonus, allowance, deduction) VALUES
(1, '2026-05', 15000.00, 3000.00, 500.00, 200.00),
(1, '2026-06', 15000.00, 2500.00, 500.00, 100.00),
(2, '2026-05', 10000.00, 1500.00, 300.00, 150.00),
(2, '2026-06', 10000.00, 1200.00, 300.00, 50.00),
(3, '2026-05', 12000.00, 2000.00, 400.00, 100.00),
(3, '2026-06', 12000.00, 1800.00, 400.00, 0.00),
(4, '2026-05', 11000.00, 1800.00, 350.00, 120.00),
(4, '2026-06', 11000.00, 1600.00, 350.00, 80.00),
(5, '2026-05', 13000.00, 2500.00, 450.00, 180.00),
(5, '2026-06', 13000.00, 2800.00, 450.00, 150.00),
(6, '2026-05', 8000.00, 800.00, 200.00, 50.00),
(6, '2026-06', 8000.00, 900.00, 200.00, 30.00);

-- 角色
INSERT INTO sys_role (role_name, role_desc) VALUES
('系统管理员', '拥有系统所有权限'),
('人事管理员', '拥有人事管理相关权限'),
('普通用户', '拥有基本查看权限');

-- 权限
INSERT INTO sys_permission (perm_name, perm_code, perm_type, perm_url, parent_id, sort_order) VALUES
('系统管理', 'system', 'menu', NULL, 0, 1),
('部门管理', 'system:dept', 'button', '/api/department', 1, 1),
('职位管理', 'system:pos', 'button', '/api/position', 1, 2),
('职称管理', 'system:title', 'button', '/api/title', 1, 3),
('角色管理', 'system:role', 'button', '/api/role', 1, 4),
('用户管理', 'system:user', 'button', '/api/users', 1, 5),
('人事管理', 'hr', 'menu', NULL, 0, 2),
('员工管理', 'hr:employee', 'button', '/api/employee', 7, 1),
('考勤管理', 'hr:attendance', 'button', '/api/attendance', 7, 2),
('薪资管理', 'hr:salary', 'button', '/api/salary', 7, 3),
('个人中心', 'profile', 'menu', NULL, 0, 3),
('查看个人信息', 'profile:view', 'button', '/api/profile', 11, 1),
('修改个人信息', 'profile:edit', 'button', '/api/profile', 11, 2),
('修改密码', 'profile:password', 'button', '/api/profile/password', 11, 3),
('操作日志', 'log', 'menu', NULL, 0, 4),
('查看日志', 'log:view', 'button', '/api/log', 15, 1);

-- 角色权限关联
INSERT INTO sys_role_permission (role_id, perm_id) SELECT 1, perm_id FROM sys_permission;
INSERT INTO sys_role_permission (role_id, perm_id) VALUES
(2, 1), (2, 2), (2, 7), (2, 8), (2, 9), (2, 10), (2, 11), (2, 12), (2, 13), (2, 14), (2, 15), (2, 16);
INSERT INTO sys_role_permission (role_id, perm_id) VALUES
(3, 2), (3, 9), (3, 10), (3, 11), (3, 12), (3, 13), (3, 14), (3, 15), (3, 16);

-- 用户角色关联
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1),
(2, 3);

-- 操作日志
INSERT INTO operation_log (user_id, username, operation, module, detail, ip_address) VALUES
(1, 'admin', 'System initialized', 'System', 'Database initialization completed', '127.0.0.1'),
(1, 'admin', 'Login', 'System', 'Admin login successful', '127.0.0.1'),
(2, 'user', 'Login', 'System', 'User login successful', '127.0.0.1');

-- 添加外键约束
ALTER TABLE department ADD CONSTRAINT fk_dept_parent FOREIGN KEY (parent_id) REFERENCES department(dept_id) ON DELETE SET NULL;
ALTER TABLE employee ADD CONSTRAINT fk_emp_dept FOREIGN KEY (dept_id) REFERENCES department(dept_id) ON DELETE SET NULL;
ALTER TABLE employee ADD CONSTRAINT fk_emp_position FOREIGN KEY (position_id) REFERENCES `position`(position_id) ON DELETE SET NULL;
ALTER TABLE employee ADD CONSTRAINT fk_emp_title FOREIGN KEY (title_id) REFERENCES title(title_id) ON DELETE SET NULL;
ALTER TABLE attendance ADD CONSTRAINT fk_att_emp FOREIGN KEY (emp_id) REFERENCES employee(emp_id) ON DELETE CASCADE;
ALTER TABLE salary ADD CONSTRAINT fk_sal_emp FOREIGN KEY (emp_id) REFERENCES employee(emp_id) ON DELETE CASCADE;
ALTER TABLE performance ADD CONSTRAINT fk_perf_emp FOREIGN KEY (emp_id) REFERENCES employee(emp_id) ON DELETE CASCADE;
ALTER TABLE sys_user_role ADD CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE;
ALTER TABLE sys_user_role ADD CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES sys_role(role_id) ON DELETE CASCADE;
ALTER TABLE sys_role_permission ADD CONSTRAINT fk_rp_role FOREIGN KEY (role_id) REFERENCES sys_role(role_id) ON DELETE CASCADE;
ALTER TABLE sys_role_permission ADD CONSTRAINT fk_rp_perm FOREIGN KEY (perm_id) REFERENCES sys_permission(perm_id) ON DELETE CASCADE;

SET FOREIGN_KEY_CHECKS = 1;

SELECT 'Database initialized successfully' AS result;