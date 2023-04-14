package br.com.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import br.com.kafka.consumer.KafkaService;

public class CreateUserService {

    private final Connection connection;

    CreateUserService() throws SQLException {
        final String url = "jdbc:sqlite:users_database.db";
        connection = DriverManager.getConnection(url);
        try {
            connection.createStatement().execute("create table User("
                    + "uuid varchar(200) primary key,"
                    + "email varchar(200))");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        var createUserService = new CreateUserService();
        try (var service = new KafkaService(CreateUserService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                createUserService::parse,
                Order.class,
                Map.of())) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) throws SQLException {
        System.out.println("-----------------------------------------");
        System.out.println("Processing new order, checking for new user.");
        System.out.println(record.value());

        var order = record.value();
        if (!hasUser(order.getEmail())) {
            insertUser(order.getEmail());
        }
    }

    private void insertUser(String email) throws SQLException {

        var insert = connection.prepareStatement("insert into User(uuid, email) values (?,?)");
        insert.setString(1, UUID.randomUUID().toString());
        insert.setString(2, email);
        insert.execute();

        System.out.println("Created new user with email " + email);
    }

    private boolean hasUser(String email) throws SQLException {

        var statement = connection.prepareStatement("select uuid from User where email = ? limit 1");
        statement.setString(1, email);
        var result = statement.executeQuery();
        return result.next();
    }

}
