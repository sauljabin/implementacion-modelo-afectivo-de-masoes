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
import util.RandomGenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MassiveJsonGenerator {

    private static final String AGENT_NAME = "UsuarioRegistrado";

    public static void main(String[] args) throws IOException {
        AgentTypeDefinitionModel agentTypeDefinitionModel = new AgentTypeDefinitionModel(GenericEmotionalAgent.class, AGENT_NAME);

        List<AgentTypeDefinitionModel> agentTypeDefinitionModels = Arrays.asList(
                agentTypeDefinitionModel
        );

        List<StimulusDefinitionModel> stimulusDefinitionModels = Arrays.asList(
                new StimulusDefinitionModel("Recibir Reconocimiento", 0.05, 0.05, true),
                new StimulusDefinitionModel("Nueva Contribución", 0.03, 0.04, false),
                new StimulusDefinitionModel("Artículo Sobresaliente", 0.06, 0.08, true),
                new StimulusDefinitionModel("Guerra de Ediciones", 0.04, -0.04, true),
                new StimulusDefinitionModel("Artículo Borrado", -0.06, -0.08, true),
                new StimulusDefinitionModel("Artículo Modificado", -0.01, -0.03, true)

        );

        List<StimulusConfigurationModel> stimulusConfigurations = stimulusDefinitionModels.stream()
                .map(stimulusDefinitionModel -> new StimulusConfigurationModel(stimulusDefinitionModel))
                .collect(Collectors.toList());

        List<AgentConfigurationModel> agentConfigurationModels = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            agentConfigurationModels.add(
                    new AgentConfigurationModel(
                            agentTypeDefinitionModel,
                            AGENT_NAME + (i + 1),
                            RandomGenerator.getDouble(-1, 1),
                            RandomGenerator.getDouble(-1, 1),
                            stimulusConfigurations
                    )
            );
        }

        GeneralConfiguration generalConfiguration = new GeneralConfiguration(10, agentTypeDefinitionModels, stimulusDefinitionModels, agentConfigurationModels);

        ObjectMapper mapper = new ObjectMapper();

        File folder = new File("output");
        folder.mkdir();

        File file = new File("output/wikipedia-caso1-escenario3.json");

        mapper.writerWithDefaultPrettyPrinter().writeValue(file, generalConfiguration);
    }

}
