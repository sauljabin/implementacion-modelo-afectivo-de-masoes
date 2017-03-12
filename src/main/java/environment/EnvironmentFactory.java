/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package environment;

import org.reflections.Reflections;

import java.util.Optional;
import java.util.Set;

public class EnvironmentFactory {

    public Environment createEnvironment(String environment) {
        Reflections reflections = new Reflections(getClass().getPackage().getName());

        Set<Class<? extends Environment>> subTypes = reflections.getSubTypesOf(Environment.class);

        Optional<Class<? extends Environment>> first = subTypes.stream()
                .filter(aClass -> aClass.getSimpleName().toLowerCase().contains(environment.toLowerCase()))
                .findFirst();

        if (first.isPresent()) {
            try {
                return first.get().newInstance();
            } catch (Exception e) {
                throw new InvalidEnvironmentException(e);
            }
        } else {
            throw new InvalidEnvironmentException(String.format("Invalid environment name \"%s\"", environment));
        }
    }

}
