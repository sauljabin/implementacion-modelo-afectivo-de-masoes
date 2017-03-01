package util.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseHelper {

    private static final String SQLITE_CLASS_NAME = "org.sqlite.JDBC";
    private static final String JDBC_SQLITE_MASOES_DB = "jdbc:sqlite:Data/Masoes";

    private Connection connection;
    private Statement statement;

    public void openConnection() throws DataBaseException{
        createConnection();
        createStatementObject();
    }

    public void closeConnection() throws DataBaseException {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e){
            throw new DataBaseException(e.getMessage(), false);
        }
    }

    public boolean performSimpleStatement(String sqlUpdateClause) throws DataBaseException {
        try{
            int affectedRows = statement.executeUpdate(sqlUpdateClause);
            return affectedRows > 0;
        } catch (SQLException ex) {
            throw new DataBaseException(ex.getMessage(), true);
        }
    }

    public ResultSet performRetrieveStatement(String sqlSelectClause) throws DataBaseException {
        try {
            return statement.executeQuery(sqlSelectClause);
        } catch (SQLException ex) {
            throw new DataBaseException(ex.getMessage(), true);
        }
    }

    private void createConnection() throws DataBaseException {
        try {
            Class.forName(SQLITE_CLASS_NAME);
            connection = DriverManager.getConnection(JDBC_SQLITE_MASOES_DB);
        } catch (SQLException | ClassNotFoundException e){
            throw new DataBaseException(e.getMessage(), false);
        }
    }

    private void createStatementObject() throws DataBaseException {
        try {
            statement = connection.createStatement();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), true);
        }
    }

}
