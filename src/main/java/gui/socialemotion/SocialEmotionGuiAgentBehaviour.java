/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.socialemotion;

import agent.AgentManagementAssistant;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import masoes.MasoesSettings;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.collective.GetSocialEmotion;
import masoes.ontology.state.collective.SocialEmotion;
import ontology.OntologyAssistant;
import util.ServiceBuilder;
import util.StopWatch;

import javax.swing.*;
import java.util.List;

public class SocialEmotionGuiAgentBehaviour extends CyclicBehaviour {

    private static final long WAIT = 1000 / Long.parseLong(MasoesSettings.getInstance().get(MasoesSettings.BEHAVIOUR_IPS));
    private SocialEmotionGuiAgent socialEmotionGuiAgent;
    private OntologyAssistant masoesOntologyAssistant;
    private StopWatch stopWatch;
    private AgentManagementAssistant agentManagementAssistant;
    private AID socialEmotionAgentAID;

    public SocialEmotionGuiAgentBehaviour(SocialEmotionGuiAgent socialEmotionGuiAgent) {
        this.socialEmotionGuiAgent = socialEmotionGuiAgent;
        masoesOntologyAssistant = new OntologyAssistant(socialEmotionGuiAgent, MasoesOntology.getInstance());
        agentManagementAssistant = new AgentManagementAssistant(socialEmotionGuiAgent);
        stopWatch = new StopWatch();
    }

    @Override
    public void onStart() {
        List<AID> socialEmotionAgents = agentManagementAssistant.search(new ServiceBuilder()
                .name(MasoesOntology.ACTION_GET_SOCIAL_EMOTION)
                .build()
        );

        if (socialEmotionAgents.isEmpty()) {
            String message = socialEmotionGuiAgent.getLocalName() + " no social emotion agent calculator found";
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            socialEmotionGuiAgent.doDelete();
        }

        socialEmotionAgentAID = socialEmotionAgents.get(0);
    }

    @Override
    public void action() {
        SocialEmotion socialEmotion = (SocialEmotion) masoesOntologyAssistant.sendRequestAction(socialEmotionAgentAID, new GetSocialEmotion());
        socialEmotionGuiAgent.getSocialEmotionGui().setSocialEmotion(socialEmotion);
        sleep();
    }

    private void sleep() {
        stopWatch.start();
        while (stopWatch.getTime() < WAIT) {
            continue;
        }
        stopWatch.stop();
    }

}
