/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import logger.application.ApplicationLogger;
import org.junit.Before;
import org.junit.Test;
import settings.jade.JadeSettings;

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

    private JadeOption jadeOption;
    private JadeSettings jadeSettingsMock;
    private ApplicationLogger loggerMock;

    @Before
    public void setUp() throws Exception {
        jadeSettingsMock = mock(JadeSettings.class);
        loggerMock = mock(ApplicationLogger.class);
        jadeOption = new JadeOption();
        setFieldValue(jadeOption, "logger", loggerMock);
        setFieldValue(jadeOption, "jadeSettings", jadeSettingsMock);
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
    public void shouldSetSettings() {
        String expectedKey1 = "expectedKey1";
        String expectedValue1 = "value1";
        String expectedKey2 = "expectedKey2";
        String expectedValue2 = "value2";

        Properties expectedProperties = new Properties();
        expectedProperties.put(expectedKey1, expectedValue1);
        expectedProperties.put(expectedKey2, expectedValue2);

        jadeOption.setProperties(expectedProperties);
        jadeOption.exec();

        verify(jadeSettingsMock).set(expectedKey1, expectedValue1);
        verify(jadeSettingsMock).set(expectedKey2, expectedValue2);
        verify(loggerMock).updatedSettings();
    }

}