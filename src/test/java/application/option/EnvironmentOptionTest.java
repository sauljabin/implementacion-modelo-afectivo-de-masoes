/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ArgumentType;
import environment.Environment;
import environment.EnvironmentFactory;
import jade.JadeSettings;
import masoes.MasoesSettings;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class EnvironmentOptionTest {

    private EnvironmentOption environmentOption;
    private MasoesSettings masoesSettingsMock;
    private JadeSettings jadeSettingsMock;
    private EnvironmentFactory environmentFactoryMock;
    private Environment environmentMock;

    @Before
    public void setUp() throws Exception {
        masoesSettingsMock = mock(MasoesSettings.class);
        jadeSettingsMock = mock(JadeSettings.class);
        environmentFactoryMock = mock(EnvironmentFactory.class);
        environmentMock = mock(Environment.class);

        environmentOption = new EnvironmentOption();
        setFieldValue(environmentOption, "masoesSettings", masoesSettingsMock);
        setFieldValue(environmentOption, "jadeSettings", jadeSettingsMock);
        setFieldValue(environmentOption, "environmentFactory", environmentFactoryMock);

        doReturn(environmentMock).when(environmentFactoryMock).createEnvironment(anyString());
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(environmentOption.getOpt(), is("E"));
        assertThat(environmentOption.getLongOpt(), is(nullValue()));
        assertThat(environmentOption.getDescription(), containsString("Sets the environment (dummy, wikipedia)"));
        assertThat(environmentOption.getArgType(), is(ArgumentType.ONE_ARG));
        assertThat(environmentOption.getOrder(), is(60));
        assertFalse(environmentOption.isFinalOption());
    }

    @Test
    public void shouldSetEnvironmentSettingValue() {
        String caseStudy = "default";
        environmentOption.setValue(caseStudy);
        environmentOption.exec();
        verify(masoesSettingsMock).set(MasoesSettings.MASOES_ENV, caseStudy);
        verify(environmentFactoryMock).createEnvironment(caseStudy);
    }

    @Test
    public void shouldInvokeEnvironmentCreation() {
        String expectedParameter = "expectedParameter";
        doReturn(expectedParameter).when(environmentMock).toJadeParameter();
        environmentOption.exec();
        verify(environmentFactoryMock).createEnvironment(anyString());
        verify(environmentMock).toJadeParameter();
        verify(jadeSettingsMock).set(eq(JadeSettings.AGENTS), eq(expectedParameter));
    }

}