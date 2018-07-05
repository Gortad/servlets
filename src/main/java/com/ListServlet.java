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
    private int counter = 0;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("listSessionCounter") == null) {
            request.getSession().setAttribute("listSessionCounter", 0);
        }

        if (getServletContext().getAttribute("listContextCounter") == null) {
            getServletContext().setAttribute("listContextCounter", 0);
        }

        counter += 1;

        int sessionCounter = (int) request.getSession().getAttribute("listSessionCounter") + 1;
        int contextCounter = (int) getServletContext().getAttribute("listContextCounter") + 1;


        request.getSession().setAttribute("listSessionCounter", sessionCounter);
        getServletContext().setAttribute("listContextCounter", contextCounter);


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
        writer.append("licznik pola: ");
        writer.append(counter + "<br>");
        writer.append("licznik sesji: ");
        writer.append(sessionCounter + "<br>");
        writer.append("licznik kontekstu: ");
        writer.append(contextCounter + "<br>");
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
        writer.append("<form action='?action=logout' method='post'>");
        writer.append("<button type='submit'>");
        writer.append("Wyloguj");
        writer.append("</button>");
        writer.append("</form>");

        writer.append("</body>");
        writer.append("</html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}