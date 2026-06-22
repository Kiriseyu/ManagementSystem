@echo off
chcp 65001 >nul
setlocal EnableDelayedExpansion

echo ================================================
echo 人事管理系统 - 数据库初始化脚本
echo ================================================
echo.

set "MYSQL_EXE=mysql"
set "DB_HOST=localhost"
set "DB_PORT=3306"
set "DB_USER=root"
set "DB_PASSWORD=kamori"
set "DB_NAME=managementsys"
set "SQL_FILE=sql\final_init.sql"

echo 检查MySQL客户端是否可用...
where mysql >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到mysql命令，请确保MySQL已正确安装并添加到PATH环境变量
    pause
    exit /b 1
)
echo [OK] MySQL客户端可用

echo.
echo 检查SQL脚本文件...
if not exist "%SQL_FILE%" (
    echo [错误] SQL脚本文件不存在: %SQL_FILE%
    pause
    exit /b 1
)
echo [OK] SQL脚本文件存在

echo.
echo ================================================
echo 即将执行以下操作:
echo  1. 删除现有数据库 (如果存在): %DB_NAME%
echo  2. 创建新数据库并执行初始化脚本
echo ================================================
echo.

set "confirm="
set /p "confirm=确认执行数据库初始化? (Y/N): "
if /i not "%confirm%"=="Y" (
    echo 操作已取消
    pause
    exit /b 0
)

echo.
echo 正在删除现有数据库...
%MYSQL_EXE% -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% -e "DROP DATABASE IF EXISTS %DB_NAME%;"
if %errorlevel% neq 0 (
    echo [错误] 删除数据库失败
    pause
    exit /b 1
)
echo [OK] 删除数据库完成

echo.
echo 正在执行SQL初始化脚本...
%MYSQL_EXE% -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASSWORD% < "%SQL_FILE%"
if %errorlevel% neq 0 (
    echo [错误] 执行SQL脚本失败
    pause
    exit /b 1
)
echo [OK] SQL脚本执行完成

echo.
echo ================================================
echo 数据库初始化成功!
echo ================================================
echo 数据库: %DB_NAME%
echo 默认管理员账号: admin / admin123
echo 默认普通用户账号: user / user123
echo ================================================

pause