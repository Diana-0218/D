<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>编辑菜品 - 智能食堂</title>
    <style>
        body { font-family: sans-serif; margin: 20px; background-color: #f4f4f4; }
        .container { max-width: 600px; margin: 50px auto; padding: 20px; background-color: #fff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #333; }
        label { display: block; margin-bottom: 5px; color: #555; }
        input[type="text"], input[type="number"], select { width: 95%; padding: 10px; margin-bottom: 15px; border: 1px solid #ddd; border-radius: 4px; }
        input[type="checkbox"] { margin-bottom: 15px; }
        input[readonly] { background-color: #eee; cursor: not-allowed; }
        button { width: 48%; padding: 10px; background-color: #5cb85c; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; margin-top: 10px; }
        button:hover { background-color: #4cae4c; }
        button.cancel { background-color: #aaa; margin-left: 4%; }
        button.cancel:hover { background-color: #888; }
        .error { color: red; margin-bottom: 15px; }
        .back-link { margin-top: 15px; display: inline-block; color: #007bff; text-decoration: none; }
        .back-link:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class="container">
        <h2>编辑菜品信息</h2>

        <%-- 显示错误信息 --%>
        <p class="error">${requestScope.error}</p>

        <c:if test="${not empty requestScope.dish}">
            <form action="${pageContext.request.contextPath}/dishes" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="dishId" value="${dish.dishId}">
                
                <div>
                    <label for="dishId">菜品ID:</label>
                    <input type="text" id="dishId" name="dishIdDisplay" value="${dish.dishId}" readonly>
                </div>
                <div>
                    <label for="dishName">菜品名称:</label>
                    <input type="text" id="dishName" name="dishName" required value="<c:out value='${dish.dishName}'/>">
                </div>
                <div>
                    <label for="isVegetarian">是否素食:</label>
                    <input type="checkbox" id="isVegetarian" name="isVegetarian" value="true" ${dish.vegetarian ? 'checked' : ''}>
                     <input type="hidden" name="_isVegetarian" value="on"/> <%-- 处理未选中复选框的情况 --%>
               </div>
                <div>
                    <label for="windowLocation">窗口位置:</label>
                    <input type="text" id="windowLocation" name="windowLocation" value="<c:out value='${dish.windowLocation}'/>">
                </div>
                <div>
                    <label for="price">价格 (元):</label>
                    <input type="number" id="price" name="price" step="0.01" min="0" required value="${dish.price}">
                </div>
                 <div>
                    <label for="averageRating">平均评分:</label>
                    <input type="text" id="averageRating" name="averageRatingDisplay" value="<fmt:formatNumber value='${dish.averageRating}' type='number' minFractionDigits='1' maxFractionDigits='2'/>" readonly>
                    <%-- 通常评分不由编辑表单修改 --%>
                </div>
                
                <button type="submit">更新菜品</button>
                <button type="button" class="cancel" onclick="window.location.href='${pageContext.request.contextPath}/dishes?action=list'">取消</button>
            </form>
        </c:if>
        <c:if test="${empty requestScope.dish}">
            <p>找不到要编辑的菜品信息。</p>
        </c:if>

        <a href="${pageContext.request.contextPath}/dishes?action=list" class="back-link">&laquo; 返回菜品列表</a>
    </div>
</body>
</html>