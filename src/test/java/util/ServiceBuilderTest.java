/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames;
import language.SemanticLanguage;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ServiceBuilderTest {

    private ServiceBuilder serviceBuilder;

    @Before
    public void setUp() {
        serviceBuilder = new ServiceBuilder();
    }

    @Test
    public void shouldSetOntology() {
        Ontology ontologyMock = mock(Ontology.class);
        String expectedOntologyName = "expectedOntologyName";
        doReturn(expectedOntologyName).when(ontologyMock).getName();
        ServiceDescription serviceDescription = serviceBuilder
                .ontology(ontologyMock)
                .build();
        assertThat(serviceDescription.getAllOntologies().next(), is(expectedOntologyName));
    }

    @Test
    public void shouldSetName() {
        String expectedName = "expectedName";
        ServiceDescription serviceDescription = serviceBuilder
                .name(expectedName)
                .build();
        assertThat(serviceDescription.getName(), is(expectedName));
        assertThat(serviceDescription.getType(), is(expectedName));
    }

    @Test
    public void shouldSetLanguage() {
        Codec languageMock = mock(Codec.class);
        String expectedLanguageName = "expectedLanguageName";
        doReturn(expectedLanguageName).when(languageMock).getName();
        ServiceDescription serviceDescription = serviceBuilder
                .language(languageMock)
                .build();
        assertThat(serviceDescription.getAllLanguages().next(), is(expectedLanguageName));
    }

    @Test
    public void shouldSetSemanticLanguage() {
        ServiceDescription serviceDescription = serviceBuilder
                .fipaSL()
                .build();
        assertThat(serviceDescription.getAllLanguages().next(), is(SemanticLanguage.getInstance().getName()));
    }


    @Test
    public void shouldSetProtocol() {
        String expectedProtocol = "expectedProtocol";
        ServiceDescription serviceDescription = serviceBuilder
                .protocol(expectedProtocol)
                .build();
        assertThat(serviceDescription.getAllProtocols().next(), is(expectedProtocol));
    }

    @Test
    public void shouldSetRequestProtocol() {
        ServiceDescription serviceDescription = serviceBuilder
                .fipaRequest()
                .build();
        assertThat(serviceDescription.getAllProtocols().next(), is(FIPANames.InteractionProtocol.FIPA_REQUEST));
    }

}