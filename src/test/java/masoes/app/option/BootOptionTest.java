/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app.option;

import jade.core.Agent;
import masoes.env.Environment;
import masoes.env.EnvironmentAgentInfo;
import masoes.env.EnvironmentFactory;
import masoes.env.InvalidEnvironmentException;
import masoes.jade.JadeBoot;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class BootOptionTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private BootOption bootOption;
    private EnvironmentFactory mockEnvironmentFactory;
    private JadeBoot mockJadeBoot;
    private Environment mockEnv;
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        mockEnvironmentFactory = mock(EnvironmentFactory.class);
        mockJadeBoot = mock(JadeBoot.class);
        mockEnv = mock(Environment.class);

        bootOption = new BootOption();
        setFieldValue(bootOption, "environmentFactory", mockEnvironmentFactory);
        setFieldValue(bootOption, "jadeBoot", mockJadeBoot);

        when(mockEnvironmentFactory.createEnvironment()).thenReturn(mockEnv);
    }

    @Test
    public void shouldGetCorrectConfiguration() {
        assertThat(bootOption.getOpt(), is("b"));
        assertThat(bootOption.getLongOpt(), is("boot"));
        assertThat(bootOption.getDescription(), is("Starts the application"));
        assertFalse(bootOption.hasArg());
        assertThat(bootOption.getOrder(), is(60));
    }

    @Test
    public void shouldInvokeEnvironmentCreation() {
        List<EnvironmentAgentInfo> expectedJadeAgentOptionList = new ArrayList<>();
        expectedJadeAgentOptionList.add(new EnvironmentAgentInfo("agent", Agent.class, Arrays.asList("arg1", "arg2")));
        when(mockEnv.getEnvironmentAgentInfoList()).thenReturn(expectedJadeAgentOptionList);

        bootOption.exec(null);

        verify(mockEnvironmentFactory).createEnvironment();
        verify(mockEnv).getEnvironmentAgentInfoList();
        verify(mockJadeBoot).boot(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue(), is("agent:jade.core.Agent(arg1,arg2);settings:masoes.jade.setting.SettingsAgent"));
    }

    @Test
    public void shouldInvokeEnvironmentCreationWithTwoAgents() {
        List<EnvironmentAgentInfo> expectedJadeAgentOptionList = new ArrayList<>();
        expectedJadeAgentOptionList.add(new EnvironmentAgentInfo("agent", Agent.class, Arrays.asList("arg1", "arg2")));
        expectedJadeAgentOptionList.add(new EnvironmentAgentInfo("agent2", Agent.class));
        when(mockEnv.getEnvironmentAgentInfoList()).thenReturn(expectedJadeAgentOptionList);

        bootOption.exec(null);

        verify(mockEnvironmentFactory).createEnvironment();
        verify(mockEnv).getEnvironmentAgentInfoList();
        verify(mockJadeBoot).boot(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue(), is("agent:jade.core.Agent(arg1,arg2);agent2:jade.core.Agent;settings:masoes.jade.setting.SettingsAgent"));
    }

    @Test
    public void shouldThrowInvalidParameterWhenNoExistValueException() {
        when(mockEnv.getEnvironmentAgentInfoList()).thenReturn(null);
        expectedException.expect(InvalidEnvironmentException.class);
        expectedException.expectMessage("No agents in environment");
        bootOption.exec(null);
    }

}