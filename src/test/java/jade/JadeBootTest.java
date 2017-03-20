/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade;

import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.PlatformController;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.unitils.util.ReflectionUtils.setFieldValue;

public class JadeBootTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private PlatformController platformController;
    private ProfileImpl jadeProfileMock;
    private Runtime jadeRuntimeMock;
    private JadeBoot jadeBoot;
    private JadeSettings jadeSettingsMock;
    private AgentContainer agentContainerMock;
    private Agent agentMock;

    @Before
    public void setUp() throws Exception {
        jadeSettingsMock = mock(JadeSettings.class);
        jadeProfileMock = mock(ProfileImpl.class);
        jadeRuntimeMock = mock(Runtime.class);
        agentContainerMock = mock(AgentContainer.class);
        platformController = mock(PlatformController.class);

        jadeBoot = JadeBoot.getInstance();
        setFieldValue(jadeBoot, "jadeProfile", jadeProfileMock);
        setFieldValue(jadeBoot, "jadeRuntime", jadeRuntimeMock);
        setFieldValue(jadeBoot, "jadeSettings", jadeSettingsMock);

        doReturn(platformController).when(agentContainerMock).getPlatformController();
        doReturn(agentContainerMock).when(jadeRuntimeMock).createMainContainer(jadeProfileMock);
        agentMock = mock(Agent.class);
    }

    @After
    public void tearDown() throws Exception {
        setFieldValue(jadeBoot, "INSTANCE", null);
    }

    @Test
    public void shouldSetJadeSettingsInJadeProfile() {
        String expectedKey1 = "key";
        String expectedKey2 = "key";
        String expectedValue1 = "key";
        String expectedValue2 = "key";

        Map<String, String> map = new HashMap<>();
        map.put(expectedKey1, expectedValue1);
        map.put(expectedKey2, expectedValue2);

        doReturn(map).when(jadeSettingsMock).toMap();

        jadeBoot.boot();

        verify(jadeProfileMock).setParameter(expectedKey1, expectedValue1);
        verify(jadeProfileMock).setParameter(expectedKey2, expectedValue2);
        verify(jadeRuntimeMock).createMainContainer(jadeProfileMock);
    }

    @Test
    public void shouldThrowJadeBootExceptionWhenErrorInCreateContainer() {
        String expectedMessage = "expectedMessage";
        doThrow(new RuntimeException(expectedMessage)).when(jadeRuntimeMock).createMainContainer(any(ProfileImpl.class));
        expectedException.expect(JadeBootException.class);
        expectedException.expectMessage(expectedMessage);
        jadeBoot.boot();
    }

    @Test
    public void shouldInvokeAddAgent() throws Exception {
        String expectedAgentName = "expectedAgentName";
        jadeBoot.boot();
        jadeBoot.addAgent(expectedAgentName, agentMock);
        verify(agentContainerMock).acceptNewAgent(expectedAgentName, agentMock);
    }

    @Test
    public void shouldThrowJadeBootExceptionWhenErrorInAddAgent() throws Exception {
        String expectedMessage = "expectedMessage";
        doThrow(new RuntimeException(expectedMessage)).when(agentContainerMock).acceptNewAgent(anyString(), any(Agent.class));
        expectedException.expect(AgentException.class);
        expectedException.expectMessage(expectedMessage);
        jadeBoot.boot();
        jadeBoot.addAgent(anyString(), any(Agent.class));
    }

    @Test
    public void shouldKillContainer() throws Exception {
        jadeBoot.boot();
        jadeBoot.kill();
        verify(platformController).kill();
    }

    @Test
    public void shouldThrowJadeBootExceptionWhenErrorKill() throws Exception {
        String expectedMessage = "expectedMessage";
        doThrow(new RuntimeException(expectedMessage)).when(platformController).kill();
        expectedException.expect(ContainerException.class);
        expectedException.expectMessage(expectedMessage);
        jadeBoot.boot();
        jadeBoot.kill();
    }

    @Test
    public void shouldGetAgentFromName() throws Exception {
        jadeBoot.boot();
        String expectedName = "expectedName";
        jadeBoot.getAgent(expectedName);
        verify(agentContainerMock).getAgent(expectedName);
    }

}