package me.smegasberla.exstension.api.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DatabaseManager {

    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final int port;
    private Connection connection;

    public DatabaseManager(String host, String database, String username, String password, int port) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public DatabaseManager(String host, String database, String username, String password) {
        this(host, database, username, password, 3306);
    }

    public void connect() throws SQLException {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        connection = DriverManager.getConnection(url, username, password);
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void execute(String sql, Object... params) throws SQLException {
        if (!isConnected()) {
            connect();
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            statement.executeUpdate();
        }
    }

    public <T> List<T> query(String sql, Function<ResultSet, T> mapper, Object... params) throws SQLException {
        if (!isConnected()) {
            connect();
        }

        List<T> results = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(mapper.apply(resultSet));
                }
            }
        }

        return results;
    }

    public void executeAsync(String sql, Consumer<Boolean> callback, Object... params) {
        new Thread(() -> {
            try {
                execute(sql, params);
                if (callback != null) {
                    callback.accept(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.accept(false);
                }
            }
        }).start();
    }
}
