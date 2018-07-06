package com;

import product.Product;
import product.ProductManager;
import product.ProductMemoryManagerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Obsługuje ekran edycji produktu.
 *
 * @author Ryszard Poklewski-Koziełł
 */

public class EditServlet extends HttpServlet {
    private int counter = 0;

    private ProductManager handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
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


        if (request.getSession().getAttribute("editSessionCounter") == null) {
            request.getSession().setAttribute("editSessionCounter", 0);
        }

        if (getServletContext().getAttribute("editContextCounter") == null) {
            getServletContext().setAttribute("editContextCounter", 0);
        }

        counter += 1;

        int sessionCounter = (int) request.getSession().getAttribute("editSessionCounter") + 1;
        int contextCounter = (int) getServletContext().getAttribute("editContextCounter") + 1;

        request.getSession().setAttribute("editSessionCounter", sessionCounter);
        getServletContext().setAttribute("editContextCounter", contextCounter);

        ProductManager productManager = handle(request,response);
        Integer pk = Integer.valueOf(request.getParameter("productId"));
        Product product = productManager.getProductByPK(pk);
        if (product == null) {
            product = new Product("", new BigDecimal("0.0"), pk);
        }

        java.io.Writer writer = response.getWriter();
        writer.append("<html><body>");
        writer.append("licznik pola: ");
        writer.append(counter + "<br>");
        writer.append("licznik sesji: ");
        writer.append(sessionCounter + "<br>");
        writer.append("licznik kontekstu: ");
        writer.append(contextCounter + "<br>");
        if (request.getParameter("error") != null) {
            writer.append(request.getParameter("error"));
        }
        writer.append("<form action='?action=save_product' method='post'>");
        writer.append("Produkt: <input type='text' name='name' value='" + product.getName() + "'> <br>");
        writer.append("Cena: <input type='number' min='0' step='0.01' name='price' value='" + product.getPrice() + "'> <br>");
        writer.append("<input type='hidden' name='productId' value='" + product.getPk() + "'> <br>");
        writer.append("<input type='submit' value='Zapisz'>");
        writer.append("</form>");
        writer.append("<a href='../list'>Anuluj</a>");
        writer.append("<form action='?action=logout' method='post'>");
        writer.append("<button type='submit'>");
        writer.append("Wyloguj");
        writer.append("</button>");
        writer.append("</form>");
        writer.append("</body></html>");
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
            if ((request.getParameter("name")).equals("") || (request.getParameter("price")).equals("")) {
                String errorMsg = "Bledy:<br>";
                if ((request.getParameter("name")).equals("")) {
                    errorMsg += "nazwa nie moze byc pusta<br>";
                }
                if ((request.getParameter("price")).equals("")) {
                    errorMsg += "cena nie moze byc pusta<br>";
                }
                DecimalFormat decimalFormat = new DecimalFormat(request.getParameter("price"));
                if (decimalFormat.isParseBigDecimal()) {
                    errorMsg += "cena musi być liczbą <br>";
                }

                response.sendRedirect("../edit/?productId=" + pk + "&" + "error=" + errorMsg);
                return;
            }
            String name = request.getParameter("name");
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            Product product = new Product(name, price, pk);
            if (productManager.getNewPk() > pk) {
                productManager.updateProduct(product);
            } else {
                productManager.insertProduct(product);
            }
        }
        request.getSession().setAttribute("productManager", productManager);
        response.sendRedirect("../list");
    }
}