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
    public static String getFileNameWithoutExtension(File file) throws IllegalStateException {
        Asserts.notNull(file, "File must not be null!");

        return file.getName().replaceFirst("[.][^.]+$", "");
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
    public static void createFileIfNotExists(File file, boolean ignoreErrors) throws IllegalStateException, IllegalArgumentException {
        Asserts.notNull(file, "File must not be null!");

        if (!file.exists()) {
            try {
                if (!file.createNewFile() && !ignoreErrors)
                    throw new IllegalStateException("Could not create file " + file.getName());
            } catch (IOException e) {
                if (!ignoreErrors)
                    throw new IllegalStateException("Could not create file " + file.getName(), e);
            }
        }
    }

    /**
     * Deletes a folder by walking down the file tree and deleting the deepest file then proceeding until reaching the start point.
     * <b>Passed variable can only be a directory, regular file will be ignored.</b>
     *
     * @param file the folder to delete
     */
    @SneakyThrows
    public static void deleteFolder(File file) {
        if (!file.isDirectory()) return;

        Files.walk(file.toPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
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
    public static void createFolderIfNotExists(File file, boolean ignoreErrors) throws IllegalStateException, IllegalArgumentException {
        Asserts.notNull(file, "File must not be null!");

        if (!file.exists()) {
            if (!file.mkdirs() && !ignoreErrors)
                throw new IllegalStateException("Could not create file " + file.getName());
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
    public static void appendFile(File file, String... linesToAppend) throws IllegalStateException, IllegalArgumentException {
        Asserts.notNull(file, "File must not be null!");
        Asserts.notEmpty(linesToAppend, "LinesToAppend must not be empty!");

        FileUtils.createFileIfNotExists(file, false);

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append(String.join(System.lineSeparator(), linesToAppend));
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
    public static void copyFolder(File source, File destination, String... ignore) throws IllegalStateException, IllegalArgumentException {
        Asserts.notNull(source, "Source file must not be null!");
        Asserts.notNull(destination, "Destination must not be null!");

        FileUtils.createFolderIfNotExists(source, false);
        FileUtils.createFolderIfNotExists(destination, false);
        List<String> blacklist = Arrays.asList(ignore);

        for (File f : Objects.requireNonNull(source.listFiles())) {
            if (f.canRead() && !blacklist.contains(f.getName())) {
                File copyFile = new File(destination, f.getName());

                if (f.isDirectory()) {
                    FileUtils.copyFolder(f, copyFile);
                    continue;
                }

                FileUtils.copyFileContent(f, copyFile);
            }
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
    public static void copyFileContent(File source, File destination) throws IllegalStateException, IllegalArgumentException {
        Asserts.notNull(source, "Source file must not be null!");
        Asserts.notNull(destination, "Destination must not be null!");

        FileUtils.createFileIfNotExists(source, false);
        FileUtils.createFileIfNotExists(destination, false);

        try (FileInputStream fileInputStream = new FileInputStream(source)) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(destination)) {
                byte[] buffer = new byte[1024];
                int length;

                while ((length = fileInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred while copying file content.", e);
        }
    }

    @SneakyThrows
    public static void downloadTo(File file, String rawUrl) throws IllegalArgumentException {
        downloadTo(file, rawUrl, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
    }

    @SneakyThrows
    public static void downloadTo(File file, String rawUrl, String userAgent) throws IllegalArgumentException {
        Asserts.notNull(file, "Destination file must not be null!");
        Asserts.notNull(rawUrl, "Download URL must not be null!");

        try {
            URL url = new URL(rawUrl);

            // TODO user agent
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", userAgent);

            try (InputStream inputStream = connection.getInputStream()) {
                try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int read;

                    while ((read = inputStream.read(buffer, 0, buffer.length)) >= 0) {
                        fileOutputStream.write(buffer, 0, read);
                    }
                }
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Download URL must be a valid URL!", e);
        }
    }

    public static String getContent(File file, boolean appendLineSeparator) {
        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8)) {
            StringBuilder stringBuilder = new StringBuilder();

            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
                if (appendLineSeparator) stringBuilder.append(System.lineSeparator());
            }

            return stringBuilder.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred when attempting to read content of file: " + file.getName(), e);
        }
    }
}