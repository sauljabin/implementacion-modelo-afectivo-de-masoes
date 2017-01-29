/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import logger.writer.ApplicationLogger;
import org.junit.Before;
import org.junit.Test;
import settings.loader.JadeSettings;

import java.util.Properties;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class JadeOptionTest {

    private static final String GUI = "gui";
    private static final String GUI_VALUE = "false";
    private static final String PORT = "port";
    private static final String PORT_VALUE = "1099";
    private JadeOption jadeOption;
    private JadeSettings jadeSettingsMock;
    private ApplicationLogger loggerMock;
    private Properties properties;

    @Before
    public void setUp() throws Exception {
        jadeSettingsMock = mock(JadeSettings.class);
        loggerMock = mock(ApplicationLogger.class);

        jadeOption = new JadeOption();
        setFieldValue(jadeOption, "logger", loggerMock);
        setFieldValue(jadeOption, "jadeSettings", jadeSettingsMock);

        properties = new Properties();
        properties.put(GUI, GUI_VALUE);
        properties.put(PORT, PORT_VALUE);
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(jadeOption.getOpt(), is("J"));
        assertThat(jadeOption.getLongOpt(), is(nullValue()));
        assertThat(jadeOption.getDescription(), containsString("Sets JADE settings"));
        assertThat(jadeOption.getArgType(), is(ArgumentType.UNLIMITED_ARGS));
        assertThat(jadeOption.getOrder(), is(40));
        assertFalse(jadeOption.isFinalOption());
    }

    @Test
    public void shouldSeJadeSettings() {
        jadeOption.setProperties(properties);
        jadeOption.exec();
        verify(jadeSettingsMock).set(GUI, GUI_VALUE);
        verify(jadeSettingsMock).set(PORT, PORT_VALUE);
        verify(loggerMock).updatedSettings();
    }

}