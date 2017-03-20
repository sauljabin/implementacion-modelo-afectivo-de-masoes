/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes.data;

import jade.core.AID;
import jade.util.leap.List;
import util.ToStringBuilder;

public class ObjectEnvironment {

    private String name;
    private AID creator;
    private List objectProperties;

    public ObjectEnvironment() {
    }

    public ObjectEnvironment(AID creator, String name, List objectProperties) {
        this.name = name;
        this.creator = creator;
        this.objectProperties = objectProperties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AID getCreator() {
        return creator;
    }

    public void setCreator(AID creator) {
        this.creator = creator;
    }

    public List getObjectProperties() {
        return objectProperties;
    }

    public void setObjectProperties(List objectProperties) {
        this.objectProperties = objectProperties;
    }

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder();
        toStringBuilder.object(this);

        if (creator != null) {
            toStringBuilder.append("creator", creator.getLocalName());
        }

        return toStringBuilder.append("name", name)
                .append("objectProperties", objectProperties)
                .toString();
    }

}
