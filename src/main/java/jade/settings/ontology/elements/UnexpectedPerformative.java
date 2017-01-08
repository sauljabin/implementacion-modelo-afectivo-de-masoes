/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.settings.ontology.elements;

import jade.content.Predicate;

public class UnexpectedPerformative implements Predicate {

    private String message;
    private String performative;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPerformative() {
        return performative;
    }

    public void setPerformative(String performative) {
        this.performative = performative;
    }

}
