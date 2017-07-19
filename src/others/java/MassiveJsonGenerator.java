/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import gui.simulator.GeneralConfiguration;
import gui.simulator.agentconfiguration.AgentConfigurationModel;
import gui.simulator.agenttypedefinition.AgentTypeDefinitionModel;
import gui.simulator.stimulusconfiguration.StimulusConfigurationModel;
import gui.simulator.stimulusdefinition.StimulusDefinitionModel;
import masoes.agent.GenericEmotionalAgent;
import masoes.component.behavioural.Emotion;
import masoes.component.behavioural.emotion.AdmirationEmotion;
import masoes.component.behavioural.emotion.SadnessEmotion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MassiveJsonGenerator {

    private static final String AGENT_NAME = "UsuarioRegistrado";
    private static AgentTypeDefinitionModel agentTypeDefinitionModel;
    private static List<AgentTypeDefinitionModel> agentTypeDefinitionModels;
    private static List<StimulusDefinitionModel> stimulusDefinitionModels;

    public static void main(String[] args) throws IOException {
        initDefinitions();
        escenario3();
        escenario4();
    }

    private static void escenario4() throws IOException {
        List<StimulusConfigurationModel> stimulusConfigurations = makeStimulusConfigurationList();

        stimulusConfigurations.get(3).setSelected(false);
        stimulusConfigurations.get(4).setSelected(false);
        stimulusConfigurations.get(5).setSelected(false);

        List<AgentConfigurationModel> agentConfigurationModels = new ArrayList<>();

        Emotion emotion = new SadnessEmotion();

        fillAgentConfigurationList(stimulusConfigurations, agentConfigurationModels, emotion, 1, 25);

        stimulusConfigurations = makeStimulusConfigurationList();

        stimulusConfigurations.get(0).setSelected(false);
        stimulusConfigurations.get(1).setSelected(false);
        stimulusConfigurations.get(2).setSelected(false);

        emotion = new AdmirationEmotion();

        fillAgentConfigurationList(stimulusConfigurations, agentConfigurationModels, emotion, 26, 50);

        save(agentConfigurationModels, "output/wikipedia-caso1-escenario4.json");
    }

    private static void fillAgentConfigurationList(List<StimulusConfigurationModel> stimulusConfigurations, List<AgentConfigurationModel> agentConfigurationModels, Emotion emotion, int initialValue, int iterations) {
        for (int i = initialValue; i <= iterations; i++) {
            agentConfigurationModels.add(
                    new AgentConfigurationModel(
                            agentTypeDefinitionModel,
                            AGENT_NAME + i,
                            emotion.getRandomEmotionalState(),
                            stimulusConfigurations
                    )
            );
        }
    }

    private static List<StimulusConfigurationModel> makeStimulusConfigurationList() {
        return stimulusDefinitionModels.stream()
                .map(stimulusDefinitionModel -> new StimulusConfigurationModel(stimulusDefinitionModel))
                .collect(Collectors.toList());
    }

    private static void escenario3() throws IOException {
        List<StimulusConfigurationModel> stimulusConfigurations = makeStimulusConfigurationList();

        stimulusConfigurations.get(3).setSelected(false);
        stimulusConfigurations.get(4).setSelected(false);
        stimulusConfigurations.get(5).setSelected(false);

        List<AgentConfigurationModel> agentConfigurationModels = new ArrayList<>();

        Emotion emotion = new SadnessEmotion();

        fillAgentConfigurationList(stimulusConfigurations, agentConfigurationModels, emotion, 1, 50);

        save(agentConfigurationModels, "output/wikipedia-caso1-escenario3.json");
    }

    private static void initDefinitions() {
        agentTypeDefinitionModel = new AgentTypeDefinitionModel(GenericEmotionalAgent.class, AGENT_NAME);

        agentTypeDefinitionModels = Arrays.asList(
                agentTypeDefinitionModel
        );

        stimulusDefinitionModels = Arrays.asList(
                new StimulusDefinitionModel("Artículo Nuevo", 0.05, 0.05, true),
                new StimulusDefinitionModel("Nueva Edición", 0.03, 0.04, false),
                new StimulusDefinitionModel("Artículo Sobresaliente", 0.08, 0.08, true),
                new StimulusDefinitionModel("Guerra de Ediciones", -0.08, -0.08, true),
                new StimulusDefinitionModel("Artículo Borrado", -0.06, -0.06, true),
                new StimulusDefinitionModel("Artículo Modificado", -0.02, -0.03, true)

        );
    }

    private static void save(List<AgentConfigurationModel> agentConfigurationModels, String fileName) throws IOException {
        GeneralConfiguration generalConfiguration = new GeneralConfiguration(100, agentTypeDefinitionModels, stimulusDefinitionModels, agentConfigurationModels);

        ObjectMapper mapper = new ObjectMapper();

        File folder = new File("output");
        folder.mkdir();

        File file = new File(fileName);

        mapper.writerWithDefaultPrettyPrinter().writeValue(file, generalConfiguration);
    }

}
