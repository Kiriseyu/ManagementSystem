# 企业人力资源管理系统 (ManagementSystem)

## 项目介绍

企业人力资源管理系统是一个基于Java Servlet + MySQL的Web应用系统，提供了完整的人力资源管理功能，包括部门管理、员工管理、考勤管理和薪资管理。

## 技术栈

- **后端**: Java Servlet (Java 8+)
- **数据库**: MySQL 5.7+
- **前端**: HTML5 + CSS3 + JavaScript
- **应用服务器**: Tomcat 8.5+

## 项目结构

```
ManagementSystem/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── hr/
│       │           ├── entity/          # 实体类
│       │           │   ├── Department.java
│       │           │   ├── Employee.java
│       │           │   ├── Attendance.java
│       │           │   └── Salary.java
│       │           ├── dao/              # 数据访问层
│       │           │   ├── DepartmentDAO.java
│       │           │   ├── EmployeeDAO.java
│       │           │   ├── AttendanceDAO.java
│       │           │   └── SalaryDAO.java
│       │           ├── servlet/         # Servlet层
│       │           │   ├── DepartmentServlet.java
│       │           │   ├── EmployeeServlet.java
│       │           │   ├── AttendanceServlet.java
│       │           │   └── SalaryServlet.java
│       │           └── util/           # 工具类
│       │               └── DBUtil.java
│       └── resources/
│           └── db.properties        # 数据库配置文件
├── WebContent/
│   ├── WEB-INF/
│   │   ├── lib/
│   │   │   └── mysql-connector-j-9.7.0.jar
│   │   └── web.xml
│   ├── css/
│   │   └── style.css
│   ├── js/
│   │   └── common.js
│   ├── index.html               # 首页
│   ├── department.html          # 部门管理页面
│   ├── employee.html            # 员工管理页面
│   ├── attendance.html          # 考勤管理页面
│   └── salary.html              # 薪资管理页面
├── sql/
│   └── init.sql                  # 数据库初始化脚本
├── BUILD.md                      # 构建说明
└── README.md
```

## 功能说明

### 1. 部门管理
- 查看所有部门列表
- 添加新部门
- 编辑部门信息
- 删除部门

### 2. 员工管理
- 查看所有员工列表
- 按部门筛选员工
- 添加新员工
- 编辑员工信息
- 删除员工

### 3. 考勤管理
- 查看所有考勤记录
- 按员工筛选考勤记录
- 添加考勤记录（签到/签退）
- 编辑考勤信息
- 删除考勤记录

### 4. 薪资管理
- 查看所有薪资记录
- 按员工筛选薪资记录
- 添加薪资记录
- 编辑薪资信息
- 删除薪资记录
- 自动计算实发工资

## 数据库配置

### 1. 数据库初始化

执行 `sql/init.sql` 脚本初始化数据库：

```bash
mysql -u root -p < sql/init.sql
```

或者在MySQL客户端中直接执行该脚本。

### 2. 配置数据库连接

修改 `src/main/resources/db.properties` 文件中的数据库连接信息：

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/hr_system?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
db.username=root
db.password=your_password
```

### 3. 数据库表结构

系统包含以下4张表：

- **department** - 部门表
- **employee** - 员工表
- **attendance** - 考勤表
- **salary** - 薪资表

## 部署步骤

### 前置要求

- JDK 8 或更高版本
- Apache Tomcat 8.5 或更高版本
- MySQL 5.7 或更高版本

### 1. 编译项目

#### Windows:
```powershell
# 创建输出目录
mkdir WebContent\WEB-INF\classes

# 编译Java文件
javac -encoding UTF-8 -cp "WebContent\WEB-INF\lib\*" -d WebContent\WEB-INF\classes src\main\java\com\hr\entity\*.java src\main\java\com\hr\util\*.java src\main\java\com\hr\dao\*.java src\main\java\com\hr\servlet\*.java

# 复制资源文件
copy src\main\resources\db.properties WebContent\WEB-INF\classes\
```

#### Linux/Mac:
```bash
# 创建输出目录
mkdir -p WebContent/WEB-INF/classes

# 编译Java文件
javac -encoding UTF-8 -cp "WebContent/WEB-INF/lib/*" -d WebContent/WEB-INF/classes src/main/java/com/hr/entity/*.java src/main/java/com/hr/util/*.java src/main/java/com/hr/dao/*.java src/main/java/com/hr/servlet/*.java

# 复制资源文件
cp src/main/resources/db.properties WebContent/WEB-INF/classes/
```

### 2. 部署到Tomcat

**方式一：直接复制目录**
将整个 `ManagementSystem` 目录复制到 Tomcat 的 `webapps` 目录下。

**方式二：打包为WAR文件**
```bash
jar -cvf ManagementSystem.war *
```
然后将生成的 `ManagementSystem.war` 文件复制到 Tomcat 的 `webapps` 目录下。

### 3. 启动Tomcat

启动Tomcat服务器，访问 `http://localhost:8080/ManagementSystem/` 即可使用系统。

## API接口说明

### 部门管理API (`/api/department`)

| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| GET | /api/department | 获取所有部门 | - |
| GET | /api/department | 获取指定部门 | id |
| POST | /api/department | 添加部门 | deptName, deptLocation, deptPhone |
| PUT | /api/department | 更新部门 | deptId, deptName, deptLocation, deptPhone |
| DELETE | /api/department | 删除部门 | id |

### 员工管理API (`/api/employee`)

| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| GET | /api/employee | 获取所有员工 | - |
| GET | /api/employee | 获取指定员工 | id |
| GET | /api/employee | 按部门获取员工 | deptId |
| POST | /api/employee | 添加员工 | empName, empGender, empBirthdate, empPhone, empEmail, empAddress, deptId, hireDate, jobTitle |
| PUT | /api/employee | 更新员工 | empId, empName, empGender, empBirthdate, empPhone, empEmail, empAddress, deptId, hireDate, jobTitle |
| DELETE | /api/employee | 删除员工 | id |

### 考勤管理API (`/api/attendance`)

| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| GET | /api/attendance | 获取所有考勤记录 | - |
| GET | /api/attendance | 获取指定考勤记录 | id |
| GET | /api/attendance | 按员工获取考勤记录 | empId |
| POST | /api/attendance | 添加考勤记录 | empId, attDate, checkInTime, checkOutTime, status, remark |
| PUT | /api/attendance | 更新考勤记录 | attId, empId, attDate, checkInTime, checkOutTime, status, remark |
| DELETE | /api/attendance | 删除考勤记录 | id |

### 薪资管理API (`/api/salary`)

| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| GET | /api/salary | 获取所有薪资记录 | - |
| GET | /api/salary | 获取指定薪资记录 | id |
| GET | /api/salary | 按员工获取薪资记录 | empId |
| POST | /api/salary | 添加薪资记录 | empId, salMonth, baseSalary, bonus, allowance, deduction |
| PUT | /api/salary | 更新薪资记录 | salId, empId, salMonth, baseSalary, bonus, allowance, deduction |
| DELETE | /api/salary | 删除薪资记录 | id |

## 注意事项

1. 确保MySQL服务已启动
2. 确保Tomcat已正确配置并能正常运行
3. 修改数据库密码后记得重新编译项目
4. 考勤日期格式为 `YYYY-MM-DD`
5. 考勤时间格式为 `YYYY-MM-DD HH:mm:ss`
6. 薪资月份格式为 `YYYY-MM`

## 许可证

本项目仅供学习和参考使用。
