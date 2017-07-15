/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator;

import behaviour.CounterBehaviour;
import gui.simulator.agentconfiguration.AgentConfigurationModel;
import gui.simulator.stimulusconfiguration.StimulusConfigurationModel;
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
import org.apache.commons.lang3.StringUtils;
import translate.Translation;
import util.RandomGenerator;
import util.StopWatch;
import util.StringFormatter;
import util.TextFileWriter;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static gui.simulator.SimulatorGuiAgentInitialBehaviour.FILE_RESULT_OUTPUT;
import static gui.simulator.SimulatorGuiAgentInitialBehaviour.FILE_RESULT_OUTPUT_LATEX;
import static gui.simulator.SimulatorGuiAgentInitialBehaviour.FOLDER_RESULT_OUTPUT;

public class SimulatorGuiAgentBehaviour extends CounterBehaviour {

    private static final long WAIT = 1000 / Long.parseLong(MasoesSettings.getInstance().get(MasoesSettings.BEHAVIOUR_IPS));
    private static final String AGENTS_HEADER_FORMAT = "%45s %25s %20s %25s %7s %7s %15s";
    private final Object lock = new Object();
    private OntologyAssistant assistant;
    private SimulatorGuiAgent simulatorGuiAgent;
    private SocialEmotionCalculator socialEmotionCalculator;
    private Translation translation = Translation.getInstance();
    private StopWatch stopWatch;
    private boolean paused;
    private TextFileWriter writerResults;
    private TextFileWriter writerLatex;

    public SimulatorGuiAgentBehaviour(SimulatorGuiAgent simulatorGuiAgent, int maxCount) {
        super(simulatorGuiAgent, maxCount);
        this.simulatorGuiAgent = simulatorGuiAgent;
        assistant = new OntologyAssistant(simulatorGuiAgent, MasoesOntology.getInstance());
        socialEmotionCalculator = new SocialEmotionCalculator();
        stopWatch = new StopWatch();
    }

    @Override
    public void onStart() {
        writerResults = new TextFileWriter(FOLDER_RESULT_OUTPUT, FILE_RESULT_OUTPUT, true);
        writerLatex = new TextFileWriter(FOLDER_RESULT_OUTPUT, FILE_RESULT_OUTPUT_LATEX, true);
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
        simulatorGuiAgent.getSimulatorGui().getPauseButton().setEnabled(false);

        int width = 600;
        int height = 400;

        try {
            File folder = new File("output/simulation");
            folder.mkdir();
            simulatorGuiAgent.getCentralEmotionChart().exportImage(folder, height);
            simulatorGuiAgent.getMaximumDistanceChart().exportImage(folder, width, height);
            simulatorGuiAgent.getEmotionalDispersionChart().exportImage(folder, width, height);
            simulatorGuiAgent.getEmotionModificationChart().exportImage(folder, width, height);
            simulatorGuiAgent.getBehaviourModificationChart().exportImage(folder, width, height);
            simulatorGuiAgent.getEmotionalStateChart().exportImage(folder, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }

        writerResults.close();

        writerLatex.appendln("\\fuentecuadro{13}{\\yo}");
        writerLatex.appendln("\\end{tinycuadro}");
        writerLatex.close();

        return super.onEnd();
    }

    private void updateStates(int i) {
        socialEmotionCalculator.clear();

        writeAgentStateHeader(i);

        List<AgentStateWrapper> stateWrappers = simulatorGuiAgent.getAgentConfigurationModels()
                .stream()
                .map(agent -> {
                    AID receiver = myAgent.getAID(agent.getName());

                    AgentAction agentAction = new GetEmotionalState();

                    String stimulusName = "";

                    List<StimulusConfigurationModel> stimuli = agent.getStimulusConfigurations()
                            .stream()
                            .filter(StimulusConfigurationModel::isSelected)
                            .collect(Collectors.toList());

                    if (!stimuli.isEmpty()) {
                        StimulusConfigurationModel randomItem = RandomGenerator.getRandomItem(stimuli);
                        stimulusName = randomItem.getStimulusDefinition().getName();
                        EventStimulus stimulus = new EventStimulus(randomItem.isSelf() ? receiver : myAgent.getAID(), randomItem.getStimulusDefinition().getValue());
                        agentAction = new EvaluateStimulus(stimulus);
                    }

                    AgentState agentState = (AgentState) assistant.sendRequestAction(receiver, agentAction);

                    simulatorGuiAgent.getAgentStateTableModel().add(agentState);

                    EmotionalState emotionalState = agentState.getEmotionState().toEmotionalState();

                    socialEmotionCalculator.addEmotionalState(emotionalState);

                    simulatorGuiAgent.getCentralEmotionChart().addEmotionalState(agent.getName(), emotionalState);
                    simulatorGuiAgent.getEmotionModificationChart().addEmotion(agent.getName(), i, agentState);
                    simulatorGuiAgent.getBehaviourModificationChart().addBehaviourType(agent.getName(), i, agentState);
                    simulatorGuiAgent.getEmotionalStateChart().addEmotionalState(agent.getName(), i, emotionalState);

                    writeAgentState(agent, stimulusName, agentState);
                    return new AgentStateWrapper(stimulusName, agentState);
                })
                .collect(Collectors.toList());

        writeSocialEmotionHeader();

        EmotionalDispersion emotionalDispersion = socialEmotionCalculator.getEmotionalDispersion();
        CentralEmotion centralEmotionalState = socialEmotionCalculator.getCentralEmotion();
        MaximumDistance maximumDistances = socialEmotionCalculator.getMaximumDistance();

        Emotion emotion = AffectiveModel.getInstance().searchEmotion(centralEmotionalState.toEmotionalState());

        writeSocialEmotion(emotionalDispersion, centralEmotionalState, maximumDistances, emotion);
        writeSocialEmotionLatex(stateWrappers.get(0), centralEmotionalState, emotion, maximumDistances, emotionalDispersion, i);

        for (int x = 1; x < stateWrappers.size() - 1; x++) {
            AgentStateWrapper agentState = stateWrappers.get(x);
            writeAgentStateLatex(agentState);
            writerLatex.appendln(StringUtils.repeat("& ", 7) + "\\\\ \\cline{2-6}");
        }

        if (stateWrappers.size() > 1) {
            writeAgentStateLatex(stateWrappers.get(stateWrappers.size() - 1));
            writerLatex.appendln(StringUtils.repeat("& ", 7) + "\\\\ \\midrule[1pt]");
        }

        setGuiSocialEmotion();
    }

    private void writeSocialEmotionLatex(AgentStateWrapper agentState, CentralEmotion centralEmotionalState, Emotion emotion, MaximumDistance maximumDistances, EmotionalDispersion emotionalDispersion, int i) {
        int agentCount = simulatorGuiAgent.getAgentConfigurationModels().size();

        writerLatex.append("\\multirow{" + agentCount + "}{*}{" + i + "} ");

        writeAgentStateLatex(agentState);

        String socialEmotionRow = StringUtils.repeat("& \\multirow{" + agentCount + "}{*}{%s} ", 7);

        writerLatex.appendln(socialEmotionRow + " \\\\" + (agentCount > 1 ? "\\cline{2-6}" : "\\midrule[1pt]"),
                StringFormatter.toString(centralEmotionalState.getActivation()),
                StringFormatter.toString(centralEmotionalState.getSatisfaction()),
                translation.get(emotion.getName().toLowerCase()),
                StringFormatter.toString(maximumDistances.getActivation()),
                StringFormatter.toString(maximumDistances.getSatisfaction()),
                StringFormatter.toString(emotionalDispersion.getActivation()),
                StringFormatter.toString(emotionalDispersion.getSatisfaction())
        );
    }

    private void writeAgentStateLatex(AgentStateWrapper agentState) {
        writerLatex.append("& %s & %s & %s & %s & %s ",
                agentState.getAgentState().getAgent().getLocalName(),
                agentState.getStimulusName(),
                StringFormatter.toString(agentState.getAgentState().getEmotionState().getActivation()),
                StringFormatter.toString(agentState.getAgentState().getEmotionState().getSatisfaction()),
                translation.get(agentState.getAgentState().getEmotionState().getName().toLowerCase()));
    }

    private void writeSocialEmotion(EmotionalDispersion emotionalDispersion, CentralEmotion centralEmotionalState, MaximumDistance maximumDistances, Emotion emotion) {
        writerResults.appendln("%30s: %s %s - %s",
                translation.get("gui.central_emotion"),
                StringFormatter.toStringPoint(centralEmotionalState.getActivation(),
                        centralEmotionalState.getSatisfaction()),
                translation.get(emotion.getName().toLowerCase()),
                translation.get(emotion.getType().toString().toLowerCase())
        );

        writerResults.appendln("%30s: %s",
                translation.get("gui.maximum_distance"),
                StringFormatter.toStringPoint(maximumDistances.getActivation(),
                        maximumDistances.getSatisfaction())
        );

        writerResults.appendln("%30s: %s",
                translation.get("gui.emotional_dispersion"),
                StringFormatter.toStringPoint(emotionalDispersion.getActivation(),
                        emotionalDispersion.getSatisfaction())
        );
    }

    private void writeSocialEmotionHeader() {
        writerResults.newLine(2);

        writerResults.appendln(translation.get("gui.social_emotion").toUpperCase());
    }

    private void writeAgentState(AgentConfigurationModel agent, Object stimulusName, AgentState agentState) {
        writerResults.appendln(AGENTS_HEADER_FORMAT,
                stimulusName,
                agent.getName(),
                translation.get(agentState.getEmotionState().getName().toLowerCase()),
                translation.get(agentState.getEmotionState().getType().toLowerCase()),
                StringFormatter.toString(agentState.getEmotionState().getActivation()),
                StringFormatter.toString(agentState.getEmotionState().getSatisfaction()),
                translation.get(agentState.getBehaviourState().getType().toLowerCase())
        );
    }

    private void writeAgentStateHeader(int i) {
        writerResults.newLine(2);
        writerResults.appendln("====%s: %s====", translation.get("gui.iteration").toUpperCase(), i);
        writerResults.newLine(2);

        writerResults.appendln(translation.get("gui.current_emotional_states").toUpperCase());

        writerResults.appendln(AGENTS_HEADER_FORMAT,
                translation.get("gui.stimulus"),
                translation.get("gui.agent"),
                translation.get("gui.emotion"),
                translation.get("gui.emotion_type"),
                "A",
                "S",
                translation.get("gui.behaviour")
        );
    }

    private void updateSocialEmotionChart(int i) {
        simulatorGuiAgent.getCentralEmotionChart().addCentralEmotion(socialEmotionCalculator.getCentralEmotion());
        simulatorGuiAgent.getMaximumDistanceChart().addMaximumDistance(i, socialEmotionCalculator.getMaximumDistance());
        simulatorGuiAgent.getEmotionalDispersionChart().addDispersion(i, socialEmotionCalculator.getEmotionalDispersion());
    }

    private void setGuiSocialEmotion() {
        EmotionalDispersion emotionalDispersion = socialEmotionCalculator.getEmotionalDispersion();
        CentralEmotion centralEmotionalState = socialEmotionCalculator.getCentralEmotion();
        MaximumDistance maximumDistance = socialEmotionCalculator.getMaximumDistance();

        simulatorGuiAgent.getSimulatorGui().getCollectiveCentralEmotionalStateLabel()
                .setText(centralEmotionalState.toStringPoint());

        simulatorGuiAgent.getSimulatorGui().getCollectiveCentralEmotionLabel()
                .setText(
                        translate(centralEmotionalState.getEmotion().getName()) +
                                " - " +
                                translate(centralEmotionalState.getEmotion().getType().toString())
                );

        simulatorGuiAgent.getSimulatorGui().getEmotionalDispersionValueLabel()
                .setText(emotionalDispersion.toStringPoint());

        simulatorGuiAgent.getSimulatorGui().getMaxDistanceEmotionValueLabel()
                .setText(maximumDistance.toStringPoint());
    }

    private String translate(String key) {
        return Translation.getInstance().get(key.toLowerCase());
    }

    private void setIteration(int i) {
        simulatorGuiAgent.getSimulatorGui().getIterationLabel().setText(String.valueOf(i));
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
