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

import lombok.Getter;
import net.iceyleagons.icicle.utilities.Asserts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A wrapper class for Java's regular {@link Path}, however with many useful extra operations in house,
 * so no need for complementary methods.
 *
 * <p>
 * Please note, that this is just a wrapper class, therefore it does not contain the default {@link Path} methods, for accessing them, please use:
 * {@link #getPath()} ()}
 * </p>
 * <p>
 * Features:
 * <ul>
 *     <li>Automatic file/folder creation when constructing instance</li>
 *     <li>Getting children, also with automatic file/folder creation</li>
 *     <li>Easy file copying/moving</li>
 *     <li>Easy file content getting/setting/appending</li>
 *     <li>Easy deletion (both for files and directories)</li>
 * </ul>
 *
 * As of 3.0.0 methods have been moved from using {@link File} to {@link Path} & {@link Files}
 *
 * @author TOTHTOMI
 * @version 3.0.0
 * @since 1.0.0
 */
@Getter
public class AdvancedFile {

    private final Path path;
    private final boolean directory;

    /**
     * Calls the default constructor of {@link AdvancedFile} ({@link #AdvancedFile(File, boolean)}), with the directory parameter defaulted to false.
     *
     * @param file the file to wrap
     */
    public AdvancedFile(File file) {
        this(file, false);
    }

    /**
     * Creates a new {@link AdvancedFile} instance.
     * <p>
     * The constructor is also, if it does not exist, creates a new directory or file depending on what was provided in the constructor's
     * "directory" parameter. If this is a file that's created by the developers, the directory should be specified by them, however if the {@link AdvancedFile}
     * is created from mapping a directory for example, the directory parameter should be from {@link File#isDirectory()}
     *
     * @param file   the file to wrap
     * @param directory whether the file should be treated as a directory or not
     */
    public AdvancedFile(File file, boolean directory) {
        this(file.toPath(), directory);
    }

    public AdvancedFile(Path path) {
        this(path, false);
    }

    public AdvancedFile(Path path, boolean directory) {
        Asserts.notNull(path, "Path must not be null!");

        this.path = path;
        this.directory = Files.exists(path) ? Files.isDirectory(path) : directory;

        if (directory) FileUtils.createDirectoryIfNotExists(path, false);
        else FileUtils.createFileIfNotExists(path, false);
    }

    /**
     * Deletes this {@link AdvancedFile} by checking whether it's a directory ({@link #isDirectory()}), if it is {@link FileUtils#deleteDirectory(Path)} will be called,
     * otherwise Java's default {@link File#delete()} will be used.
     */
    public void delete() {
        if (isDirectory()) FileUtils.deleteDirectory(this.path);
        else {
            try {
                Files.deleteIfExists(this.path);
            } catch (IOException e) {
                throw new IllegalStateException("Could not delete file: " + this.path);
            }
        }
    }

    /**
     * This method creates an exact copy of this file. Exact copy means:
     * creating a file in the specified destination with the current file's name and content.
     * <p>
     * "ignore" is only used if the file is a directory, for information please see {@link #copyTo(AdvancedFile, String...)}
     *
     * @param destination the destination to copy to
     * @param ignore      the files to ignore
     * @return the resulting {@link AdvancedFile}, <b>Does not match original file</b>
     */
    public AdvancedFile makeExactCopy(Path destination, String... ignore) {
        AdvancedFile copy = new AdvancedFile(destination, isDirectory());
        this.copyTo(copy, ignore);
        return copy;
    }

    /**
     * This method is similar to {@link #createOrGetChild(String)} and {@link #createOrGetChild(String, boolean)}, with the difference being it
     * just creates a new {@link Path}, rather than an {@link AdvancedFile}, therefore automatic creation and type (directory, file) checking is not included.
     *
     * @param name the file name
     * @return the resulting {@link File}
     */
    public Path getChild(String name) {
        Asserts.notNull(name, "Name must not be null!");
        Asserts.state(directory, "File must be a directory!");

        return this.path.resolve(name);
    }

    /**
     * This method will basically create a new advanced file with the specified name, and the current file being the parent directory.
     * This will call {@link #createOrGetChild(String, boolean)} with the default value of "false" for "directory".
     *
     * @param name the file name
     * @return the resulting {@link AdvancedFile}
     */
    public AdvancedFile createOrGetChild(String name) {
        return this.createOrGetChild(name, false);
    }

    /**
     * This method will basically create a new advanced file with the specified name, and the current file being the parent directory.
     * This method does not create the directory or file, that's handled in the {@link AdvancedFile} constructor!
     *
     * @param name   the fileName
     * @param directory whether the file is a directory or not
     * @return the resulting {@link AdvancedFile}
     */
    public AdvancedFile createOrGetChild(String name, boolean directory) {
        Asserts.notNull(name, "Name must not be null!");
        return new AdvancedFile(this.path.resolve(name), directory);
    }

    /**
     * Copies this file to the specified destination file.
     * This method calls: {@link #copyTo(Path, String...)}
     *
     * @param destination the destination to copy to
     * @param ignore      the file names to ignore in the root directory (only used when the file is a directory)
     */
    public void copyTo(AdvancedFile destination, String... ignore) {
        copyTo(destination.getPath(), ignore);
    }

    /**
     * Copies this file to the specified destination file.
     * If this file is a directory the destination must be one as well!
     * <p>
     * Ignore is only used if the file type is Directory.
     * When ignore is specified the file names specified in it will be ignored when copying. <b>NOTE </b> that ignoring only happens in the root folder,
     * this "blacklist" checking is not performed down the file tree!
     *
     * @param destination the destination to copy to
     * @param ignore      the file names to ignore in the root directory (only used when the file is a directory)
     */
    public void copyTo(Path destination, String... ignore) {
        Asserts.notNull(destination, "Destination must not be null!");
        Asserts.state(Files.isDirectory(destination) == this.directory, "Current and destination file does not match file type!");

        if (this.directory) {
            FileUtils.createDirectoryIfNotExists(destination, false);
            FileUtils.copyDirectory(destination, destination, ignore);
        } else {
            FileUtils.createFileIfNotExists(destination, false);
            FileUtils.copyFile(this.path, destination);
        }
    }

    /**
     * Moves this {@link AdvancedFile} to the specified destination, by making an exact copy using {@link #makeExactCopy(Path, String...)} and deleting
     * the original ones with {@link #delete()}
     *
     * @param destination the destination to move to (must be a directory)
     * @return the resulting copy file <b>IT IS DIFFERENT from the original one!</b>
     */
    public AdvancedFile moveFile(AdvancedFile destination) {
        Asserts.state(destination.directory, "Destination must be a directory!");
        AdvancedFile copy = this.makeExactCopy(destination.getPath().resolve(this.path.getFileName()));
        this.delete();
        return copy;
    }

    /**
     * Appends the specified content (lines) to the file, leaving the already existing content intact.
     * This method calls {@link FileUtils#appendFile(Path, String...)}
     *
     * @param content the lines to append
     */
    public void appendToFile(String... content) {
        Asserts.state(!directory, "Cannot write content into directories!");
        FileUtils.appendFile(this.path, content);
    }

    /**
     * Returns this file's name without the file extension using {@link FileUtils#getFileNameWithoutExtension(Path)}
     *
     * @return the fileName
     */
    public String getFilenameWithoutExtension() {
        return FileUtils.getFileNameWithoutExtension(this.path);
    }

    /**
     * Sets the content of the file, overwriting the already existing content.
     * If you don't want to overwrite the content refer to {@link #appendToFile(String...)}
     *
     * @param content the content to set to
     */
    public void setContent(String content) {
        FileUtils.setContent(this.path, content);
    }

    /**
     * Returns the content of the file as a {@link String}.
     * If appendLineSeparator is set to true, after each line {@link System#lineSeparator()} will be appended.
     *
     * @param appendLineSeparator whether to apply line separator
     * @return the resulting {@link String}
     */
    public String getContent(boolean appendLineSeparator) {
        return FileUtils.getContent(this.path, appendLineSeparator);
    }

    public byte[] readAllBytes() {
        return FileUtils.getContent(this.path);
    }

    public void setContent(byte[] content) {
        FileUtils.setContent(this.path, content);
    }

    public void compressSelf() {
        // TODO compression possibly does not work for directories
        try {
            Path tmp = Files.createTempFile(this.path.getParent(), this.path.getFileName().toString(), "tmp");
            this.compressTo(tmp);
            this.setContent(FileUtils.getContent(tmp));
            Files.delete(tmp);
        } catch (IOException e) {
            throw new IllegalStateException("Could not compress file " + this.path, e);
        }
    }

    public void compressTo(AdvancedFile output) {
        try {
            FileZipper.compress(this.path, output.getPath());
        } catch (IOException e) {
            throw new IllegalStateException("Could not compress file " + this.path + " to output: " + output.getPath(), e);
        }
    }

    public void compressTo(Path file) {
        try {
            FileZipper.compress(this.path, file);
        } catch (IOException e) {
            throw new IllegalStateException("Could not compress file " + this.path + " to output: " + file, e);
        }
    }

    public File asFile() {
        return this.path.toFile();
    }
}