/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.collective;

import environment.dummy.DummyEmotionalAgent;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import masoes.component.behavioural.EmotionalState;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.collective.GetSocialEmotion;
import masoes.ontology.state.collective.SocialEmotion;
import ontology.OntologyAssistant;
import org.junit.Before;
import org.junit.Test;
import test.FunctionalTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SocialEmotionAgentFunctionalTest extends FunctionalTest {

    private AID socialEmotionAgent;

    @Before
    public void setUp() {
        socialEmotionAgent = createAgent(SocialEmotionAgent.class, null);
    }

    @Test
    public void shouldGetAllServicesFromDF() {
        List<ServiceDescription> services = services(socialEmotionAgent);
        List<String> results = services.stream().map(ServiceDescription::getName).collect(Collectors.toList());
        assertThat(results, hasItem(MasoesOntology.ACTION_GET_SOCIAL_EMOTION));
    }

    @Test
    public void shouldGetCentralEmotion() {
        EmotionalState firstEmotionalState = new EmotionalState();

        createAgent(DummyEmotionalAgent.class, Arrays.asList(
                "--activation=" + String.valueOf(firstEmotionalState.getActivation()),
                "--satisfaction=" + String.valueOf(firstEmotionalState.getSatisfaction())
        ));

        EmotionalState secondEmotionalState = new EmotionalState();
        createAgent(DummyEmotionalAgent.class, Arrays.asList(
                "--activation=" + String.valueOf(secondEmotionalState.getActivation()),
                "--satisfaction=" + String.valueOf(secondEmotionalState.getSatisfaction())
        ));

        SocialEmotionCalculator socialEmotionCalculator = new SocialEmotionCalculator();
        socialEmotionCalculator.addEmotionalState(firstEmotionalState);
        socialEmotionCalculator.addEmotionalState(secondEmotionalState);

        OntologyAssistant ontologyAssistant = createOntologyAssistant(MasoesOntology.getInstance());
        SocialEmotion socialEmotion = (SocialEmotion) ontologyAssistant.sendRequestAction(socialEmotionAgent, new GetSocialEmotion());

        assertThat(socialEmotion.getCentralEmotion().getActivation(), is(socialEmotionCalculator.getCentralEmotionalState().getActivation()));
        assertThat(socialEmotion.getCentralEmotion().getSatisfaction(), is(socialEmotionCalculator.getCentralEmotionalState().getSatisfaction()));

        assertThat(socialEmotion.getEmotionalDispersion().getActivation(), is(socialEmotionCalculator.getEmotionalDispersion().getActivation()));
        assertThat(socialEmotion.getEmotionalDispersion().getSatisfaction(), is(socialEmotionCalculator.getEmotionalDispersion().getSatisfaction()));

        assertThat(socialEmotion.getMaximumDistances().getActivation(), is(socialEmotionCalculator.getMaximumDistances().getActivation()));
        assertThat(socialEmotion.getMaximumDistances().getSatisfaction(), is(socialEmotionCalculator.getMaximumDistances().getSatisfaction()));
    }

}