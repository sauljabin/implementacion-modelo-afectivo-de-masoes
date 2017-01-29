/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import gui.agent.RequesterGuiAgent;
import jade.command.AgentCommandFormatter;
import jade.core.Agent;
import masoes.environment.Environment;
import masoes.environment.EnvironmentFactory;
import org.junit.Before;
import org.junit.Test;
import settings.agent.SettingsAgent;
import settings.loader.ApplicationSettings;
import settings.loader.JadeSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
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
        verify(jadeSettingsMock).set(eq(JadeSettings.AGENTS), contains("agent:jade.core.Agent(arg1,arg2)"));
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
        verify(jadeSettingsMock).set(eq(JadeSettings.AGENTS), contains("agent:jade.core.Agent(arg1,arg2);agent2:jade.core.Agent"));
    }

    @Test
    public void shouldSetEnvironmentSettingValue() {
        String caseStudy = "default";
        environmentOption.setValue(caseStudy);
        environmentOption.exec();
        verify(applicationSettingsMock).set(ApplicationSettings.MASOES_ENV, caseStudy);
    }

    @Test
    public void shouldAddSettingsAgent() {
        doReturn(null).when(environmentMock).getAgentCommands();

        environmentOption.exec();

        verify(environmentFactoryMock).createEnvironment();
        verify(environmentMock, atLeastOnce()).getAgentCommands();
        verify(jadeSettingsMock).set(eq(JadeSettings.AGENTS), contains("settings:" + SettingsAgent.class.getName()));
    }

    @Test
    public void shouldNotAddSettingsAgentWhenHasOne() {
        List<AgentCommandFormatter> expectedJadeAgentOptionList = new ArrayList<>();
        expectedJadeAgentOptionList.add(new AgentCommandFormatter("alreadyExists", SettingsAgent.class));
        doReturn(expectedJadeAgentOptionList).when(environmentMock).getAgentCommands();

        environmentOption.exec();

        verify(environmentFactoryMock).createEnvironment();
        verify(jadeSettingsMock).set(eq(JadeSettings.AGENTS), contains("alreadyExists:" + SettingsAgent.class.getName()));
        verify(jadeSettingsMock, never()).set(eq(JadeSettings.AGENTS), contains("settings:" + SettingsAgent.class.getName()));
    }

    @Test
    public void shouldAddGuiAgent() {
        doReturn(null).when(environmentMock).getAgentCommands();
        doReturn("true").when(jadeSettingsMock).get(JadeSettings.GUI);

        environmentOption.exec();

        verify(environmentFactoryMock).createEnvironment();
        verify(environmentMock, atLeastOnce()).getAgentCommands();
        verify(jadeSettingsMock).set(eq(JadeSettings.AGENTS), contains("requester:" + RequesterGuiAgent.class.getName()));
    }

    @Test
    public void shouldNotAddNewGuiAgentAgentWhenHasOne() {
        List<AgentCommandFormatter> expectedJadeAgentOptionList = new ArrayList<>();
        expectedJadeAgentOptionList.add(new AgentCommandFormatter("alreadyExists", RequesterGuiAgent.class));
        doReturn(expectedJadeAgentOptionList).when(environmentMock).getAgentCommands();
        doReturn("true").when(jadeSettingsMock).get(JadeSettings.GUI);

        environmentOption.exec();

        verify(environmentFactoryMock).createEnvironment();
        verify(jadeSettingsMock).set(eq(JadeSettings.AGENTS), contains("alreadyExists:" + RequesterGuiAgent.class.getName()));
        verify(jadeSettingsMock, never()).set(eq(JadeSettings.AGENTS), contains("requester:" + RequesterGuiAgent.class.getName()));
    }

    @Test
    public void shouldNotAddGuiAgentWhenJadeGuiIsFalse() {
        doReturn(null).when(environmentMock).getAgentCommands();
        doReturn("false").when(jadeSettingsMock).get(JadeSettings.GUI);

        environmentOption.exec();

        verify(jadeSettingsMock, never()).set(eq(JadeSettings.AGENTS), contains("requester:" + RequesterGuiAgent.class.getName()));
    }

    @Test
    public void shouldNotAddGuiAgentAgentWhenHasOneAndJadeGuiIsFalse() {
        List<AgentCommandFormatter> expectedJadeAgentOptionList = new ArrayList<>();
        expectedJadeAgentOptionList.add(new AgentCommandFormatter("alreadyExists", RequesterGuiAgent.class));
        doReturn(expectedJadeAgentOptionList).when(environmentMock).getAgentCommands();
        doReturn("false").when(jadeSettingsMock).get(JadeSettings.GUI);

        environmentOption.exec();

        verify(environmentFactoryMock).createEnvironment();
        verify(jadeSettingsMock, never()).set(eq(JadeSettings.AGENTS), contains("alreadyExists:" + RequesterGuiAgent.class.getName()));
        verify(jadeSettingsMock, never()).set(eq(JadeSettings.AGENTS), contains("requester:" + RequesterGuiAgent.class.getName()));
    }

}