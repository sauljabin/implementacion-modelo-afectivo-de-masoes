/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ApplicationLogger;
import application.ArgumentType;
import masoes.MasoesSettings;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class MasoesOptionTest {

    private MasoesOption masoesOption;
    private MasoesSettings masoesSettingsMock;
    private ApplicationLogger loggerMock;

    @Before
    public void setUp() throws Exception {
        masoesSettingsMock = mock(MasoesSettings.class);
        loggerMock = mock(ApplicationLogger.class);

        masoesOption = new MasoesOption();
        setFieldValue(masoesOption, "logger", loggerMock);
        setFieldValue(masoesOption, "masoesSettings", masoesSettingsMock);
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(masoesOption.getOpt(), is("M"));
        assertThat(masoesOption.getLongOpt(), is(nullValue()));
        assertThat(masoesOption.getDescription(), is(notNullValue()));
        assertThat(masoesOption.getArgType(), is(ArgumentType.UNLIMITED_ARGS));
        assertThat(masoesOption.getOrder(), is(50));
        assertFalse(masoesOption.isFinalOption());
    }

    @Test
    public void shouldSetMasoesSettings() {
        Properties properties = new Properties();
        properties.put("masoes.env", "dummy");

        masoesOption.setProperties(properties);
        masoesOption.exec();

        verify(masoesSettingsMock).set("masoes.env", "dummy");
        verify(loggerMock).updatedSettings();
    }

}