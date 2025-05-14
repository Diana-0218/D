<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户登录 - 智能食堂</title>
    <style>
        body { font-family: sans-serif; margin: 20px; background-color: #f4f4f4; }
        .container { max-width: 400px; margin: 50px auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #333; }
        label { display: block; margin-bottom: 5px; color: #555; }
        input[type="text"], input[type="password"] { width: 95%; padding: 10px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 4px; }
        button { width: 100%; padding: 10px; background-color: #5cb85c; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
        button:hover { background-color: #4cae4c; }
        .error { color: red; margin-bottom: 15px; text-align: center; }
        .register-link { text-align: center; margin-top: 15px; }
        .register-link a { color: #007bff; text-decoration: none; }
        .register-link a:hover { text-decoration: underline; }
        .success { color: green; margin-bottom: 15px; text-align: center; }
    </style>
</head>
<body>
    <div class="container">
        <h2>用户登录</h2>

        <%-- 显示错误信息 --%>
        <p class="error">${requestScope.error}</p>

        <%-- 显示注册成功信息 --%>
        <% if ("true".equals(request.getParameter("success"))) { %>
            <p class="success">注册成功，请登录！</p>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div>
                <label for="username">用户名:</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div>
                <label for="password">密码:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit">登录</button>
        </form>

        <div class="register-link">
            还没有账号？ <a href="${pageContext.request.contextPath}/register.jsp">立即注册</a>
        </div>
    </div>
</body>
</html>