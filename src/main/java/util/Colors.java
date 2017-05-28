/*
 * Copyright (c) 2017 Saúl Piña <sauljabin@gmail.com>
 * License GPLv3 <https://www.gnu.org/licenses/gpl-3.0.html>
 * Please see the LICENSE.txt file
 */

package util;

import java.awt.*;
import java.util.Arrays;

public final class Colors {

    private static final Color[] color = {
            new Color(0xff0e10),
            new Color(0x1f57ff),
            new Color(0x3ec864),
            new Color(0xff6bad),
            new Color(0x20c3ff),
            new Color(0xff6e28),
            new Color(0xff20ea),
            new Color(0xb25bff),
            new Color(0x54ff64),
            new Color(0x2a75c8),
            new Color(0xe2da25),
            new Color(0xe6daa6),
            new Color(0xff8645),
            new Color(0x6e750e),
            new Color(0x650021),
            new Color(0xae7181),
            new Color(0x06470c),
            new Color(0x13eac9),
            new Color(0x00ffff),
            new Color(0x24c830),
            new Color(0xd1b26f),
            new Color(0xc79fef),
            new Color(0x06c2ac),
            new Color(0x9a0eea),
            new Color(0x75bbfd),
            new Color(0xffff14),
            new Color(0xc20078),
            new Color(0x014d4e),
            new Color(0x96f97b),
            new Color(0x29386),
            new Color(0x95d0fc),
            new Color(0x653700),
            new Color(0xADBDDF),
            new Color(0x15b01a),
            new Color(0x7e1e9c),
            new Color(0x9aae07),
            new Color(0x7af9ab),
            new Color(0x137e6d),
            new Color(0x610023),
            new Color(0x4b006e),
            new Color(0x8fff9f),
            new Color(0xdbb40c),
            new Color(0xa2cffe),
            new Color(0xc0fb2d),
            new Color(0xbe03fd),
            new Color(0x840000),
            new Color(0xd0fefe),
            new Color(0x3f9b0b),
            new Color(0x01153e),
            new Color(0x04d8b2),
            new Color(0xc04e01),
            new Color(0x0cff0c),
            new Color(0x333DFC),
            new Color(0xcf6275),
            new Color(0x580f41),
            new Color(0xffd1df),
            new Color(0xceb301),
            new Color(0x380282),
            new Color(0xaaff32),
            new Color(0x8f1402),
            new Color(0xaaa662),
            new Color(0x53fca1),
            new Color(0x8e82fe),
            new Color(0xcb416b),
    };

    public static Color getColor(int i) {
        return color.length > i ? color[i] : getRandomColor();
    }

    public static Color getRandomColor() {
        return RandomGenerator.getRandomItem(Arrays.asList(color));
    }

}
