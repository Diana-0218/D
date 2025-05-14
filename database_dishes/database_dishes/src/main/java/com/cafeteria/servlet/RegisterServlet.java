package com.cafeteria.servlet;

import com.cafeteria.dao.UserDAO;
import com.cafeteria.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理用户注册请求的Servlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String preference = request.getParameter("preference"); // 获取用户偏好

        // 基本验证
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "用户名和密码不能为空");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "两次输入的密码不一致");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // 检查用户名是否已存在
        if (userDAO.findByUsername(username) != null) {
            request.setAttribute("error", "用户名已被注册");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // 创建新用户对象
        // 注意：实际应用中密码应该在存储前进行哈希处理
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); // 存储明文密码（不推荐，应替换为哈希）
        newUser.setPreference(preference != null ? preference : ""); // 设置偏好，如果为空则设为空字符串

        // 添加用户到数据库
        boolean success = userDAO.addUser(newUser);

        if (success) {
            // 注册成功，重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/login.jsp?success=true");
        } else {
            // 注册失败
            request.setAttribute("error", "注册失败，请稍后重试");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 直接访问 /register 时，显示注册页面
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}