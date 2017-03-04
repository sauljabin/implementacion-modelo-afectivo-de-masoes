/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class DataBaseConnectionTest {

    private static final String DB_FILE = "data/test.sqlite3";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE;
    private DataBaseConnection dataBaseConnection;
    private Connection connectionMock;
    private Statement statementMock;

    @Before
    public void setUp() throws Exception {
        DataBaseSettings.getInstance().set(DataBaseSettings.URL, DB_URL);
        dataBaseConnection = DataBaseConnection.getConnection();
        connectionMock = mock(Connection.class);
        setFieldValue(dataBaseConnection, "connection", connectionMock);

        statementMock = mock(Statement.class);
        doReturn(statementMock).when(connectionMock).createStatement();
    }

    @After
    public void tearDown() throws Exception {
        File file = new File(DB_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void shouldGetSameInstance() {
        assertThat(DataBaseConnection.getConnection(), is(dataBaseConnection));
    }

    @Test
    public void shouldGetAnotherInstance() {
        assertThat(DataBaseConnection.getConnection(true), is(not(dataBaseConnection)));
    }

    @Test
    public void shouldGetNewConnectionWhenClose() throws Exception {
        dataBaseConnection.closeConnection();
        assertThat(DataBaseConnection.getConnection(), is(not(dataBaseConnection)));
    }

    @Test
    public void shouldInvokeQuery() throws Exception {
        ResultSet resultSetMock = mock(ResultSet.class);
        doReturn(resultSetMock).when(statementMock).executeQuery(anyString());
        String expectedSql = "expectedSql";
        QueryResult actualResultSet = dataBaseConnection.query(expectedSql);
        verify(connectionMock).createStatement();
        verify(statementMock).executeQuery(expectedSql);
        assertThat(actualResultSet.getResultSet(), is(resultSetMock));
    }

    @Test
    public void shouldInvokeExecute() throws Exception {
        String expectedSql = "expectedSql";
        dataBaseConnection.execute(expectedSql);
        verify(connectionMock).createStatement();
        verify(statementMock).executeUpdate(expectedSql);
    }

    @Test
    public void shouldReturnTrueWhenExecuteReturn1() throws Exception {
        doReturn(1).when(statementMock).executeUpdate(anyString());
        assertTrue(dataBaseConnection.execute(""));
    }

    @Test
    public void shouldReturnFalseWhenExecuteReturn0() throws Exception {
        doReturn(0).when(statementMock).executeUpdate(anyString());
        assertFalse(dataBaseConnection.execute(""));
    }

    @Test
    public void shouldCloseConnection() throws Exception {
        setFieldValue(dataBaseConnection, "statement", statementMock);
        dataBaseConnection.closeConnection();
        verify(connectionMock).close();
        verify(statementMock).close();
    }

    @Test
    public void shouldCloseStatement() throws Exception {
        setFieldValue(dataBaseConnection, "statement", statementMock);
        dataBaseConnection.closeStatement();
        verify(statementMock).close();
    }

    @Test
    public void shouldCloseStatementInQueryBeforeInvoke() throws Exception {
        Statement anotherStatementMock = mock(Statement.class);
        setFieldValue(dataBaseConnection, "statement", anotherStatementMock);

        dataBaseConnection.query("");

        InOrder inOrder = Mockito.inOrder(anotherStatementMock, statementMock);
        inOrder.verify(anotherStatementMock).close();
        inOrder.verify(statementMock).executeQuery(anyString());
    }

    @Test
    public void shouldCloseStatementInExecuteBeforeInvoke() throws Exception {
        Statement anotherStatementMock = mock(Statement.class);
        setFieldValue(dataBaseConnection, "statement", anotherStatementMock);

        dataBaseConnection.execute("");

        InOrder inOrder = Mockito.inOrder(anotherStatementMock, statementMock);
        inOrder.verify(anotherStatementMock).close();
        inOrder.verify(statementMock).executeUpdate(anyString());
    }

}