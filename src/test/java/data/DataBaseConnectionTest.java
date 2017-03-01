/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DataBaseConnectionTest {

    private DataBaseConnection dataBaseConnection;

    @Before
    public void setUp() {
        DataBaseSettings.getInstance().set(DataBaseSettings.URL, "jdbc:sqlite:data/test.sqlite3");
        dataBaseConnection = DataBaseConnection.getConnection();
        dataBaseConnection.execute("create table test (name varchar(10))");
    }

    @After
    public void tearDown() {
        dataBaseConnection.execute("drop table test");
        dataBaseConnection.closeConnection();
    }

    @Test
    public void shouldInsertAndGetValues() throws Exception {
        dataBaseConnection.execute("insert into test (name) values ('first')");
        ResultSet query = dataBaseConnection.query("select * from test");
        assertTrue(query.next());
        assertThat(query.getString("name"), is("first"));
    }

    @Test
    public void shouldGetSameInstance() {
        assertThat(DataBaseConnection.getConnection(), is(dataBaseConnection));
    }

}