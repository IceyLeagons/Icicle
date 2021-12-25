/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.core.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import net.iceyleagons.icicle.core.utils.Version;

import java.util.Objects;

@Getter
@AllArgsConstructor(staticName = "of")
public class DependencyNotation {

    private final String author;
    private final String name;
    private final Version version;

    public static DependencyNotation fromString(String string) {
        val splitDep = string.split(":");
        if (splitDep.length == 2)
            // No author, probably Icicle internals/standard libraries.
            return of(null, splitDep[0], new Version(splitDep[1]));
        else if (splitDep.length == 3)
            return of(splitDep[0], splitDep[1], new Version(splitDep[2]));
        else
            throw new IllegalArgumentException("Given string is not a dependency notation.");
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DependencyNotation))
            return false;
        val dep = (DependencyNotation) obj;

        //>= 0
        if (!dep.name.equalsIgnoreCase(name))
            return false;
        if (!Objects.equals(dep.author, author))
            return false;

        return dep.version.compareTo(version) >= 0;
    }
}
