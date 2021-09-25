package net.iceyleagons.icicle.utilities.updater;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.utilities.file.FileUtils;
import net.iceyleagons.icicle.utilities.updater.comparator.VersionComparator;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@Getter
@RequiredArgsConstructor
public class Updater {

    private final UpdaterSource updaterSource;
    private final VersionComparator versionComparator;

    private UpdateStatus status;

    public void checkForUpdates(String currentVersion, boolean autoDownload, @Nullable File destination) {
        if (checkForUpdates(currentVersion)) {
            System.out.println("Outdated!"); // TODO proper text with logger and what not

            if (autoDownload && destination != null) {
                downloadLatest(destination);
            }
        }
    }

    public boolean checkForUpdates(String currentVersion) {
        final UpdateStatus check = this.versionComparator.compare(currentVersion, updaterSource.getPluginLatestVersion());
        return check == UpdateStatus.OUTDATED; // false -> no update found, true -> update found
    }

    public void downloadLatest(File to) {
        FileUtils.downloadTo(to, updaterSource.getPluginLatestDownload());
    }
}
