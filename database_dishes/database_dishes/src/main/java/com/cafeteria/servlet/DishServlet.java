package com.cafeteria.servlet;

import com.cafeteria.dao.DishDAO;
import com.cafeteria.model.Dish;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import com.google.gson.Gson;

/**
 * 处理菜品相关请求的Servlet
 */
@WebServlet("/dishes")
public class DishServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DishDAO dishDAO;

    @Override
    public void init() throws ServletException {
        dishDAO = new DishDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list"; // 默认动作是列出所有菜品
        }

        switch (action) {
            case "list":
                // 检查是否请求JSON格式
                String format = request.getParameter("format");
                if ("json".equals(format)) {
                    listDishesAsJson(request, response);
                } else {
                    listDishes(request, response);
                }
                break;
            case "showAddForm":
                showAddForm(request, response);
                break;
            case "showEditForm":
                showEditForm(request, response);
                break;
            case "delete":
                deleteDish(request, response);
                break;
            default:
                listDishes(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            // 如果没有指定action，可能是一个错误或者直接提交表单，可以重定向到列表页
            response.sendRedirect(request.getContextPath() + "/dishes");
            return;
        }

        switch (action) {
            case "add":
                addDish(request, response);
                break;
            case "update":
                updateDish(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/dishes");
                break;
        }
    }

    private void listDishes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Dish> dishes = dishDAO.getAllDishes();
        request.setAttribute("dishes", dishes);
        request.getRequestDispatcher("dishes.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("addDish.jsp").forward(request, response);
    }

     private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int dishId = Integer.parseInt(request.getParameter("dishId"));
            Dish existingDish = dishDAO.findDishById(dishId);
            if (existingDish != null) {
                request.setAttribute("dish", existingDish);
                request.getRequestDispatcher("editDish.jsp").forward(request, response);
            } else {
                // 如果找不到菜品，可以显示错误信息或重定向
                request.setAttribute("error", "找不到要编辑的菜品");
                listDishes(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "无效的菜品ID");
            listDishes(request, response);
        }
    }

    private void addDish(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String dishName = request.getParameter("dishName");
            boolean isVegetarian = Boolean.parseBoolean(request.getParameter("isVegetarian"));
            String windowLocation = request.getParameter("windowLocation");
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            // 新增菜品时，平均评分可以设为默认值，例如0
            BigDecimal averageRating = BigDecimal.ZERO;

            Dish newDish = new Dish(dishName, isVegetarian, windowLocation, price, averageRating);
            boolean success = dishDAO.addDish(newDish);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/dishes?action=list&success=add");
            } else {
                request.setAttribute("error", "添加菜品失败");
                showAddForm(request, response); // 返回添加表单并显示错误
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "价格格式无效");
            showAddForm(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "添加菜品时发生错误: " + e.getMessage());
            showAddForm(request, response);
        }
    }

    private void updateDish(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int dishId = Integer.parseInt(request.getParameter("dishId"));
            String dishName = request.getParameter("dishName");
            boolean isVegetarian = Boolean.parseBoolean(request.getParameter("isVegetarian"));
            String windowLocation = request.getParameter("windowLocation");
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            // 更新时通常不直接更新平均评分，评分应由用户评价系统计算
            // 如果需要允许管理员修改，则从请求获取，否则从数据库读取现有值
            Dish existingDish = dishDAO.findDishById(dishId);
            if (existingDish == null) {
                 request.setAttribute("error", "找不到要更新的菜品");
                 listDishes(request, response);
                 return;
            }
            BigDecimal averageRating = existingDish.getAverageRating(); // 保留现有评分

            Dish dishToUpdate = new Dish(dishId, dishName, isVegetarian, windowLocation, price, averageRating);
            boolean success = dishDAO.updateDish(dishToUpdate);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/dishes?action=list&success=update");
            } else {
                request.setAttribute("error", "更新菜品失败");
                request.setAttribute("dish", dishToUpdate); // 将尝试更新的数据传回编辑页面
                request.getRequestDispatcher("editDish.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID或价格格式无效");
            listDishes(request, response); // 或者重定向回编辑页并显示错误
        } catch (Exception e) {
            request.setAttribute("error", "更新菜品时发生错误: " + e.getMessage());
            listDishes(request, response);
        }
    }

    private void deleteDish(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
            int dishId = Integer.parseInt(request.getParameter("dishId"));
            boolean success = dishDAO.deleteDish(dishId);
            if (success) {
                 response.sendRedirect(request.getContextPath() + "/dishes?action=list&success=delete");
            } else {
                request.setAttribute("error", "删除菜品失败");
                listDishes(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "无效的菜品ID");
            listDishes(request, response);
        } catch (Exception e) {
             request.setAttribute("error", "删除菜品时发生错误: " + e.getMessage());
             listDishes(request, response);
        }
    }
    
    /**
     * 以JSON格式返回菜品列表数据
     * 用于前端JavaScript通过AJAX请求获取菜品数据
     */
    private void listDishesAsJson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Dish> dishes = dishDAO.getAllDishes();
        
        // 设置响应类型为JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // 使用Gson将菜品列表转换为JSON字符串
        Gson gson = new Gson();
        String jsonData = gson.toJson(dishes);
        
        // 将JSON数据写入响应
        PrintWriter out = response.getWriter();
        out.print(jsonData);
        out.flush();
    }
}