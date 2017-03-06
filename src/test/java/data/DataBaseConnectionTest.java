/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package data;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.unitils.util.ReflectionUtils.setFieldValue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DataBaseConnection.class, DriverManager.class})
public class DataBaseConnectionTest {

    private static final String EXPECTED_URL = "expectedURL";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private DataBaseConnection dataBaseConnection;
    private Connection connectionMock;
    private Statement statementMock;

    @Before
    public void setUp() throws Exception {
        DataBaseSettings.getInstance().set(DataBaseSettings.URL, EXPECTED_URL);
        dataBaseConnection = DataBaseConnection.getConnection();
        connectionMock = mock(Connection.class);
        statementMock = mock(Statement.class);
        doReturn(statementMock).when(connectionMock).createStatement();

        mockStatic(DriverManager.class);
        when(DriverManager.getConnection(EXPECTED_URL)).thenReturn(connectionMock);

        dataBaseConnection.connect();
    }

    @After
    public void tearDown() throws Exception {
        setFieldValue(dataBaseConnection, "INSTANCE", null);
    }

    @Test
    public void shouldGetSameInstance() {
        assertThat(DataBaseConnection.getConnection(), is(dataBaseConnection));
    }

    @Test
    public void shouldInvokeDriveWhenConnect() throws Exception {
        verifyStatic();
        DriverManager.getConnection(EXPECTED_URL);
    }

    @Test
    public void shouldNotInvokeDriveWhenIsConnected() throws Exception {
        dataBaseConnection.connect();
        verifyStatic();
        DriverManager.getConnection(EXPECTED_URL);
    }

    @Test
    public void shouldCloseConnectionWhenUseForce() throws Exception {
        dataBaseConnection.connect(true);
        verify(connectionMock).close();
        verifyStatic();
        DriverManager.getConnection(EXPECTED_URL);
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
        dataBaseConnection.close();
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
        setFieldValue(dataBaseConnection, "connection", connectionMock);

        dataBaseConnection.execute("");

        InOrder inOrder = Mockito.inOrder(anotherStatementMock, statementMock);
        inOrder.verify(anotherStatementMock).close();
        inOrder.verify(statementMock).executeUpdate(anyString());
    }

    @Test
    public void shouldSetAutoCommitToFalseToBeginTransaction() throws Exception {
        dataBaseConnection.beginTransaction();
        verify(connectionMock).setAutoCommit(false);
    }

    @Test
    public void shouldThrowExceptionIfErrorWhileBeginningTransaction() throws Exception {
        expectedException.expect(DataBaseException.class);
        expectedException.expectCause(isA(SQLException.class));
        doThrow(new SQLException("ERROR")).when(connectionMock).setAutoCommit(anyBoolean());
        dataBaseConnection.beginTransaction();
    }

    @Test
    public void shouldInvokeCommitToEndTransactionAndSetAutoCommitToTrue() throws Exception {
        dataBaseConnection.endTransaction();
        verify(connectionMock).commit();
        verify(connectionMock).setAutoCommit(true);
    }

    @Test
    public void shouldThrowExceptionIfErrorWhileEndingTransaction() throws Exception {
        expectedException.expect(DataBaseException.class);
        expectedException.expectCause(isA(SQLException.class));
        doThrow(new SQLException("ERROR")).when(connectionMock).commit();
        dataBaseConnection.endTransaction();
    }

    @Test
    public void shouldInvokeRollbackToRollbackTransactionAndSetAutoCommitToTrue() throws Exception {
        dataBaseConnection.rollbackTransaction();
        verify(connectionMock).rollback();
        verify(connectionMock).setAutoCommit(true);
    }

    @Test
    public void shouldThrowExceptionIfErrorWhileRollbackTransaction() throws Exception {
        expectedException.expect(DataBaseException.class);
        expectedException.expectCause(isA(SQLException.class));
        doThrow(new SQLException("ERROR")).when(connectionMock).rollback();
        dataBaseConnection.rollbackTransaction();
    }

}