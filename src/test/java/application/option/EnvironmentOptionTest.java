/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import jade.command.AgentCommandFormatter;
import jade.core.Agent;
import masoes.environment.Environment;
import masoes.environment.EnvironmentFactory;
import org.junit.Before;
import org.junit.Test;
import settings.agent.SettingsAgent;
import settings.application.ApplicationSettings;
import settings.jade.JadeSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
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

        doReturn(environmentMock).when(environmentFactoryMock).createEnvironment();
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
    public void shouldInvokeEnvironmentCreation() {
        List<AgentCommandFormatter> expectedJadeAgentOptionList = new ArrayList<>();
        expectedJadeAgentOptionList.add(new AgentCommandFormatter("agent", Agent.class, Arrays.asList("arg1", "arg2")));
        doReturn(expectedJadeAgentOptionList).when(environmentMock).getAgentCommands();

        environmentOption.exec();

        verify(environmentFactoryMock).createEnvironment();
        verify(environmentMock, atLeastOnce()).getAgentCommands();
        verify(jadeSettingsMock).set(JadeSettings.AGENTS, "agent:jade.core.Agent(arg1,arg2);settings:" + SettingsAgent.class.getName());
    }

    @Test
    public void shouldSetOnlySettingsAgent() {
        doReturn(null).when(environmentMock).getAgentCommands();

        environmentOption.exec();

        verify(environmentFactoryMock).createEnvironment();
        verify(environmentMock, atLeastOnce()).getAgentCommands();
        verify(jadeSettingsMock).set(JadeSettings.AGENTS, "settings:" + SettingsAgent.class.getName());
    }

    @Test
    public void shouldInvokeEnvironmentCreationWithTwoAgents() {
        List<AgentCommandFormatter> expectedJadeAgentOptionList = new ArrayList<>();
        expectedJadeAgentOptionList.add(new AgentCommandFormatter("agent", Agent.class, Arrays.asList("arg1", "arg2")));
        expectedJadeAgentOptionList.add(new AgentCommandFormatter("agent2", Agent.class));
        doReturn(expectedJadeAgentOptionList).when(environmentMock).getAgentCommands();

        environmentOption.exec();

        verify(environmentFactoryMock).createEnvironment();
        verify(environmentMock, atLeastOnce()).getAgentCommands();
        verify(jadeSettingsMock).set(JadeSettings.AGENTS, "agent:jade.core.Agent(arg1,arg2);agent2:jade.core.Agent;settings:" + SettingsAgent.class.getName());
    }

    @Test
    public void shouldSetEnvironmentSettingValue() {
        String caseStudy = "default";
        environmentOption.setValue(caseStudy);
        environmentOption.exec();
        verify(applicationSettingsMock).set(ApplicationSettings.MASOES_ENV, caseStudy);
    }

}