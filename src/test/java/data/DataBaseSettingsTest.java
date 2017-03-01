/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class DataBaseSettingsTest {

    private DataBaseSettings dataBaseSettings;

    @Before
    public void setUp() {
        dataBaseSettings = DataBaseSettings.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        setFieldValue(dataBaseSettings, "INSTANCE", null);
    }

    @Test
    public void shouldGetSameInstance() {
        assertThat(DataBaseSettings.getInstance(), is(dataBaseSettings));
    }

    @Test
    public void shouldLoadInitValues() {
        Map<String, String> expectedValues = new HashMap<>();
        expectedValues.put("driver", "org.sqlite.JDBC");
        expectedValues.put("url", "jdbc:sqlite:data/masoes.sqlite3");
        expectedValues.keySet().forEach(
                key -> assertThat(dataBaseSettings.get(key), is(expectedValues.get(key)))
        );
    }

}