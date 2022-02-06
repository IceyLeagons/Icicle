/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.test.icicle.core.utils;

import net.iceyleagons.icicle.core.utils.Version;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * From: https://gist.github.com/brianguertin/ada4b65c6d1c4f6d3eee3c12b6ce021b
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Feb. 05, 2022
 */
public class VersionTest {

    @Test
    @DisplayName("Parsing of: 1.26.6")
    public void newInstance_withTwoDotRelease_isParsedCorrectly() {
        final Version version = new Version("1.26.6");
        Assertions.assertArrayEquals(version.numbers, new int[]{1, 26, 6});
    }

    @Test
    @DisplayName("Parsing of: 1.26.6-DEBUG")
    public void newInstance_withTwoDotReleaseAndPreReleaseName_isParsedCorrectly() {
        final Version version = new Version("1.26.6-DEBUG");
        Assertions.assertArrayEquals(version.numbers, new int[]{1, 26, 6});
    }

    @Test
    @DisplayName("Comparing 2.0.0 to 1.0.0")
    public void compareTo_withEarlierVersion_isGreaterThan() {
        Assertions.assertEquals(new Version("2.0.0").compareTo(new Version("1.0.0")), 1);
    }

    @Test
    @DisplayName("Comparing 2.0.0 to 2.0.0")
    public void compareTo_withSameVersion_isEqual() {
        Assertions.assertEquals(new Version("2.0.0").compareTo(new Version("2.0.0")), 0);
    }

    @Test
    @DisplayName("Comparing 1.0.0 to 2.0.0")
    public void compareTo_withLaterVersion_isLessThan() {
        Assertions.assertEquals(new Version("1.0.0").compareTo(new Version("2.0.0")), -1);
    }

    @Test
    @DisplayName("Comparing 1 to 1.0.0")
    public void compareTo_withMorePreciseSameVersion_isFalse() {
        Assertions.assertEquals(new Version("1").compareTo(new Version("1.0.0")), 0);
    }

    @Test
    @DisplayName("Comparing 2 to 1.0.0")
    public void compareTo_withMorePreciseEarlierVersion_isFalse() {
        Assertions.assertEquals(new Version("2").compareTo(new Version("1.0.0")), 1);
    }

    @Test
    @DisplayName("Comparing 1 to 1.0.1")
    public void compareTo_withMorePreciseLaterVersion_isLessThan() {
        Assertions.assertEquals(new Version("1").compareTo(new Version("1.0.1")), -1);
    }
}