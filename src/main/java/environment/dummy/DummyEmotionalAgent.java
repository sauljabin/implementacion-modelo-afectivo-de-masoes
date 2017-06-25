/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment.dummy;

import agent.AgentException;
import knowledge.Knowledge;
import masoes.agent.EmotionalAgent;
import masoes.component.behavioural.EmotionalState;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DummyEmotionalAgent extends EmotionalAgent {

    private static final String THEORY = "theories/behavioural/dummy/dummyEmotionalAgent.prolog";

    private static final String ACTIVATION_OPTION = "a";
    private static final String ACTIVATION = "activation";

    private static final String SATISFACTION_OPTION = "s";
    private static final String SATISFACTION = "satisfaction";

    private static final String KNOWLEDGE_PATH_OPTION = "kp";
    private static final String KNOWLEDGE_PATH = "knowledge-path";


    private static final String KNOWLEDGE_OPTION = "k";
    private static final String KNOWLEDGE = "knowledge";

    @Override
    public void setUp() {

        if (hasArgs()) {

            EmotionalState emotionalState = new EmotionalState();

            String[] args = getStringArgs();
            Options options = createOptions();
            CommandLineParser commandLineParser = new DefaultParser();

            try {
                CommandLine commandLine = commandLineParser.parse(options, args);

                if (commandLine.hasOption(ACTIVATION_OPTION)) {
                    emotionalState.setActivation(Double.parseDouble(commandLine.getOptionValue(ACTIVATION_OPTION)));
                }

                if (commandLine.hasOption(SATISFACTION_OPTION)) {
                    emotionalState.setSatisfaction(Double.parseDouble(commandLine.getOptionValue(SATISFACTION_OPTION)));
                }

                if (commandLine.hasOption(KNOWLEDGE_PATH_OPTION)) {
                    String knowledgePath = commandLine.getOptionValue(KNOWLEDGE_PATH_OPTION);
                    setKnowledgePath(knowledgePath);
                }

                if (commandLine.hasOption(KNOWLEDGE_OPTION)) {
                    String knowledge = commandLine.getOptionValue(KNOWLEDGE_OPTION);
                    setKnowledge(knowledge);
                }

                if (!commandLine.hasOption(KNOWLEDGE_PATH_OPTION) && !commandLine.hasOption(KNOWLEDGE_OPTION)) {
                    setKnowledgePath(THEORY);
                }
            } catch (ParseException e) {
                throw new AgentException(e);
            }

            getBehaviouralComponent().setEmotionalState(emotionalState);
        } else {
            setKnowledgePath(THEORY);
        }
    }

    private void setKnowledge(String knowledge) {
        getBehaviouralComponent().addKnowledge(new Knowledge(knowledge));
    }

    private void setKnowledgePath(String path) {
        getBehaviouralComponent().addKnowledge(new Knowledge(Paths.get(path)));
    }

    public Options createOptions() {
        Options options = new Options();
        options.addOption(new Option(ACTIVATION_OPTION, ACTIVATION, true, ACTIVATION));
        options.addOption(new Option(SATISFACTION_OPTION, SATISFACTION, true, SATISFACTION));
        options.addOption(new Option(KNOWLEDGE_PATH_OPTION, KNOWLEDGE_PATH, true, KNOWLEDGE_PATH));
        options.addOption(new Option(KNOWLEDGE_OPTION, KNOWLEDGE, true, KNOWLEDGE));
        return options;
    }

    public String[] getStringArgs() {
        return Arrays.asList(getArguments())
                .stream()
                .map(o -> o.toString())
                .collect(Collectors.toList())
                .toArray(new String[getArguments().length]);
    }

    private boolean hasArgs() {
        return getArguments() != null && getArguments().length > 0;
    }

}
