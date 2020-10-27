package ru.fomin.battleship.server.core;

import java.sql.*;

public class SQLClient {
    private static Connection connection;
    private static Statement statement;

    synchronized static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:server.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    synchronized static String getNickname(String login, String password) {
        String query = String.format("select nickname from clients where login = '%s' and password = '%s'",
                login, password);

        try (ResultSet set = statement.executeQuery(query)) {
            if (set.next())
                return set.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    synchronized static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean setLoginData(String login, String password, String nickName) throws SQLException {
        String query = String.format("select login from clients where login = '%s'",
                login);
        ResultSet set = statement.executeQuery(query);
        if (set.next()) {
            return false;
        } else {
            query = String.format("insert into clients (login, password, nickname) values ('%s','%s','%s')",
                    login, password, nickName);
            statement.execute(query);
            return true;
        }
    }
}
