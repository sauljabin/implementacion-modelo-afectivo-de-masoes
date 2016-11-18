/*
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package masoes.util.string;

import org.apache.commons.lang3.RandomStringUtils;

public class StringGenerator {

    public String getString(int length) {
        return RandomStringUtils.random(length);
    }

}
