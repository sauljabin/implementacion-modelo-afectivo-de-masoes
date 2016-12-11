/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import masoes.app.settings.ApplicationSettings;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class EnvironmentOptionTest {

    private EnvironmentOption environmentOption;
    private ApplicationSettings mockApplicationSettings;

    @Before
    public void setUp() throws Exception {
        mockApplicationSettings = mock(ApplicationSettings.class);
        environmentOption = new EnvironmentOption();
        setFieldValue(environmentOption, "applicationSettings", mockApplicationSettings);
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(environmentOption.getOpt(), is("e"));
        assertThat(environmentOption.getLongOpt(), is("env"));
        assertThat(environmentOption.getDescription(), is("Sets the environment for case study"));
        assertThat(environmentOption.getArgType(), is(ArgumentType.ONE_ARG));
        assertThat(environmentOption.getOrder(), is(50));
    }

    @Test
    public void shouldSetEnvironmentSettingValue() {
        String caseStudy = "default";
        environmentOption.setValue(caseStudy);
        environmentOption.exec();
        verify(mockApplicationSettings).set(ApplicationSettings.MASOES_ENV, caseStudy);
    }

}