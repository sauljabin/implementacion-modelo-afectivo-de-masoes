/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames;
import language.SemanticLanguage;

public class ServiceBuilder {

    private Ontology ontology;
    private Codec language;
    private String protocol;
    private String name;

    public ServiceBuilder fipaSL() {
        this.language = SemanticLanguage.getInstance();
        return this;
    }

    public ServiceBuilder fipaRequest() {
        this.protocol = FIPANames.InteractionProtocol.FIPA_REQUEST;
        return this;
    }

    public ServiceBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ServiceBuilder ontology(Ontology ontology) {
        this.ontology = ontology;
        return this;
    }

    public ServiceBuilder language(Codec language) {
        this.language = language;
        return this;
    }

    public ServiceBuilder protocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public ServiceDescription build() {
        ServiceDescription serviceDescription = new ServiceDescription();

        if (protocol != null) {
            serviceDescription.addProtocols(protocol);
        }

        if (ontology != null) {
            serviceDescription.addOntologies(ontology.getName());
        }

        if (language != null) {
            serviceDescription.addLanguages(language.getName());
        }

        if (name != null) {
            serviceDescription.setName(name);
            serviceDescription.setType(name);
        }
        return serviceDescription;
    }

}
