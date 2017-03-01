/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBaseConnection {

    private static DataBaseConnection INSTANCE;
    private Statement statement;
    private Connection connection;

    public synchronized static DataBaseConnection getConnection() {
        if (INSTANCE == null) {
            INSTANCE = new DataBaseConnection();
        }
        return INSTANCE;
    }

    private DataBaseConnection() {
        try {
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

    public synchronized boolean execute(String sql) throws DataBaseException {
        closeStatement();
        try {
            statement = connection.createStatement();
            int affectedRows = statement.executeUpdate(sql);
            return affectedRows > 0;
        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

    public synchronized ResultSet query(String sql) throws DataBaseException {
        closeStatement();
        try {
            statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

    public synchronized void closeStatement() {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (Exception e) {
            throw new DataBaseException(e);
        }
    }

    public synchronized void closeConnection() {
        closeStatement();
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            throw new DataBaseException(e);
        }
        INSTANCE = null;
    }

}
