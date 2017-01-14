/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.settings;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class ApplicationSettingsTest {

    private static final String APP_NAME = "app.name";
    private static final String APP_REVISION = "app.revision";
    private static final String APP_VERSION = "app.version";
    private static final String OS_NAME = "os.name";
    private static final String OS_ARCH = "os.arch";
    private static final String OS_VERSION = "os.version";
    private static final String JAVA_VERSION = "java.version";
    private static final String JAVA_VENDOR = "java.vendor";
    private static final String JADE_VERSION = "jade.version";
    private static final String JADE_REVISION = "jade.revision";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ApplicationSettings applicationSettings;

    @Before
    public void setUp() {
        applicationSettings = ApplicationSettings.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        setFieldValue(applicationSettings, "INSTANCE", null);
    }

    @Test
    public void shouldGetSameInstance() {
        ApplicationSettings expectedApplicationSettings = ApplicationSettings.getInstance();
        assertThat(applicationSettings, is(expectedApplicationSettings));
    }

    @Test
    public void shouldLoadInitValues() {
        Map<String, String> expectedValues = new HashMap<>();
        expectedValues.put(OS_NAME, System.getProperty(OS_NAME));
        expectedValues.put(OS_ARCH, System.getProperty(OS_ARCH));
        expectedValues.put(OS_VERSION, System.getProperty(OS_VERSION));
        expectedValues.put(JAVA_VERSION, System.getProperty(JAVA_VERSION));
        expectedValues.put(JAVA_VENDOR, System.getProperty(JAVA_VENDOR));
        expectedValues.put(JADE_VERSION, jade.core.Runtime.getVersion());
        expectedValues.put(JADE_REVISION, jade.core.Runtime.getRevision());

        expectedValues.keySet().forEach(
                key -> assertThat(applicationSettings.get(key), is(expectedValues.get(key)))
        );

        assertThat(applicationSettings.get(APP_NAME), is(notNullValue()));
        assertThat(applicationSettings.get(APP_REVISION), is(notNullValue()));
        assertThat(applicationSettings.get(APP_VERSION), is(notNullValue()));
    }

}
