/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology;

import jade.content.Predicate;
import jade.content.onto.basic.Action;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import settings.ontology.GetSetting;
import settings.ontology.Setting;
import settings.ontology.SettingsOntology;
import settings.ontology.SystemSettings;

public class OntologyAssistantResponderBehaviour extends OneShotBehaviour {

    @Override
    public void action() {
        myAgent.addBehaviour(new OntologyResponderBehaviour(myAgent, MessageTemplate.MatchAll(), SettingsOntology.getInstance()) {
            @Override
            public boolean isValidAction(Action action) {
                return true;
            }

            @Override
            public Predicate performAction(Action action) throws FailureException {
                GetSetting agentAction = (GetSetting) action.getAction();
                ArrayList settings = new ArrayList();
                settings.add(new Setting(agentAction.getKey(), agentAction.getKey()));
                return new SystemSettings(settings);
            }
        });
    }

}
