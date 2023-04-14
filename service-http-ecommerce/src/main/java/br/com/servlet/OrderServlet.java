package br.com.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.eclipse.jetty.servlet.Source;

import br.com.kafka.dispatcher.KafkaDispatcher;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OrderServlet extends HttpServlet {

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
        emailDispatcher.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var orderId = UUID.randomUUID().toString();
            var email = req.getParameter("email");
            var amount = new BigDecimal(req.getParameter("amount"));

            var order = new Order(orderId, amount, email);
            orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, order);

            var text = "Thank you for your order! We are processing you order.";
            emailDispatcher.send("ECOMMERCE_SEND_EMAIL", email, text);

            System.out.println("New order sent successfully");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("New order sent successfully.");
        } catch (ExecutionException e) {
            throw new ServletException(e);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }
}
