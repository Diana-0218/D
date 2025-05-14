<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>菜品列表 - 智能食堂</title>
    <style>
        body { font-family: sans-serif; margin: 20px; background-color: #f4f4f4; }
        .container { max-width: 900px; margin: 20px auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #333; margin-bottom: 20px; }
        table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background-color: #f2f2f2; color: #333; }
        tr:nth-child(even) { background-color: #f9f9f9; }
        .actions a, .add-link a { margin-right: 8px; color: #007bff; text-decoration: none; padding: 5px 8px; border: 1px solid #007bff; border-radius: 4px; }
        .actions a:hover, .add-link a:hover { background-color: #007bff; color: white; text-decoration: none; }
        .actions a.delete { color: #dc3545; border-color: #dc3545; }
        .actions a.delete:hover { background-color: #dc3545; color: white; }
        .add-link { margin-bottom: 20px; text-align: right; }
        .message { padding: 10px; margin-bottom: 15px; border-radius: 4px; text-align: center; }
        .success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .user-info { text-align: right; margin-bottom: 10px; color: #555; }
        .user-info a { color: #dc3545; text-decoration: none; margin-left: 10px; }
        .user-info a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class="container">
        <h2>菜品列表</h2>

        <%-- 显示用户信息和登出链接 --%>
        <div class="user-info">
            <c:if test="${not empty sessionScope.user}">
                欢迎, ${sessionScope.user.username}! 
                <a href="${pageContext.request.contextPath}/logout">登出</a> <%-- 假设有 LogoutServlet --%>
            </c:if>
            <c:if test="${empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/login.jsp">登录</a>
            </c:if>
        </div>

        <%-- 显示操作结果消息 --%>
        <c:if test="${param.success == 'add'}">
            <p class="message success">菜品添加成功！</p>
        </c:if>
        <c:if test="${param.success == 'update'}">
            <p class="message success">菜品更新成功！</p>
        </c:if>
        <c:if test="${param.success == 'delete'}">
            <p class="message success">菜品删除成功！</p>
        </c:if>
        <c:if test="${not empty requestScope.error}">
            <p class="message error">${requestScope.error}</p>
        </c:if>

        <%-- 添加菜品链接 (可以考虑权限控制) --%>
        <div class="add-link">
            <a href="${pageContext.request.contextPath}/dishes?action=showAddForm">添加新菜品</a>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>名称</th>
                    <th>素食</th>
                    <th>窗口</th>
                    <th>价格</th>
                    <th>评分</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="dish" items="${requestScope.dishes}">
                    <tr>
                        <td>${dish.dishId}</td>
                        <td><c:out value="${dish.dishName}"/></td>
                        <td>${dish.vegetarian ? '是' : '否'}</td>
                        <td><c:out value="${dish.windowLocation}"/></td>
                        <td><fmt:formatNumber value="${dish.price}" type="currency" currencySymbol="￥"/></td>
                        <td><fmt:formatNumber value="${dish.averageRating}" type="number" minFractionDigits="1" maxFractionDigits="2"/></td>
                        <td class="actions">
                            <a href="${pageContext.request.contextPath}/dishes?action=showEditForm&dishId=${dish.dishId}">编辑</a>
                            <a href="${pageContext.request.contextPath}/dishes?action=delete&dishId=${dish.dishId}" 
                               onclick="return confirm('确定要删除菜品 \'${dish.dishName}\' 吗？');" class="delete">删除</a>
                            <%-- 可以添加查看详情、评分等链接 --%>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty requestScope.dishes}">
                    <tr>
                        <td colspan="7" style="text-align: center;">暂无菜品信息</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</body>
</html>