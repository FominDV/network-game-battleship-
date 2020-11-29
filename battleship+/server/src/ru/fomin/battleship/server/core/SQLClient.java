package ru.fomin.battleship.server.core;

import java.sql.*;
import java.util.Vector;

public class SQLClient {
    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement insertClientData;

    synchronized static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:server.db");
            statement = connection.createStatement();
            insertClientData = connection.prepareStatement("insert into clients (login, password, nickname) values (?,?,?)");
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

    public synchronized static boolean setClientData(String login, String password, String nickName) throws SQLException {
        String query = String.format("select login from clients where login = '%s'", login);
        ResultSet set = statement.executeQuery(query);
        if (set.next()) {
            return false;
        } else {
            insertClientData.setString(1, login);
            insertClientData.setString(2, password);
            insertClientData.setString(3, nickName);
            insertClientData.executeUpdate();
            return true;
        }
    }

    public static Vector<String> getDataMap(String login) {
        Vector<String> dataMap = new Vector<>();
        String query = String.format("select name,data from data_map where login = '%s'", login);
        try (ResultSet set = statement.executeQuery(query)) {
            while (set.next()) {
                dataMap.add(set.getString(1));
                dataMap.add(set.getString(2));
            }
            return dataMap;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isValidNameForSave(String name, String login) {
        String query = String.format("select data from data_map where name = '%s' and login = '%s'", name, login);
        try {
            ResultSet set = statement.executeQuery(query);
            if (set.next()) return false;
            else return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }

    }

    public static boolean setNewDataMap(String[] arr) {
        String query = String.format("insert into data_map (login, name, data) values ('%s','%s','%s')", arr[1], arr[2], arr[3]);
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public static void removeData(String login, String name) {
        String query = String.format("DELETE FROM data_map WHERE login = '%s' AND name = '%s'", login, name);
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
