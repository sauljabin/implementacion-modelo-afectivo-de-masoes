/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.agent;

import agent.AgentException;
import agent.AgentManagementAssistant;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import knowledge.Knowledge;
import masoes.component.behavioural.BehaviouralComponent;
import masoes.ontology.MasoesOntology;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import util.ServiceBuilder;
import util.ToStringBuilder;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class EmotionalAgent extends Agent {

    private static final String ACTIVATION_OPTION = "a";
    private static final String ACTIVATION = "activation";
    private static final String SATISFACTION_OPTION = "s";
    private static final String SATISFACTION = "satisfaction";
    private static final String KNOWLEDGE_PATH_OPTION = "kp";
    private static final String KNOWLEDGE_PATH = "knowledge-path";
    private static final String KNOWLEDGE_OPTION = "k";
    private static final String KNOWLEDGE = "knowledge";
    private BehaviouralComponent behaviouralComponent;
    private EmotionalAgentLogger logger;
    private AgentManagementAssistant agentManagementAssistant;

    public EmotionalAgent() {
        logger = new EmotionalAgentLogger(this);
        agentManagementAssistant = new AgentManagementAssistant(this);
    }

    @Override
    protected final void setup() {
        try {
            behaviouralComponent = new BehaviouralComponent(this);

            addBehaviour(new EvaluateStimulusBehaviour(this));
            addBehaviour(new ResponseAgentStateBehaviour(this));

            setArguments();

            setUp();

            agentManagementAssistant.register(
                    createService(MasoesOntology.ACTION_EVALUATE_STIMULUS),
                    createService(MasoesOntology.ACTION_GET_EMOTIONAL_STATE)
            );
        } catch (Exception e) {
            logger.exception(e);
            throw e;
        }
    }

    private void setArguments() {
        if (hasArguments()) {
            String[] args = getStringArguments();
            Options options = createOptions();
            CommandLineParser commandLineParser = new DefaultParser();

            try {
                CommandLine commandLine = commandLineParser.parse(options, args);

                if (commandLine.hasOption(ACTIVATION_OPTION)) {
                    getBehaviouralComponent().getCurrentEmotionalState().setActivation(Double.parseDouble(commandLine.getOptionValue(ACTIVATION_OPTION)));
                }

                if (commandLine.hasOption(SATISFACTION_OPTION)) {
                    getBehaviouralComponent().getCurrentEmotionalState().setSatisfaction(Double.parseDouble(commandLine.getOptionValue(SATISFACTION_OPTION)));
                }

                if (commandLine.hasOption(KNOWLEDGE_PATH_OPTION)) {
                    String knowledgePath = commandLine.getOptionValue(KNOWLEDGE_PATH_OPTION);
                    setKnowledgePath(knowledgePath);
                }

                if (commandLine.hasOption(KNOWLEDGE_OPTION)) {
                    String knowledge = commandLine.getOptionValue(KNOWLEDGE_OPTION);
                    setKnowledge(knowledge);
                }
            } catch (ParseException e) {
                throw new AgentException(e);
            }
        }
    }

    private ServiceDescription createService(String serviceName) {
        return new ServiceBuilder()
                .fipaRequest()
                .fipaSL()
                .ontology(MasoesOntology.getInstance())
                .name(serviceName)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("aid", getAID())
                .append("behaviouralComponent", behaviouralComponent)
                .toString();
    }

    public BehaviouralComponent getBehaviouralComponent() {
        return behaviouralComponent;
    }

    private void setKnowledge(String knowledge) {
        getBehaviouralComponent().addKnowledge(new Knowledge(knowledge));
    }

    private void setKnowledgePath(String path) {
        getBehaviouralComponent().addKnowledge(new Knowledge(Paths.get(path)));
    }

    private Options createOptions() {
        Options options = new Options();
        options.addOption(new Option(ACTIVATION_OPTION, ACTIVATION, true, ACTIVATION));
        options.addOption(new Option(SATISFACTION_OPTION, SATISFACTION, true, SATISFACTION));
        options.addOption(new Option(KNOWLEDGE_PATH_OPTION, KNOWLEDGE_PATH, true, KNOWLEDGE_PATH));
        options.addOption(new Option(KNOWLEDGE_OPTION, KNOWLEDGE, true, KNOWLEDGE));
        return options;
    }

    public String[] getStringArguments() {
        return Arrays.asList(getArguments())
                .stream()
                .map(o -> o.toString())
                .collect(Collectors.toList())
                .toArray(new String[getArguments().length]);
    }

    public boolean hasArguments() {
        return getArguments() != null && getArguments().length > 0;
    }

    public abstract void setUp();

}
