/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package gui.simulator;

import environment.Environment;
import gui.simulator.agentconfiguration.AgentConfigurationModel;
import gui.simulator.stimulusconfiguration.StimulusConfigurationModel;
import gui.simulator.stimulusdefinition.StimulusDefinitionModel;
import jade.content.AgentAction;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import masoes.component.behavioural.AffectiveModel;
import masoes.component.behavioural.Emotion;
import masoes.ontology.MasoesOntology;
import masoes.ontology.state.AgentState;
import masoes.ontology.state.GetEmotionalState;
import masoes.ontology.state.collective.CentralEmotion;
import masoes.ontology.state.collective.EmotionalDispersion;
import masoes.ontology.state.collective.GetSocialEmotion;
import masoes.ontology.state.collective.MaximumDistance;
import masoes.ontology.state.collective.SocialEmotion;
import ontology.OntologyAssistant;
import org.apache.commons.lang3.StringUtils;
import translate.Translation;
import util.StringFormatter;
import util.TextFileWriter;
import util.ToStringBuilder;

import java.io.File;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class SimulatorGuiAgentInitialBehaviour extends OneShotBehaviour {

    public static final String FILE_RESULT_OUTPUT = "results.txt";
    public static final String FOLDER_RESULT_OUTPUT = "output/simulation";
    public static final String FILE_RESULT_OUTPUT_LATEX = "resultsLatex.txt";

    private static final String STIMULI_HEADER_FORMAT = "%40s %40s %7s %7s %15s";
    private static final String AGENTS_HEADER_FORMAT = "%25s %20s %25s %7s %7s %15s| %s";
    private Translation translation = Translation.getInstance();
    private SimulatorGuiAgent simulatorGuiAgent;
    private OntologyAssistant assistant;
    private TextFileWriter writerResults;
    private TextFileWriter writerLatex;

    public SimulatorGuiAgentInitialBehaviour(SimulatorGuiAgent simulatorGuiAgent) {
        this.simulatorGuiAgent = simulatorGuiAgent;
        assistant = new OntologyAssistant(simulatorGuiAgent, MasoesOntology.getInstance());
    }

    @Override
    public int onEnd() {
        try {
            File folder = new File("output/simulation");
            folder.mkdir();
            simulatorGuiAgent.getCentralEmotionChart().exportImage(folder, translation.get("gui.central_emotion") + "_initial", 400);
        } catch (Exception e) {
            e.printStackTrace();
        }
        writerResults.close();
        writerLatex.close();
        return super.onEnd();
    }

    @Override
    public void onStart() {
        writerResults = new TextFileWriter(FOLDER_RESULT_OUTPUT, FILE_RESULT_OUTPUT);
        writerLatex = new TextFileWriter(FOLDER_RESULT_OUTPUT, FILE_RESULT_OUTPUT_LATEX);
    }

    @Override
    public void action() {
        writeHeaderStimulus();

        simulatorGuiAgent.getStimulusDefinitionModels().forEach(stimulus ->
                writeStimulus(stimulus)
        );

        writeHeaderAgentState();
        writeHeaderLatex();

        List<AgentState> agentStates = simulatorGuiAgent.getAgentConfigurationModels().stream().map(agent -> {
            AID receiver = myAgent.getAID(agent.getName());

            AgentAction agentAction = new GetEmotionalState();

            AgentState agentState = (AgentState) assistant.sendRequestAction(receiver, agentAction);
            simulatorGuiAgent.getCentralEmotionChart().addEmotionalState(agent.getName(), agentState.getEmotionState().toEmotionalState());

            writeAgentState(agent, agentState);
            return agentState;
        }).collect(toList());

        SocialEmotion socialEmotion = (SocialEmotion) assistant.sendRequestAction(
                myAgent.getAID(Environment.SOCIAL_EMOTION_AGENT),
                new GetSocialEmotion()
        );

        writeSocialEmotionHeader();

        EmotionalDispersion emotionalDispersion = socialEmotion.getEmotionalDispersion();
        CentralEmotion centralEmotionalState = socialEmotion.getCentralEmotion();
        MaximumDistance maximumDistances = socialEmotion.getMaximumDistance();

        simulatorGuiAgent.getCentralEmotionChart().addCentralEmotion(centralEmotionalState);

        Emotion emotion = AffectiveModel.getInstance().searchEmotion(centralEmotionalState.toEmotionalState());

        writeSocialEmotion(emotionalDispersion, centralEmotionalState, maximumDistances, emotion);
        writeSocialEmotionLatex(agentStates.get(0), centralEmotionalState, emotion, maximumDistances, emotionalDispersion);

        for (int i = 1; i < agentStates.size() - 1; i++) {
            AgentState agentState = agentStates.get(i);
            writeAgentStateLatex(agentState);
            writerLatex.appendln(StringUtils.repeat("& ", 7) + "\\\\ \\cline{2-7}");
        }

        if (agentStates.size() > 1) {
            writeAgentStateLatex(agentStates.get(agentStates.size() - 1));
            writerLatex.appendln(StringUtils.repeat("& ", 7) + "\\\\ \\midrule[1pt]");
        }
    }

    private void writeSocialEmotionLatex(AgentState agentState, CentralEmotion centralEmotionalState, Emotion emotion, MaximumDistance maximumDistances, EmotionalDispersion emotionalDispersion) {
        int agentCount = simulatorGuiAgent.getAgentConfigurationModels().size();

        writerLatex.append("\\multirow{" + agentCount + "}{*}{0} ");

        writeAgentStateLatex(agentState);

        String socialEmotionRow = StringUtils.repeat("& \\multirow{" + agentCount + "}{*}{%s} ", 7);

        writerLatex.appendln(socialEmotionRow + " \\\\" + (agentCount > 1 ? "\\cline{2-7}" : "\\midrule[1pt]"),
                StringFormatter.toString(centralEmotionalState.getActivation()),
                StringFormatter.toString(centralEmotionalState.getSatisfaction()),
                translation.get(emotion.getName().toLowerCase()),
                StringFormatter.toString(maximumDistances.getActivation()),
                StringFormatter.toString(maximumDistances.getSatisfaction()),
                StringFormatter.toString(emotionalDispersion.getActivation()),
                StringFormatter.toString(emotionalDispersion.getSatisfaction())
        );
    }

    private void writeAgentStateLatex(AgentState agentState) {
        writerLatex.append("& %s & & %s & %s & %s & %s ",
                agentState.getAgent().getLocalName(),
                StringFormatter.toString(agentState.getEmotionState().getActivation()),
                StringFormatter.toString(agentState.getEmotionState().getSatisfaction()),
                translation.get(agentState.getEmotionState().getName().toLowerCase()),
                translation.get(agentState.getBehaviourState().getType().toLowerCase())
        );
    }

    private void writeHeaderLatex() {
        writerLatex.appendln("\\begin{tinycuadro}[etiqueta=, titulo={}]{c|l|l|c|c|l|l|c|c|l|c|c|c|c}");
        writerLatex.appendln("\\toprule");
        writerLatex.appendln("\\multirow{2}{*}{\\textbf{Iteración}} & \\multicolumn{6}{c|}{\\textbf{Agentes}} &  \\multicolumn{3}{c|}{\\textbf{$EC(Ag)$}} & \\multicolumn{2}{c|}{\\textbf{$m(Ag)$}} & \\multicolumn{2}{c}{\\textbf{$\\sigma(Ag)$}} \\\\ \\cline{2-14}");
        writerLatex.appendln("& Nombre & Estímulo & A & S & Emoción & Comportamiento & A & S & Emoción & A & S & A & S \\\\");
        writerLatex.appendln("\\midrule[1pt]");
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

    private void writeAgentState(AgentConfigurationModel agent, AgentState agentState) {
        writerResults.appendln(AGENTS_HEADER_FORMAT,
                agent.getName(),
                translation.get(agentState.getEmotionState().getName().toLowerCase()),
                translation.get(agentState.getEmotionState().getType().toLowerCase()),
                StringFormatter.toString(agentState.getEmotionState().getActivation()),
                StringFormatter.toString(agentState.getEmotionState().getSatisfaction()),
                translation.get(agentState.getBehaviourState().getType().toLowerCase()),
                agent.getStimulusConfigurations()
                        .stream()
                        .filter(StimulusConfigurationModel::isSelected)
                        .map(stimulusConfiguration ->
                                new ToStringBuilder()
                                        .append(translation.get("gui.stimulus"), stimulusConfiguration.getStimulusDefinition().getName())
                                        .append(translation.get("gui.pa"), StringFormatter.toString(stimulusConfiguration.getActivation()))
                                        .append(translation.get("gui.ps"), StringFormatter.toString(stimulusConfiguration.getSatisfaction()))
                                        .append(translation.get("gui.condition"), stimulusConfiguration.isSelf() ? translation.get("gui.self") : translation.get("gui.others"))
                                        .toString()
                        )
                        .collect(joining(", "))
        );
    }

    private void writeHeaderAgentState() {
        writerResults.newLine(2);

        writerResults.appendln(translation.get("gui.initial_agent_configuration").toUpperCase());

        writerResults.appendln(AGENTS_HEADER_FORMAT,
                translation.get("gui.agent"),
                translation.get("gui.emotion"),
                translation.get("gui.emotion_type"),
                "A",
                "S",
                translation.get("gui.behaviour"),
                translation.get("gui.stimuli")
        );
    }

    private void writeStimulus(StimulusDefinitionModel stimulus) {
        writerResults.appendln(STIMULI_HEADER_FORMAT,
                stimulus.getName(),
                stimulus.getValue(),
                StringFormatter.toString(stimulus.getActivation()),
                StringFormatter.toString(stimulus.getSatisfaction()),
                stimulus.isSelf() ? translation.get("gui.self") : translation.get("gui.others"));
    }

    private void writeHeaderStimulus() {
        writerResults.appendln("%s: %s", translation.get("gui.iterations").toUpperCase(), simulatorGuiAgent.getSimulatorGui().getIterationsSpinner().getValue());

        writerResults.newLine(2);

        writerResults.appendln(translation.get("gui.stimuli_configuration").toUpperCase());

        writerResults.appendln(STIMULI_HEADER_FORMAT,
                translation.get("gui.stimulus"),
                translation.get("gui.value"),
                translation.get("gui.pa"),
                translation.get("gui.ps"),
                translation.get("gui.condition")
        );
    }

}
