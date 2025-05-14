package com.cafeteria.servlet;

import com.cafeteria.dao.UserDAO;
import com.cafeteria.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 处理用户登录请求的Servlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("error", "用户名和密码不能为空");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        User user = userDAO.findByUsername(username);

        // 注意：实际应用中需要比较密码哈希值，而不是明文密码
        // 这里为了简化，直接比较明文。在生产环境中，必须使用密码哈希和盐值。
        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user); // 将用户信息存入Session
            // 登录成功，重定向到菜品列表页面或其他主页
            response.sendRedirect(request.getContextPath() + "/dishes"); // 假设菜品列表页面的URL是 /dishes
        } else {
            // 登录失败
            request.setAttribute("error", "用户名或密码错误");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 直接访问 /login 时，显示登录页面
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}