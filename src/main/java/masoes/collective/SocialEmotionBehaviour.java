/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.collective;

import agent.AgentManagementAssistant;
import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.GetEmotionalState;
import masoes.ontology.state.collective.GetSocialEmotion;
import masoes.ontology.state.collective.SocialEmotion;
import ontology.OntologyAssistant;
import ontology.OntologyResponderBehaviour;
import util.ServiceBuilder;

import java.util.Arrays;
import java.util.List;

public class SocialEmotionBehaviour extends OntologyResponderBehaviour {

    private AgentManagementAssistant agentManagementAssistant;
    private OntologyAssistant masoesOntologyAssistant;

    public SocialEmotionBehaviour(Agent agent) {
        super(agent, MasoesOntology.getInstance());
        agentManagementAssistant = new AgentManagementAssistant(agent);
        masoesOntologyAssistant = new OntologyAssistant(agent, MasoesOntology.getInstance());
    }

    @Override
    public boolean isValidAction(Action action) {
        return Arrays.asList(GetSocialEmotion.class)
                .contains(action.getAction().getClass());
    }

    @Override
    public Predicate performAction(Action action) throws FailureException {
        SocialEmotionCalculator socialEmotionCalculator = new SocialEmotionCalculator();

        List<AID> search = searchEmotionalAgents();

        search.stream()
                .map(aid ->
                        (AgentState) masoesOntologyAssistant.sendRequestAction(aid, new GetEmotionalState()))
                .forEach(agentState ->
                        socialEmotionCalculator.addEmotionalState(agentState.getEmotionState().toEmotionalState()));

        if (search.isEmpty()) {
            return new SocialEmotion();
        }

        return new SocialEmotion(
                socialEmotionCalculator.getCentralEmotionalState(),
                socialEmotionCalculator.getEmotionalDispersion(),
                socialEmotionCalculator.getMaximumDistance()
        );
    }

    public List<AID> searchEmotionalAgents() {
        ServiceDescription serviceDescription = new ServiceBuilder()
                .name(MasoesOntology.ACTION_GET_EMOTIONAL_STATE)
                .build();
        return agentManagementAssistant.search(serviceDescription);
    }

}
