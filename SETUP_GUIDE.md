# 项目设置指南

## 问题说明
您遇到的servlet文件报错问题主要是因为缺少Eclipse项目配置文件和Tomcat运行时库。

## 已完成的修复
我们已经为您创建了以下配置文件：
- `.project` - Eclipse项目描述文件
- `.classpath` - 类路径配置文件
- `.settings/` - Eclipse项目设置目录

## 在Eclipse中正确设置项目

### 方案一：重新导入项目（推荐）

1. 在Eclipse中关闭当前项目（如果已打开）
2. 选择 `File` → `Import...`
3. 选择 `Existing Projects into Workspace`
4. 选择项目根目录 `ManagementSystem`
5. 勾选项目，点击 `Finish`

### 方案二：配置Tomcat运行时

如果方案一仍有问题，请配置Tomcat：

1. 下载并安装 [Apache Tomcat 9.x](https://tomcat.apache.org/download-90.cgi)
2. 在Eclipse中：`Window` → `Preferences` → `Server` → `Runtime Environments`
3. 点击 `Add...` → 选择 `Apache Tomcat v9.0`
4. 浏览到您的Tomcat安装目录
5. 完成配置
6. 右键项目 → `Properties` → `Targeted Runtimes`
7. 勾选 `Apache Tomcat v9.0`

### 方案三：手动添加Servlet API库（备选方案）

如果不想配置Tomcat，可以手动添加servlet-api.jar：

1. 从Tomcat的 `lib` 目录复制 `servlet-api.jar`
2. 粘贴到项目的 `WebContent/WEB-INF/lib/` 目录
3. 右键该jar文件 → `Build Path` → `Add to Build Path`

## 验证项目配置

完成上述步骤后，检查以下内容：

1. 项目结构应显示为动态Web项目
2. src/main/java 应被识别为源文件夹
3. 所有servlet文件的错误应消失
4. 项目可以部署到Tomcat

## 快速测试

1. 右键项目 → `Run As` → `Run on Server`
2. 选择Tomcat服务器
3. 访问 http://localhost:8080/ManagementSystem/

## 常见问题

**Q: 为什么需要这些配置文件？**
A: Eclipse需要这些文件来识别项目类型、配置源路径和库依赖。

**Q: 可以用其他IDE吗？**
A: 可以，IntelliJ IDEA也支持导入该项目，但需要相应的配置。

**Q: 数据库配置在哪里？**
A: 数据库配置在 `src/main/resources/db.properties` 文件中。
