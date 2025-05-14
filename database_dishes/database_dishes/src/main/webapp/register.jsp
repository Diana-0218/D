<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户注册 - 智能食堂</title>
    <style>
        body { font-family: sans-serif; margin: 20px; background-color: #f4f4f4; }
        .container { max-width: 400px; margin: 50px auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #333; }
        label { display: block; margin-bottom: 5px; color: #555; }
        input[type="text"], input[type="password"], textarea { width: 95%; padding: 10px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 4px; }
        textarea { resize: vertical; }
        button { width: 100%; padding: 10px; background-color: #5cb85c; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
        button:hover { background-color: #4cae4c; }
        .error { color: red; margin-bottom: 15px; text-align: center; }
        .login-link { text-align: center; margin-top: 15px; }
        .login-link a { color: #007bff; text-decoration: none; }
        .login-link a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class="container">
        <h2>用户注册</h2>

        <%-- 显示错误信息 --%>
        <p class="error">${requestScope.error}</p>

        <form action="${pageContext.request.contextPath}/register" method="post">
            <div>
                <label for="username">用户名:</label>
                <input type="text" id="username" name="username" required value="${param.username}"> <%-- 保留用户输入 --%>
            </div>
            <div>
                <label for="password">密码:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div>
                <label for="confirmPassword">确认密码:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>
            <div>
                <label for="preference">口味偏好 (可选):</label>
                <textarea id="preference" name="preference" rows="3">${param.preference}</textarea> <%-- 保留用户输入 --%>
            </div>
            <button type="submit">注册</button>
        </form>

        <div class="login-link">
            已有账号？ <a href="${pageContext.request.contextPath}/login.jsp">立即登录</a>
        </div>
    </div>
</body>
</html>