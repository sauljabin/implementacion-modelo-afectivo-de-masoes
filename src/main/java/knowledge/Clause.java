/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package knowledge;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Clause {

    private String predicate;
    private List<String> arguments = new LinkedList<>();
    private List<String> bodies = new LinkedList<>();

    public Clause(String predicate) {
        this.predicate = predicate;
    }

    public Knowledge toKnowledge() {
        return new Knowledge(toString());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(predicate);
        stringBuilder.append("(");
        stringBuilder.append(arguments.stream().collect(Collectors.joining(", ")));
        stringBuilder.append(")");

        if (!bodies.isEmpty()) {
            stringBuilder.append(" :- ");
            stringBuilder.append(bodies.stream().collect(Collectors.joining(", ")));
        }

        stringBuilder.append(".");
        return stringBuilder.toString();
    }

    public Clause argument(String argument) {
        arguments.add(argument);
        return this;
    }

    public Clause arguments(String... arguments) {
        Arrays.stream(arguments).forEach(argument -> argument(argument));
        return this;
    }

    public Clause body(String body) {
        bodies.add(body);
        return this;
    }

    public Clause bodies(String... bodies) {
        Arrays.stream(bodies).forEach(body -> body(body));
        return this;
    }

}
