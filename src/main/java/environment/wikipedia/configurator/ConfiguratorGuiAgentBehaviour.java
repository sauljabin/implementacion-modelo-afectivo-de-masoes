/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.wikipedia.configurator;

import behaviour.CounterBehaviour;
import environment.wikipedia.configurator.stimulus.StimulusModel;
import jade.content.AgentAction;
import jade.core.AID;
import masoes.MasoesSettings;
import masoes.collective.SocialEmotionCalculator;
import masoes.component.behavioural.AffectiveModel;
import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.EmotionalState;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.GetEmotionalState;
import masoes.ontology.state.collective.CentralEmotion;
import masoes.ontology.state.collective.EmotionalDispersion;
import masoes.ontology.state.collective.MaximumDistance;
import masoes.ontology.stimulus.EvaluateStimulus;
import masoes.ontology.stimulus.EventStimulus;
import ontology.OntologyAssistant;
import translate.Translation;
import util.RandomGenerator;
import util.StopWatch;
import util.StringFormatter;
import util.TextFileWriter;

import java.io.File;

public class ConfiguratorGuiAgentBehaviour extends CounterBehaviour {

    private final Object lock = new Object();
    private OntologyAssistant assistant;
    private ConfiguratorGuiAgent configuratorAgent;
    private SocialEmotionCalculator socialEmotionCalculator;
    private Translation translation = Translation.getInstance();
    private StopWatch stopWatch;
    private static final long WAIT = 1000 / Long.parseLong(MasoesSettings.getInstance().get(MasoesSettings.BEHAVIOUR_IPS));
    private boolean paused;

    public ConfiguratorGuiAgentBehaviour(ConfiguratorGuiAgent configuratorAgent, int maxCount) {
        super(configuratorAgent, maxCount);
        this.configuratorAgent = configuratorAgent;
        assistant = new OntologyAssistant(configuratorAgent, MasoesOntology.getInstance());
        socialEmotionCalculator = new SocialEmotionCalculator();
        stopWatch = new StopWatch();
    }

    @Override
    public void onStart() {
        paused = false;
    }

    @Override
    public void count(int i) {
        setIteration(i);
        updateStates(i);
        updateSocialEmotionChart(i);
        sleep();

        if (paused) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    @Override
    public int onEnd() {
        configuratorAgent.getConfiguratorGui().getPauseButton().setEnabled(false);

        int width = 600;
        int height = 400;

        try {
            File folder = new File("output/wikipedia");
            folder.mkdir();
            configuratorAgent.getCentralEmotionChart().exportImage(folder, height);
            configuratorAgent.getMaximumDistanceChart().exportImage(folder, width, height);
            configuratorAgent.getEmotionalDispersionChart().exportImage(folder, width, height);
            configuratorAgent.getEmotionModificationChart().exportImage(folder, width, height);
            configuratorAgent.getBehaviourModificationChart().exportImage(folder, width, height);
            configuratorAgent.getEmotionalStateChart().exportImage(folder, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onEnd();
    }

    private void updateStates(int i) {
        socialEmotionCalculator.clear();

        TextFileWriter writer = new TextFileWriter("output/wikipedia", "iteration" + i + ".txt");
        writer.append("%s: %s", translation.get("gui.iteration").toUpperCase(), i);
        writer.newLine(2);

        writer.append(translation.get("gui.current_emotional_states").toUpperCase());

        String agentsHeaderFormat = "%40s %15s %15s %25s %7s %7s %15s";

        writer.append(agentsHeaderFormat,
                translation.get("gui.stimulus"),
                translation.get("gui.agent"),
                translation.get("gui.emotion"),
                translation.get("gui.emotion_type"),
                "A",
                "S",
                translation.get("gui.behaviour")
        );

        configuratorAgent.getAgentTableModel().getAgents().forEach(agent -> {
            AID receiver = myAgent.getAID(agent.getName());

            AgentAction agentAction = new GetEmotionalState();

            String stimulusName = "-";

            if (!agent.getStimuli().isEmpty()) {
                StimulusModel randomItem = RandomGenerator.getRandomItem(agent.getStimuli());
                stimulusName = randomItem.getName();
                EventStimulus stimulus = new EventStimulus(receiver, randomItem.getValue());
                agentAction = new EvaluateStimulus(stimulus);
            }

            AgentState agentState = (AgentState) assistant.sendRequestAction(receiver, agentAction);

            configuratorAgent.getAgentStateTableModel().addAgent(agentState);

            EmotionalState emotionalState = agentState.getEmotionState().toEmotionalState();

            socialEmotionCalculator.addEmotionalState(emotionalState);

            configuratorAgent.getCentralEmotionChart().addEmotionalState(agent.getName(), emotionalState);
            configuratorAgent.getEmotionModificationChart().addEmotion(agent.getName(), i, agentState);
            configuratorAgent.getBehaviourModificationChart().addBehaviourType(agent.getName(), i, agentState);
            configuratorAgent.getEmotionalStateChart().addEmotionalState(agent.getName(), i, emotionalState);

            writer.append(agentsHeaderFormat,
                    stimulusName,
                    agent.getName(),
                    translation.get(agentState.getEmotionState().getName().toLowerCase()),
                    translation.get(agentState.getEmotionState().getType().toLowerCase()),
                    StringFormatter.toString(agentState.getEmotionState().getActivation()),
                    StringFormatter.toString(agentState.getEmotionState().getSatisfaction()),
                    translation.get(agentState.getBehaviourState().getType().toLowerCase())
            );
        });

        writer.newLine(2);

        writer.append(translation.get("gui.social_emotion").toUpperCase());

        EmotionalDispersion emotionalDispersion = socialEmotionCalculator.getEmotionalDispersion();
        CentralEmotion centralEmotionalState = socialEmotionCalculator.getCentralEmotion();
        MaximumDistance maximumDistances = socialEmotionCalculator.getMaximumDistance();

        Emotion emotion = AffectiveModel.getInstance().searchEmotion(centralEmotionalState.toEmotionalState());

        writer.append("%30s: %s %s - %s",
                translation.get("gui.central_emotion"),
                StringFormatter.toStringPoint(centralEmotionalState.getActivation(),
                        centralEmotionalState.getSatisfaction()),
                translation.get(emotion.getName().toLowerCase()),
                translation.get(emotion.getType().toString().toLowerCase())
        );

        writer.append("%30s: %s",
                translation.get("gui.maximum_distance"),
                StringFormatter.toStringPoint(maximumDistances.getActivation(),
                        maximumDistances.getSatisfaction())
        );

        writer.append("%30s: %s",
                translation.get("gui.emotional_dispersion"),
                StringFormatter.toStringPoint(emotionalDispersion.getActivation(),
                        emotionalDispersion.getSatisfaction())
        );

        writer.close();

        setGuiSocialEmotion();
    }

    private void updateSocialEmotionChart(int i) {
        configuratorAgent.getCentralEmotionChart().addCentralEmotion(socialEmotionCalculator.getCentralEmotion());
        configuratorAgent.getMaximumDistanceChart().addMaximumDistance(i, socialEmotionCalculator.getMaximumDistance());
        configuratorAgent.getEmotionalDispersionChart().addDispersion(i, socialEmotionCalculator.getEmotionalDispersion());
    }

    private void setGuiSocialEmotion() {
        EmotionalDispersion emotionalDispersion = socialEmotionCalculator.getEmotionalDispersion();
        CentralEmotion centralEmotionalState = socialEmotionCalculator.getCentralEmotion();
        MaximumDistance maximumDistance = socialEmotionCalculator.getMaximumDistance();

        configuratorAgent.getConfiguratorGui().getCollectiveCentralEmotionalStateLabel()
                .setText(centralEmotionalState.toStringPoint());

        configuratorAgent.getConfiguratorGui().getCollectiveCentralEmotionLabel()
                .setText(
                        translate(centralEmotionalState.getEmotion().getName()) +
                                " - " +
                                translate(centralEmotionalState.getEmotion().getType().toString())
                );

        configuratorAgent.getConfiguratorGui().getEmotionalDispersionValueLabel()
                .setText(emotionalDispersion.toStringPoint());

        configuratorAgent.getConfiguratorGui().getMaxDistanceEmotionValueLabel()
                .setText(maximumDistance.toStringPoint());
    }

    private String translate(String key) {
        return Translation.getInstance().get(key.toLowerCase());
    }

    private void setIteration(int i) {
        configuratorAgent.getConfiguratorGui().getIterationLabel().setText(String.valueOf(i));
    }

    private void sleep() {
        stopWatch.start();
        long time = stopWatch.getTime();
        while (time < WAIT) {
            time = stopWatch.getTime();
        }
        stopWatch.stop();
    }

    public void pause() {
        paused = true;
    }

    public void play() {
        paused = false;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

}
