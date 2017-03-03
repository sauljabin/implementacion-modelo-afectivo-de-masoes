/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.PhoenixDatabase;

import java.sql.ResultSet;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DataBaseConnectionTest {

    private DataBaseConnection dataBaseConnection;

    @Before
    public void setUp() {
        dataBaseConnection = PhoenixDatabase.create();
        dataBaseConnection.execute("create table test (name varchar(10))");
    }

    @After
    public void tearDown() {
        PhoenixDatabase.destroy();
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