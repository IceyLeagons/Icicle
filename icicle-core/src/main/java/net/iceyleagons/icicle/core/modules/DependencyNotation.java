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
}
