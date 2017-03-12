/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class JadeSettingsTest {

    private JadeSettings jadeSettings;

    @Before
    public void setUp() {
        jadeSettings = JadeSettings.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        setFieldValue(jadeSettings, "INSTANCE", null);
    }

    @Test
    public void shouldGetSameInstance() {
        assertThat(JadeSettings.getInstance(), is(jadeSettings));
    }

    @Test
    public void shouldLoadInitValues() {
        Map<String, String> expectedValues = new HashMap<>();
        expectedValues.put("gui", "true");
        expectedValues.put("port", "1099");
        expectedValues.put("jade_mtp_http_port", "7778");
        expectedValues.put("jade_domain_df_autocleanup", "true");
        expectedValues.put("platform-id", "masoes");
        expectedValues.keySet().forEach(
                key -> assertThat(jadeSettings.get(key), is(expectedValues.get(key)))
        );
    }

}