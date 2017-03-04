/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DataBaseConnection {

    private static DataBaseConnection INSTANCE;
    private Statement statement;
    private Connection connection;

    private DataBaseConnection() {
    }

    public synchronized static DataBaseConnection getConnection() {
        if (INSTANCE == null) {
            INSTANCE = new DataBaseConnection();
        }
        return INSTANCE;
    }

    public synchronized void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            DataBaseSettings settings = DataBaseSettings.getInstance();
            String driver = settings.get(DataBaseSettings.DRIVER);
            String url = settings.get(DataBaseSettings.URL);
            Class.forName(driver);
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(true);
        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

    public synchronized void connect(boolean force) {
        if (force) {
            close();
        }
        connect();
    }

    public synchronized boolean execute(String sql) {
        closeStatement();
        try {
            statement = connection.createStatement();
            int affectedRows = statement.executeUpdate(sql);
            return affectedRows > 0;
        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

    public synchronized QueryResult query(String sql) {
        closeStatement();
        try {
            statement = connection.createStatement();
            return new QueryResult(statement.executeQuery(sql));
        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

    public synchronized void closeStatement() {
        try {
            if (statement != null) {
                statement.close();
                statement = null;
            }
        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

    public synchronized void close() {
        closeStatement();
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

}
