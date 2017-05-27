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
import jade.util.leap.ArrayList;
import settings.ontology.GetSetting;
import settings.ontology.Setting;
import settings.ontology.SettingsOntology;
import settings.ontology.SystemSettings;

public class ActionResponderValidActionBehaviour extends OneShotBehaviour {

    @Override
    public void action() {
        myAgent.addBehaviour(new ActionResponderBehaviour(myAgent, SettingsOntology.getInstance(), GetSetting.class) {

            @Override
            public Predicate performAction(Action action) throws FailureException {
                SystemSettings systemSettings = new SystemSettings(new ArrayList());
                systemSettings.getSettings().add(new Setting("expectedValue", "functionalTest"));
                return systemSettings;
            }
        });
    }

}
