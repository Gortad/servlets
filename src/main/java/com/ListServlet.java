package com;

import product.Product;
import product.ProductManager;
import product.ProductMemoryManagerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        if (request.getSession().getAttribute("productManager") == null) {
            ProductManager productManager = new ProductMemoryManagerImpl();
            productManager.insertProduct(new Product("woda", 3, 0));
            productManager.insertProduct(new Product("cola", 6, 1));
            request.getSession().setAttribute("productManager", productManager);
        }
        ProductManager productManager = (ProductManager) request.getSession().getAttribute("productManager");
        List<Product> products = productManager.getProductsList();

        java.io.Writer writer = response.getWriter();
        writer.append("<html>");
        writer.append("<body>");
        writer.append("<table border='1' width='50%'> <tr> <th> produkt </th> <th> cena</th> <th>usuń</th>");
        for (Product product: products) {
            writer.append("<tr><th>");
            writer.append("<a href='edit/?productId=" + product.getPk() + "'>");
            writer.append(product.getName());
            writer.append("</a>");
            writer.append("</th><th>");
            writer.append("" + product.getPrice());
            writer.append("</th><th>");
            writer.append("<form action='edit/?action=delete_product' method='post'>");
            writer.append("<input type='hidden' name='productId' value='" + product.getPk() + "'> <br>");
            writer.append("<button type='submit' onclick='return confirm(\"Na pewno chcesz usunąć ten produkt?\")'>");
            writer.append("usuń");
            writer.append("</button>");
            writer.append("</form>");
            writer.append("</th></tr>");
        }
        writer.append("</table>");
        writer.append("<a href='edit/?productId=" + productManager.getNewPk() + "'>");
        writer.append("Dodaj nowy!");
        writer.append("</a>");
        writer.append("</body>");
        writer.append("</html>");
    }
}