-- 创建数据库
CREATE DATABASE IF NOT EXISTS managementsys DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE managementsys;

SET NAMES utf8mb4;
SET NAMES utf8;

-- 创建用户表
CREATE TABLE IF NOT EXISTS sys_user (
    user_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(MD5加密)',
    email VARCHAR(100) UNIQUE COMMENT '邮箱',
    role ENUM('user', 'admin') DEFAULT 'user' COMMENT '角色(user/admin)',
    status TINYINT(1) DEFAULT 1 COMMENT '状态(0-禁用,1-启用)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 插入默认管理员账号
INSERT INTO sys_user (username, password, email, role, status) VALUES
('admin', MD5('admin123'), 'admin@example.com', 'admin', 1),
('user', MD5('user123'), 'user@example.com', 'user', 1);

-- 创建部门表
CREATE TABLE IF NOT EXISTS department (
    dept_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '部门ID',
    dept_name VARCHAR(100) NOT NULL COMMENT '部门名称',
    dept_location VARCHAR(200) COMMENT '部门地点',
    dept_phone VARCHAR(20) COMMENT '部门电话',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除(0-正常,1-删除)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- 创建员工表
CREATE TABLE IF NOT EXISTS employee (
    emp_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '员工ID',
    emp_name VARCHAR(100) NOT NULL COMMENT '员工姓名',
    emp_gender ENUM('M', 'F') NOT NULL COMMENT 'Gender',
    emp_birthdate DATE COMMENT '出生日期',
    emp_phone VARCHAR(20) COMMENT '联系电话',
    emp_email VARCHAR(100) COMMENT '邮箱',
    emp_address VARCHAR(200) COMMENT '家庭住址',
    dept_id INT COMMENT '所属部门ID',
    hire_date DATE NOT NULL COMMENT '入职日期',
    job_title VARCHAR(100) COMMENT '职位',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除(0-正常,1-删除)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (dept_id) REFERENCES department(dept_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工表';

-- 创建考勤表
CREATE TABLE IF NOT EXISTS attendance (
    att_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '考勤ID',
    emp_id INT NOT NULL COMMENT '员工ID',
    att_date DATE NOT NULL COMMENT '考勤日期',
    check_in_time DATETIME COMMENT '签到时间',
    check_out_time DATETIME COMMENT '签退时间',
    status ENUM('Normal', 'Late', 'Early', 'Absent', 'Leave') DEFAULT 'Normal' COMMENT 'Status',
    remark VARCHAR(200) COMMENT '备注',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除(0-正常,1-删除)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (emp_id) REFERENCES employee(emp_id) ON DELETE CASCADE,
    UNIQUE KEY unique_attendance (emp_id, att_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考勤表';

-- 创建薪资表
CREATE TABLE IF NOT EXISTS salary (
    sal_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '薪资ID',
    emp_id INT NOT NULL COMMENT '员工ID',
    sal_month VARCHAR(7) NOT NULL COMMENT '薪资月份(YYYY-MM)',
    base_salary DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '基本工资',
    bonus DECIMAL(10,2) DEFAULT 0.00 COMMENT '奖金',
    allowance DECIMAL(10,2) DEFAULT 0.00 COMMENT '补贴',
    deduction DECIMAL(10,2) DEFAULT 0.00 COMMENT '扣款',
    total_salary DECIMAL(10,2) GENERATED ALWAYS AS (base_salary + bonus + allowance - deduction) STORED COMMENT '实发工资',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除(0-正常,1-删除)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (emp_id) REFERENCES employee(emp_id) ON DELETE CASCADE,
    UNIQUE KEY unique_salary (emp_id, sal_month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='薪资表';

-- 插入部门示例数据
INSERT INTO department (dept_name, dept_location, dept_phone) VALUES
('技术部', '3楼301', '010-88881111'),
('人事部', '2楼201', '010-88882222'),
('财务部', '2楼202', '010-88883333'),
('市场部', '1楼101', '010-88884444'),
('运营部', '1楼102', '010-88885555');

-- 插入员工示例数据
INSERT INTO employee (emp_name, emp_gender, emp_birthdate, emp_phone, emp_email, emp_address, dept_id, hire_date, job_title) VALUES
('Zhang San', 'M', '1990-05-15', '13800138001', 'zhangsan@example.com', 'Beijing Chaoyang', 1, '2020-01-15', 'Senior Engineer'),
('Li Si', 'F', '1992-08-20', '13800138002', 'lisi@example.com', 'Beijing Haidian', 1, '2021-03-20', 'Mid Engineer'),
('Wang Wu', 'M', '1988-12-10', '13800138003', 'wangwu@example.com', 'Beijing Xicheng', 2, '2019-06-10', 'HR Manager'),
('Zhao Liu', 'F', '1995-03-25', '13800138004', 'zhaoliu@example.com', 'Beijing Dongcheng', 3, '2022-01-05', 'Finance Manager'),
('Qian Qi', 'M', '1993-11-30', '13800138005', 'qianqi@example.com', 'Beijing Fengtai', 4, '2020-08-18', 'Marketing Manager'),
('Sun Ba', 'F', '1997-07-07', '13800138006', 'sunba@example.com', 'Beijing Shijingshan', 5, '2023-02-14', 'Operations Specialist');

-- 插入考勤示例数据
INSERT INTO attendance (emp_id, att_date, check_in_time, check_out_time, status, remark) VALUES
(1, '2026-05-26', '2026-05-26 08:55:00', '2026-05-26 18:05:00', 'Normal', NULL),
(1, '2026-05-27', '2026-05-27 09:10:00', '2026-05-27 18:10:00', 'Late', 'Traffic'),
(1, '2026-05-28', '2026-05-28 08:45:00', '2026-05-28 18:00:00', 'Normal', NULL),
(2, '2026-05-26', '2026-05-26 08:58:00', '2026-05-26 18:02:00', 'Normal', NULL),
(2, '2026-05-27', '2026-05-27 08:50:00', '2026-05-27 17:30:00', 'Early', 'Leave Early'),
(3, '2026-05-26', '2026-05-26 08:30:00', '2026-05-26 18:30:00', 'Normal', NULL),
(3, '2026-05-27', NULL, NULL, 'Leave', 'Annual Leave'),
(4, '2026-05-26', '2026-05-26 09:00:00', '2026-05-26 18:00:00', 'Normal', NULL),
(5, '2026-05-26', '2026-05-26 08:40:00', '2026-05-26 19:00:00', 'Normal', 'OT'),
(6, '2026-05-26', '2026-05-26 08:55:00', '2026-05-26 18:00:00', 'Normal', NULL);

-- 插入薪资示例数据
INSERT INTO salary (emp_id, sal_month, base_salary, bonus, allowance, deduction) VALUES
(1, '2026-04', 15000.00, 3000.00, 500.00, 200.00),
(1, '2026-05', 15000.00, 2500.00, 500.00, 100.00),
(2, '2026-04', 10000.00, 1500.00, 300.00, 150.00),
(2, '2026-05', 10000.00, 1200.00, 300.00, 50.00),
(3, '2026-04', 12000.00, 2000.00, 400.00, 100.00),
(3, '2026-05', 12000.00, 1800.00, 400.00, 0.00),
(4, '2026-04', 11000.00, 1800.00, 350.00, 120.00),
(4, '2026-05', 11000.00, 1600.00, 350.00, 80.00),
(5, '2026-04', 13000.00, 2500.00, 450.00, 180.00),
(5, '2026-05', 13000.00, 2800.00, 450.00, 150.00),
(6, '2026-04', 8000.00, 800.00, 200.00, 50.00),
(6, '2026-05', 8000.00, 900.00, 200.00, 30.00);
