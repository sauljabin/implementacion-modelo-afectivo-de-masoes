/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes;

import jade.core.AID;
import jade.util.leap.List;
import util.ToStringBuilder;

public class ObjectStimulus extends Stimulus {

    private AID creator;
    private String objectName;
    private List objectProperties;

    public ObjectStimulus() {
    }

    public ObjectStimulus(AID creator, String objectName, List objectProperties) {
        this.creator = creator;
        this.objectName = objectName;
        this.objectProperties = objectProperties;
    }

    public AID getCreator() {
        return creator;
    }

    public void setCreator(AID creator) {
        this.creator = creator;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public List getObjectProperties() {
        return objectProperties;
    }

    public void setObjectProperties(List objectProperties) {
        this.objectProperties = objectProperties;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("creator", creator)
                .append("objectName", objectName)
                .append("objectProperties", objectProperties)
                .toString();
    }

}
