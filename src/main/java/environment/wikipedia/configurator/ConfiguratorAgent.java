/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import agent.AgentLogger;
import agent.AgentManagementAssistant;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import masoes.MasoesSettings;
import masoes.component.behavioural.EmotionalSpace;
import masoes.component.behavioural.EmotionalState;
import masoes.ontology.MasoesOntology;
import ontology.OntologyAssistant;

import java.util.Comparator;
import java.util.Optional;

public class ConfiguratorAgent extends GuiAgent {

    private AgentLogger logger;
    private ConfiguratorAgentGui configuratorAgentGui;
    private ConfiguratorAgentListener configuratorAgentListener;
    private AgentManagementAssistant agentManagementAssistant;
    private OntologyAssistant masoesOntologyAssistant;
    private EmotionalSpace emotionalSpace;

    public ConfiguratorAgent() {
        configuratorAgentGui = new ConfiguratorAgentGui();
        configuratorAgentListener = new ConfiguratorAgentListener(this, configuratorAgentGui);
        logger = new AgentLogger(this);
        agentManagementAssistant = new AgentManagementAssistant(this);
        masoesOntologyAssistant = new OntologyAssistant(this, MasoesOntology.getInstance());
        emotionalSpace = new EmotionalSpace();
    }

    @Override
    protected void setup() {
        configuratorAgentGui.showGui();
    }

    @Override
    protected void takeDown() {
        configuratorAgentGui.closeGui();
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        try {
            switch (ConfiguratorAgentEvent.fromInt(guiEvent.getType())) {
                case CLOSE_WINDOW:
                    doDelete();
                    break;
                case UPDATE_SATISFACTION_INCREASE:
                    updateSatisfactionIncrease();
                    break;
                case UPDATE_ACTIVATION_INCREASE:
                    updateActivationIncrease();
                    break;
                case CLEAN:
                    cleanSimulation();
                    break;
                case START:
                    startSimulation();
                    break;
                case ADD_AGENT:
                    addAgent();
                    break;
                case UPDATE_ACTIVATION_TO_ADD:
                case UPDATE_SATISFACTION_TO_ADD:
                    updateEmotionToAdd();
                    break;
                case REMOVE_AGENTS:
                    removeAgents();
                    break;
            }
        } catch (Exception e) {
            logger.exception(e);
            configuratorAgentGui.showError(e.getMessage());
        }
    }

    private void removeAgents() {
        configuratorAgentGui.getSelectedAgentToAdd()
                .forEach(agentToAdd ->
                        configuratorAgentGui.removeAgentToAdd(agentToAdd));
    }

    private void updateEmotionToAdd() {
        EmotionalState emotionalState = new EmotionalState(
                configuratorAgentGui.getActivationToAdd(),
                configuratorAgentGui.getSatisfactionToAdd()
        );
        configuratorAgentGui.setEmotionToAdd(emotionalSpace.searchEmotion(emotionalState));
    }

    private void addAgent() {
        Optional<AgentToAdd> max = configuratorAgentGui.getAgentsToAdd()
                .stream()
                .filter(agentToAdd ->
                        agentToAdd.getType().equals(configuratorAgentGui.getAgentTypeToAdd()))
                .max(Comparator.comparingInt(AgentToAdd::getSequence));

        int nextSequence = 1;

        if (max.isPresent()) {
            nextSequence = max.get().getSequence() + 1;
        }

        EmotionalState emotionalState = new EmotionalState(
                configuratorAgentGui.getActivationToAdd(),
                configuratorAgentGui.getSatisfactionToAdd()
        );

        AgentToAdd agentToAdd = new AgentToAdd(
                nextSequence,
                configuratorAgentGui.getAgentTypeToAdd(),
                emotionalState
        );
        configuratorAgentGui.addAgentToAdd(agentToAdd);
    }

    private void startSimulation() {
    }

    private void cleanSimulation() {
    }

    private void updateActivationIncrease() {
        MasoesSettings.getInstance().set(MasoesSettings.MASOES_ACTIVATION_INCREASE, String.valueOf(configuratorAgentGui.getActivationIncrease()));
    }

    private void updateSatisfactionIncrease() {
        MasoesSettings.getInstance().set(MasoesSettings.MASOES_SATISFACTION_INCREASE, String.valueOf(configuratorAgentGui.getSatisfactionIncrease()));
    }

}
