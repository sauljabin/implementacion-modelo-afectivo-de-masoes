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

public class KnowledgeClause {

    private String predicate;
    private List<String> arguments = new LinkedList<>();
    private List<String> bodies = new LinkedList<>();

    public KnowledgeClause(String predicate) {
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

    public KnowledgeClause argument(String argument) {
        arguments.add(argument);
        return this;
    }

    public KnowledgeClause arguments(String... arguments) {
        Arrays.stream(arguments).forEach(argument -> argument(argument));
        return this;
    }

    public KnowledgeClause body(String body) {
        bodies.add(body);
        return this;
    }

    public KnowledgeClause bodies(String... bodies) {
        Arrays.stream(bodies).forEach(body -> body(body));
        return this;
    }

}
