/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package ontology.masoes.stimulus;

import jade.core.AID;
import util.ToStringBuilder;

public class ObjectStimulus extends Stimulus {

    private AID creator;
    private String objectName;

    public ObjectStimulus() {
    }

    public ObjectStimulus(AID creator, String objectName) {
        this.creator = creator;
        this.objectName = objectName;
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

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder();
        toStringBuilder.object(this);

        if (creator != null) {
            toStringBuilder.append("creator", creator.getLocalName());
        }

        return toStringBuilder.append("objectName", objectName)
                .toString();
    }

}
