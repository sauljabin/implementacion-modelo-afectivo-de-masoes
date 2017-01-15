/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package application.option;

import application.settings.ApplicationSettings;
import jade.core.Agent;
import jade.settings.JadeSettings;
import jade.settings.agent.SettingsAgent;
import masoes.env.Environment;
import masoes.env.EnvironmentAgentInfo;
import masoes.env.EnvironmentFactory;
import org.junit.Before;
import org.junit.Test;

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
    private ApplicationSettings mockApplicationSettings;
    private JadeSettings mockJadeSettings;
    private EnvironmentFactory mockEnvironmentFactory;
    private Environment mockEnvironment;

    @Before
    public void setUp() throws Exception {
        mockApplicationSettings = mock(ApplicationSettings.class);
        mockJadeSettings = mock(JadeSettings.class);
        mockEnvironmentFactory = mock(EnvironmentFactory.class);
        mockEnvironment = mock(Environment.class);

        environmentOption = new EnvironmentOption();
        setFieldValue(environmentOption, "applicationSettings", mockApplicationSettings);
        setFieldValue(environmentOption, "jadeSettings", mockJadeSettings);
        setFieldValue(environmentOption, "environmentFactory", mockEnvironmentFactory);

        doReturn(mockEnvironment).when(mockEnvironmentFactory).createEnvironment();
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
        List<EnvironmentAgentInfo> expectedJadeAgentOptionList = new ArrayList<>();
        expectedJadeAgentOptionList.add(new EnvironmentAgentInfo("agent", Agent.class, Arrays.asList("arg1", "arg2")));
        doReturn(expectedJadeAgentOptionList).when(mockEnvironment).getEnvironmentAgentInfoList();

        environmentOption.exec();

        verify(mockEnvironmentFactory).createEnvironment();
        verify(mockEnvironment, atLeastOnce()).getEnvironmentAgentInfoList();
        verify(mockJadeSettings).set(JadeSettings.AGENTS, "agent:jade.core.Agent(arg1,arg2);settings:" + SettingsAgent.class.getName());
    }

    @Test
    public void shouldSetOnlySettingsAgent() {
        doReturn(null).when(mockEnvironment).getEnvironmentAgentInfoList();

        environmentOption.exec();

        verify(mockEnvironmentFactory).createEnvironment();
        verify(mockEnvironment, atLeastOnce()).getEnvironmentAgentInfoList();
        verify(mockJadeSettings).set(JadeSettings.AGENTS, "settings:" + SettingsAgent.class.getName());
    }

    @Test
    public void shouldInvokeEnvironmentCreationWithTwoAgents() {
        List<EnvironmentAgentInfo> expectedJadeAgentOptionList = new ArrayList<>();
        expectedJadeAgentOptionList.add(new EnvironmentAgentInfo("agent", Agent.class, Arrays.asList("arg1", "arg2")));
        expectedJadeAgentOptionList.add(new EnvironmentAgentInfo("agent2", Agent.class));
        doReturn(expectedJadeAgentOptionList).when(mockEnvironment).getEnvironmentAgentInfoList();

        environmentOption.exec();

        verify(mockEnvironmentFactory).createEnvironment();
        verify(mockEnvironment, atLeastOnce()).getEnvironmentAgentInfoList();
        verify(mockJadeSettings).set(JadeSettings.AGENTS, "agent:jade.core.Agent(arg1,arg2);agent2:jade.core.Agent;settings:" + SettingsAgent.class.getName());
    }

    @Test
    public void shouldSetEnvironmentSettingValue() {
        String caseStudy = "default";
        environmentOption.setValue(caseStudy);
        environmentOption.exec();
        verify(mockApplicationSettings).set(ApplicationSettings.MASOES_ENV, caseStudy);
    }

}