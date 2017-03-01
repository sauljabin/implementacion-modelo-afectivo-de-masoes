/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.colective;

import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import ontology.masoes.MasoesOntology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.FunctionalTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

public class DataPersistenceAgentFunctionalTest extends FunctionalTest {

    private AID agentAID;

    @Before
    public void setUp() {
        agentAID = createAgent(DataPersistenceAgent.class, null);
    }

    @After
    public void tearDown() throws Exception {
        killAgent(agentAID);
    }

    @Test
    public void shouldGetAllServicesFromDF() {
        List<ServiceDescription> services = services(agentAID);
        List<String> results = services.stream().map(ServiceDescription::getName).collect(Collectors.toList());
        assertThat(results, hasItems(
                MasoesOntology.ACTION_GET_OBJECT,
                MasoesOntology.ACTION_CREATE_OBJECT,
                MasoesOntology.ACTION_DELETE_OBJECT,
                MasoesOntology.ACTION_UPDATE_OBJECT
        ));
    }

}