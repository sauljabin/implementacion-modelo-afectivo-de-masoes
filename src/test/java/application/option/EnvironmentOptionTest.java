/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.ApplicationSettings;
import application.ArgumentType;
import environment.AgentParameter;
import environment.Environment;
import environment.EnvironmentFactory;
import jade.JadeSettings;
import masoes.NotifierAgent;
import org.junit.Before;
import org.junit.Test;
import settings.SettingsAgent;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class EnvironmentOptionTest {

    private EnvironmentOption environmentOption;
    private ApplicationSettings applicationSettingsMock;
    private JadeSettings jadeSettingsMock;
    private EnvironmentFactory environmentFactoryMock;
    private Environment environmentMock;

    @Before
    public void setUp() throws Exception {
        applicationSettingsMock = mock(ApplicationSettings.class);
        jadeSettingsMock = mock(JadeSettings.class);
        environmentFactoryMock = mock(EnvironmentFactory.class);
        environmentMock = mock(Environment.class);

        environmentOption = new EnvironmentOption();
        setFieldValue(environmentOption, "applicationSettings", applicationSettingsMock);
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
        assertThat(environmentOption.getOrder(), is(50));
        assertFalse(environmentOption.isFinalOption());
    }

    @Test
    public void shouldSetEnvironmentSettingValue() {
        String caseStudy = "default";
        environmentOption.setValue(caseStudy);
        environmentOption.exec();
        verify(applicationSettingsMock).set(ApplicationSettings.MASOES_ENV, caseStudy);
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

    @Test
    public void shouldAddSettingsAgent() {
        AgentParameter expectedAgentParameter = new AgentParameter("settings", SettingsAgent.class);
        doReturn(new ArrayList<>()).when(environmentMock).getAgentParameters();
        environmentOption.exec();
        verify(environmentMock).add(refEq(expectedAgentParameter));
    }

    @Test
    public void shouldNotAddSettingsAgentWhenHaveOne() {
        AgentParameter agentParameter = new AgentParameter("settings", SettingsAgent.class);
        ArrayList<AgentParameter> agents = new ArrayList<>();
        agents.add(agentParameter);
        doReturn(agents).when(environmentMock).getAgentParameters();
        environmentOption.exec();
        verify(environmentMock, never()).add(refEq(agentParameter));
    }

    @Test
    public void shouldAddNotifierAgent() {
        AgentParameter expectedAgentParameter = new AgentParameter("notifier", NotifierAgent.class);
        doReturn(new ArrayList<>()).when(environmentMock).getAgentParameters();
        environmentOption.exec();
        verify(environmentMock).add(refEq(expectedAgentParameter));
    }

    @Test
    public void shouldNotAddNotifierAgentWhenHaveOne() {
        AgentParameter agentParameter = new AgentParameter("notifier", NotifierAgent.class);
        ArrayList<AgentParameter> agents = new ArrayList<>();
        agents.add(agentParameter);
        doReturn(agents).when(environmentMock).getAgentParameters();
        environmentOption.exec();
        verify(environmentMock, never()).add(refEq(agentParameter));
    }

}