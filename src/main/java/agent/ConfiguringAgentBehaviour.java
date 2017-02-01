/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package agent;

import jade.core.Agent;
import ontology.configurable.ConfigurableOntology;
import protocol.OntologyResponderBehaviour;

public class ConfiguringAgentBehaviour extends OntologyResponderBehaviour {

    public ConfiguringAgentBehaviour(Agent agent) {
        super(agent, new ConfiguringAgentRequestMessageTemplate(), new ConfigurableOntology());
    }

}
