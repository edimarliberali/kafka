package br.com.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import br.com.servlet.OrderServlet;

public class HttpEcommerceService {

    public static void main(String[] args) throws Exception {
        final var server = new Server(8081);

        final var context = new ServletContextHandler();
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new OrderServlet()), "/order");

        server.setHandler(context);
        server.start();
        server.join();
    }

}
