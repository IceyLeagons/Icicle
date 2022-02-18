/*
 * Copyright 2021 Tamás Tóth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.iceyleagons.icicle.utilities.file;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.utilities.Asserts;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * Utility methods regarding files.
 *
 * @author TOTHTOMI
 * @version 2.0.0
 * @since 1.0.0
 */
public final class FileUtils {

    /**
     * Will return the file name, but without the extension.
     *
     * @param file the file
     * @return the name of the file
     * @throws IllegalArgumentException if the supplied file is null
     */
    public static String getFileNameWithoutExtension(Path file) throws IllegalStateException {
        Asserts.notNull(file, "File must not be null!");

        return file.getFileName().toString().replaceFirst("[.][^.]+$", "");
    }

    /**
     * Appends {@link System#lineSeparator()} to the supplied string and returns it.
     *
     * @param toWrite the string
     * @return the processed string
     */
    public static String writeLine(String toWrite) {
        return toWrite + System.lineSeparator();
    }

    /**
     * Method will try to create a file if it does not exist.
     * If any exceptions occur during this, an {@link IllegalStateException} will be thrown.
     *
     * @param file         the file
     * @param ignoreErrors if true no exceptions will be thrown (aka. errors will be ignored)
     * @throws IllegalStateException    if file creation fails and ignoreErrors is set to false
     * @throws IllegalArgumentException if the file is null
     */
    public static void createFileIfNotExists(Path file, boolean ignoreErrors) throws IllegalStateException, IllegalArgumentException {
        Asserts.notNull(file, "File must not be null!");

        if (!Files.exists(file)) {
            try {
                Files.createFile(file);
            } catch (IOException e) {
                if (!ignoreErrors)
                    throw new IllegalStateException("Could not create file " + file, e);
            }
        }
    }

    /**
     * Deletes a folder by walking down the file tree and deleting the deepest file then proceeding until reaching the start point.
     * <b>Passed variable can only be a directory, regular file will be ignored.</b>
     *
     * @param file the folder to delete
     */
    public static void deleteDirectory(Path file) {
        if (!Files.isDirectory(file)) return;

        try {
            Files.walk(file)
                    .sorted(Comparator.reverseOrder())
                    .forEach(f -> {
                        try {
                            Files.deleteIfExists(f);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            throw new IllegalStateException("Could not delete directory: " + file);
        }
    }

    /**
     * Method will try to create a folder if it does not exist.
     * If any exceptions occur during this, an {@link IllegalStateException} will be thrown.
     *
     * @param file         the file
     * @param ignoreErrors if true no exceptions will be thrown (aka. errors will be ignored)
     * @throws IllegalStateException    if file creation fails and ignoreErrors is set to false
     * @throws IllegalArgumentException if the file is null
     */
    public static void createDirectoryIfNotExists(Path file, boolean ignoreErrors) throws IllegalStateException, IllegalArgumentException {
        Asserts.notNull(file, "File must not be null!");

        if (!Files.exists(file)) {
            try {
                Files.createDirectory(file);
            } catch (IOException e) {
                if (!ignoreErrors)
                    throw new IllegalStateException("Could not create directory " + file, e);
            }
        }
    }

    /**
     * Appends the specified lines to the file using {@link FileWriter}.
     *
     * @param file          the file to append to
     * @param linesToAppend the lines to append
     * @throws IllegalStateException    if an exception occurs during appending
     * @throws IllegalArgumentException if the passed file is null or the lines are empty
     */
    public static void appendFile(Path file, String... linesToAppend) throws IllegalStateException, IllegalArgumentException {
        Asserts.notNull(file, "File must not be null!");
        Asserts.notEmpty(linesToAppend, "LinesToAppend must not be empty!");

        FileUtils.createFileIfNotExists(file, false);

        try {
            Files.write(file, Arrays.asList(linesToAppend), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred while writing file content.", e);
        }
    }

    /**
     * Copies the content of the folder file into the destination folder file.
     * If a file listed in the folder is directory, it will call this method recursively.
     *
     * @param source      the source file
     * @param destination the destination file
     * @param ignore      the files to ignore
     * @throws IllegalStateException    if something happens during copying
     * @throws IllegalArgumentException if one of the files is null
     */
    public static void copyDirectory(Path source, Path destination, String... ignore) throws IllegalStateException, IllegalArgumentException {
        Asserts.notNull(source, "Source file must not be null!");
        Asserts.notNull(destination, "Destination must not be null!");
        Asserts.state(Files.isDirectory(source), "Source must be a directory!");

        FileUtils.createDirectoryIfNotExists(source, false);
        FileUtils.createDirectoryIfNotExists(destination, false);
        List<String> blacklist = Arrays.asList(ignore);

        try {
            Files.list(source).forEachOrdered(f -> {
                if (Files.isReadable(f) && !blacklist.contains(f.getFileName().toString())) {
                    Path copy = destination.resolve(f.getFileName());
                    if (Files.isDirectory(f)) {
                        copyDirectory(f, copy);
                    } else {
                        copyFile(f, copy);
                    }
                }
            });
        } catch (IOException e) {
            throw new IllegalStateException("Could not copy directory " + source, e);
        }
    }

    /**
     * Copies the content of the source file into the destination file.
     *
     * @param source      the source file
     * @param destination the destination file
     * @throws IllegalStateException    if something happens during copying
     * @throws IllegalArgumentException if one of the files is null
     */
    public static void copyFile(Path source, Path destination) throws IllegalStateException, IllegalArgumentException {
        Asserts.notNull(source, "Source file must not be null!");
        Asserts.notNull(destination, "Destination must not be null!");

        FileUtils.createFileIfNotExists(source, false);
        FileUtils.createFileIfNotExists(destination, false);

        try {
            Files.copy(source, destination);
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred while copying file: " + source, e);
        }
    }

    @SneakyThrows
    public static void downloadTo(Path file, String rawUrl) throws IllegalArgumentException {
        downloadTo(file, rawUrl, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
    }

    @SneakyThrows
    public static void downloadTo(Path file, String rawUrl, String userAgent) throws IllegalArgumentException {
        Asserts.notNull(file, "Destination file must not be null!");
        Asserts.notNull(rawUrl, "Download URL must not be null!");

        try {
            URL url = new URL(rawUrl);

            // TODO user agent
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", userAgent);

            try (InputStream inputStream = connection.getInputStream()) {
                try (OutputStream outputStream = Files.newOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int read;

                    while ((read = inputStream.read(buffer, 0, buffer.length)) >= 0) {
                        outputStream.write(buffer, 0, read);
                    }
                }
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Download URL must be a valid URL!", e);
        }
    }

    public static byte[] getContent(Path file) {
        try {
            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred when attempting to read content of file: " + file, e);
        }
    }

    public static void setContent(Path file, byte[] content) {
        try {
            Files.write(file, content, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred when attempting to set content of file: " + file, e);
        }
    }

    public static String getContent(Path file, boolean appendLineSeparator) {
        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            StringBuilder stringBuilder = new StringBuilder();

            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
                if (appendLineSeparator) stringBuilder.append(System.lineSeparator());
            }

            return stringBuilder.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred when attempting to read content of file: " + file, e);
        }
    }

    public static void setContent(Path file, String content) {
        Asserts.state(!Files.isDirectory(file), "Cannot set content of directories!");
        try {
            Files.writeString(file, content, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new IllegalStateException("Could not set content of file: " + file, e);
        }
    }
}