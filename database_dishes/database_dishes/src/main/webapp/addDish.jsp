<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>添加新菜品 - 智能食堂</title>
    <style>
        body { font-family: sans-serif; margin: 20px; background-color: #f4f4f4; }
        .container { max-width: 600px; margin: 50px auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #333; }
        label { display: block; margin-bottom: 5px; color: #555; }
        input[type="text"], input[type="number"], select { width: 95%; padding: 10px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 4px; }
        input[type="checkbox"] { margin-bottom: 15px; }
        button { width: 100%; padding: 10px; background-color: #5cb85c; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
        button:hover { background-color: #4cae4c; }
        .error { color: red; margin-bottom: 15px; }
        .back-link { margin-top: 15px; display: inline-block; color: #007bff; text-decoration: none; }
        .back-link:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class="container">
        <h2>添加新菜品</h2>

        <%-- 显示错误信息 --%>
        <p class="error">${requestScope.error}</p>

        <form action="${pageContext.request.contextPath}/dishes" method="post">
            <input type="hidden" name="action" value="add">
            <div>
                <label for="dishName">菜品名称:</label>
                <input type="text" id="dishName" name="dishName" required value="${param.dishName}">
            </div>
            <div>
                <label for="isVegetarian">是否素食:</label>
                <input type="checkbox" id="isVegetarian" name="isVegetarian" value="true" ${param.isVegetarian == 'true' ? 'checked' : ''}>
                <input type="hidden" name="_isVegetarian" value="on"/> <%-- 处理未选中复选框的情况 --%>
            </div>
            <div>
                <label for="windowLocation">窗口位置:</label>
                <input type="text" id="windowLocation" name="windowLocation" value="${param.windowLocation}">
            </div>
            <div>
                <label for="price">价格 (元):</label>
                <input type="number" id="price" name="price" step="0.01" min="0" required value="${param.price}">
            </div>
            <%-- 平均评分通常不由管理员直接添加，而是通过用户评分计算 --%>
            <button type="submit">添加菜品</button>
        </form>

        <a href="${pageContext.request.contextPath}/dishes?action=list" class="back-link">&laquo; 返回菜品列表</a>
    </div>
</body>
</html>