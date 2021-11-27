package net.iceyleagons.icicle.core.translations.impl.file;

import java.io.File;

/**
 * This is a very basic string provider for parsing a CSV file with the following format:
 * language key string
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 27, 2021
 */
public class TSVFileProvider extends SeparatedFileProvider {
    public TSVFileProvider(File... csvFiles) {
        super("\t", csvFiles);
    }
}
