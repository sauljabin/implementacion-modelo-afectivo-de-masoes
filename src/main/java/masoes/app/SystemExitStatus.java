/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.app;

public enum SystemExitStatus {

    FAILURE(-1), SUCCESS(0);

    private final int value;

    private SystemExitStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
