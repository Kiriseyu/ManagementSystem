# 企业人力资源管理系统 - 构建说明

## 项目结构

```
ManagementSystem/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── hr/
│       │           ├── entity/          # 实体类
│       │           ├── dao/              # 数据访问层
│       │           ├── servlet/         # Servlet层
│       │           └── util/           # 工具类
│       └── resources/
│           └── db.properties        # 数据库配置文件
├── WebContent/
│   ├── WEB-INF/
│   │   ├── lib/
│   │   │   └── mysql-connector-j-9.7.0.jar
│   │   └── web.xml
│   └── (前端页面)
└── sql/
    └── init.sql                  # 数据库初始化脚本
```

## 构建和部署步骤

### 1. 数据库初始化

首先需要先执行 `sql/init.sql` 脚本初始化数据库：

```bash
mysql -u root -p < sql/init.sql
```

### 2. 数据库配置

修改 `src/main/resources/db.properties` 文件中的数据库连接信息：

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/hr_system?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
db.username=root
db.password=your_password
```

### 3. 编译项目

使用命令行编译（需要 JDK 8 或更高版本）：

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

### 4. 部署到Tomcat

将整个 `ManagementSystem` 目录复制到 Tomcat 的 `webapps` 目录下，或者将其打包为 WAR 文件：

```bash
# 打包为 WAR 文件
jar -cvf ManagementSystem.war *
```

### 5. 启动Tomcat

启动Tomcat服务器，系统将通过以下API提供服务：

- 部门管理API: `/api/department`
- 员工管理API: `/api/employee`
- 考勤管理API: `/api/attendance`
- 薪资管理API: `/api/salary`

## API使用说明

### 部门管理API (`/api/department`)

- `GET /api/department` - 获取所有部门
- `GET /api/department?id=1` - 获取ID为1的部门
- `POST /api/department?deptName=技术部&deptLocation=3楼&deptPhone=010-12345678` - 添加部门
- `PUT /api/department?deptId=1&deptName=技术部&deptLocation=3楼&deptPhone=010-12345678` - 更新部门
- `DELETE /api/department?id=1` - 删除部门

其他模块的API使用方式类似。
