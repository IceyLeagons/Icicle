package net.iceyleagons.icicle.utilities.updater.comparator;

import net.iceyleagons.icicle.utilities.updater.UpdateStatus;

public interface VersionComparator {

    UpdateStatus compare(String local, String remote);

    default VersionComparator getDefault() {
        return new SemanticVersionComparator();
    }
}
