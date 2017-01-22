/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package jade.exception;

import jade.content.Predicate;

public class NotUnderstoodException extends RuntimeException {

    private Predicate predicate;

    public NotUnderstoodException(String message) {
        super(message);
    }

    public NotUnderstoodException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotUnderstoodException(Predicate predicate, Throwable cause) {
        super(cause);
        this.predicate = predicate;
    }

    public Predicate getPredicate() {
        return predicate;
    }

}
