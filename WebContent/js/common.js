/**
 * 人事管理系统 - 公共JS
 * 包含主题切换、模态框、消息提示等通用功能
 */

// ============================================
// 主题切换功能
// ============================================

/**
 * 初始化主题
 * 优先读取本地存储的主题设置，否则跟随系统主题
 */
function initTheme() {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) {
        document.documentElement.setAttribute('data-theme', savedTheme);
    } else {
        // 检测系统主题偏好
        if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
            document.documentElement.setAttribute('data-theme', 'dark');
        } else {
            document.documentElement.setAttribute('data-theme', 'light');
        }
    }
    updateThemeToggleText();
}

/**
 * 更新主题切换按钮文字
 */
function updateThemeToggleText() {
    const themeToggle = document.querySelector('.theme-toggle');
    if (themeToggle) {
        const textSpan = themeToggle.querySelector('.text');
        const iconSpan = themeToggle.querySelector('.icon');
        if (textSpan && iconSpan) {
            const currentTheme = document.documentElement.getAttribute('data-theme');
            if (currentTheme === 'dark') {
                textSpan.textContent = '浅色模式';
                iconSpan.textContent = '○';
            } else {
                textSpan.textContent = '深色模式';
                iconSpan.textContent = '●';
            }
        }
    }
}

/**
 * 切换主题
 */
function toggleTheme() {
    const currentTheme = document.documentElement.getAttribute('data-theme');
    const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', newTheme);
    localStorage.setItem('theme', newTheme);
    updateThemeToggleText();
}

// ============================================
// 模态框功能
// ============================================

/**
 * 显示模态框
 * @param {string} modalId - 模态框ID
 */
function showModal(modalId) {
    document.getElementById(modalId).classList.add('show');
}

/**
 * 隐藏模态框
 * @param {string} modalId - 模态框ID
 */
function hideModal(modalId) {
    document.getElementById(modalId).classList.remove('show');
    const form = document.getElementById(modalId).querySelector('form');
    if (form) {
        form.reset();
    }
}

// ============================================
// 消息提示功能
// ============================================

/**
 * 显示消息提示
 * @param {string} message - 消息内容
 * @param {string} type - 消息类型：success | error
 */
function showMessage(message, type = 'success') {
    // 移除已存在的消息
    const existingToast = document.querySelector('.message-toast');
    if (existingToast) {
        existingToast.remove();
    }

    const div = document.createElement('div');
    div.className = `message-toast ${type}`;
    div.textContent = message;
    document.body.appendChild(div);

    // 3秒后自动移除
    setTimeout(() => {
        div.style.animation = 'slideOut 0.3s ease forwards';
        setTimeout(() => div.remove(), 300);
    }, 3000);
}

// 添加滑出动画
const style = document.createElement('style');
style.textContent = `
    @keyframes slideOut {
        from { transform: translateX(0); opacity: 1; }
        to { transform: translateX(100%); opacity: 0; }
    }
`;
document.head.appendChild(style);

// ============================================
// 数据请求功能
// ============================================

/**
 * 发送API请求
 * @param {string} url - 请求URL
 * @param {object} options - 请求选项
 * @returns {Promise<any>} 响应数据
 */
async function fetchData(url, options = {}) {
    try {
        // 默认配置：携带credentials以保持session
        const defaultOptions = {
            credentials: 'include',
            ...options
        };
        
        const response = await fetch(url, defaultOptions);
        const data = await response.json();
        
        // 检查是否返回了错误标记
        if (data.error) {
            throw new Error(data.error);
        }
        
        return data;
    } catch (error) {
        // 避免重复显示错误消息（由调用方处理）
        console.error('API请求失败:', error);
        throw error;
    }
}

// ============================================
// 数据映射转换函数（数据库英文 -> 前端中文）
// ============================================

/**
 * 性别映射：M/F -> 男/女
 */
function genderMap(value) {
    const map = { 'M': '男', 'F': '女' };
    return map[value] || value;
}

/**
 * 考勤状态映射：英文 -> 中文
 */
function statusMap(value) {
    const map = {
        'Normal': '正常',
        'Late': '迟到',
        'Early': '早退',
        'Absent': '旷工',
        'Leave': '请假'
    };
    return map[value] || value;
}

// ============================================
// 工具函数
// ============================================

/**
 * 格式化日期（仅日期部分）
 * @param {string} dateStr - ISO日期字符串
 * @returns {string} 格式化后的日期 YYYY-MM-DD
 */
function formatDate(dateStr) {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    return date.toISOString().split('T')[0];
}

/**
 * 格式化日期时间
 * @param {string} dateTimeStr - ISO日期时间字符串
 * @returns {string} 格式化后的日期时间
 */
function formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return '';
    return dateTimeStr;
}

// ============================================
// 导航功能
// ============================================

/**
 * 导航到指定页面，强制刷新避免缓存
 * @param {string} url - 目标页面URL
 */
function navigateTo(url) {
    // 强制刷新页面，避免浏览器缓存
    window.location.href = url + '?t=' + Date.now();
}

// ============================================
// 登录检查功能
// ============================================

/**
 * 检查用户是否已登录
 * 如果未登录，跳转到登录页面
 */
function checkLogin() {
    const username = sessionStorage.getItem('username');
    if (!username) {
        window.location.href = 'login.html';
        return false;
    }
    
    const userInfo = document.getElementById('user-info');
    const usernameDisplay = document.getElementById('username-display');
    if (userInfo && usernameDisplay) {
        usernameDisplay.textContent = `欢迎, ${username}`;
        userInfo.style.display = 'flex';
    }
    return true;
}

/**
 * 退出登录
 */
function logout() {
    sessionStorage.removeItem('userId');
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('role');
    window.location.href = 'login.html';
}

// ============================================
// 页面加载完成后初始化（仅初始化主题和回到顶部，不检查登录）
// ============================================

document.addEventListener('DOMContentLoaded', function() {
    initTheme();
    initBackToTop();
});

// ============================================
// 回到顶部功能
// ============================================

/**
 * 初始化回到顶部按钮
 */
function initBackToTop() {
    const backBtn = document.createElement('button');
    backBtn.className = 'back-to-top';
    backBtn.innerHTML = '↑';
    backBtn.onclick = scrollToTop;
    document.body.appendChild(backBtn);
    
    window.addEventListener('scroll', function() {
        if (window.pageYOffset > 300) {
            backBtn.classList.add('show');
        } else {
            backBtn.classList.remove('show');
        }
    });
}

/**
 * 平滑滚动到顶部
 */
function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}
