/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.ontology.knowledge;

import jade.content.AgentAction;
import util.ToStringBuilder;

public class UpdateObject implements AgentAction {

    private ObjectEnvironment objectEnvironment;

    public UpdateObject() {
    }

    public UpdateObject(ObjectEnvironment objectEnvironment) {
        this.objectEnvironment = objectEnvironment;
    }

    public ObjectEnvironment getObjectEnvironment() {
        return objectEnvironment;
    }

    public void setObjectEnvironment(ObjectEnvironment objectEnvironment) {
        this.objectEnvironment = objectEnvironment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder()
                .append("objectEnvironment", objectEnvironment)
                .toString();
    }

}
