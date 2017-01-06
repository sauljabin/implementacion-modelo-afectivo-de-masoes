/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JadeSettingsTest {

    private JadeSettings jadeSettings;

    @Before
    public void setUp() {
        jadeSettings = JadeSettings.getInstance();
        jadeSettings.load();
    }

    @Test
    public void shouldGetSameInstance() {
        JadeSettings actualJadeSettings = JadeSettings.getInstance();
        assertThat(actualJadeSettings, is(jadeSettings));
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