package com;

import product.Product;
import product.ProductManager;
import product.ProductMemoryManagerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EditServlet extends HttpServlet {
    private ProductManager handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        return (ProductManager) request.getSession().getAttribute("productManager");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getParameter("productId") == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        ProductManager productManager = handle(request,response);
        Integer pk = Integer.valueOf(request.getParameter("productId"));
        Product product = productManager.getProductByPK(pk);
        if (product == null) {
            product = new Product("", 0, pk);
        }

        java.io.Writer writer = response.getWriter();
        writer.append("<html><body>");
        writer.append("<form action='?action=save_product' method='post'>");
        writer.append("Produkt: <input type='text' name='name' value='" + product.getName() + "'> <br>");
        writer.append("Cena: <input type='number' name='price' value='" + product.getPrice() + "'> <br>");
        writer.append("<input type='hidden' name='productId' value='" + product.getPk() + "'> <br>");
        writer.append("<input type='submit' value='Zapisz'>");
        writer.append("</form>");
        writer.append("<a href='../list'>Anuluj</a>");
        writer.append("</html></body>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProductManager productManager = handle(request,response);
        String action = request.getParameter("action");
        if (action == null || (!action.equals("save_product") && !action.equals("delete_product"))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Integer pk = Integer.valueOf(request.getParameter("productId"));

        if (action.equals("delete_product")) {
            productManager.deleteProduct(productManager.getProductByPK(pk));
        } else {
            String name = request.getParameter("name");
            Integer price = Integer.valueOf(request.getParameter("price"));
            Product product = new Product(name, price, pk);
            if (productManager.getProductsList().size() > pk) {
                productManager.updateProduct(product);
            } else {
                productManager.insertProduct(product);
            }
        }
        request.getSession().setAttribute("productManager", productManager);
        response.sendRedirect("../list");
    }
}