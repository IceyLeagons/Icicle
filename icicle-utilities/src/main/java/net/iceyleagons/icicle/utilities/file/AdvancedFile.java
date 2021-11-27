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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * A wrapper record (class) for Java's regular {@link File}, however with many useful extra operations in house,
 * so no need for complementary methods.
 *
 * <p>
 * Please note, that this is just a wrapper class, therefore it does not contain the default {@link File} methods, for accessing them, please use:
 * {@link #getFile()}
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
 * @author TOTHTOMI
 * @version 2.0.0
 * @since 1.0.0
 */
@Getter
public class AdvancedFile {

    private final File file;
    private final boolean folder;

    /**
     * Calls the default constructor of {@link AdvancedFile} ({@link #AdvancedFile(File, boolean)}), with the folder parameter defaulted to false.
     *
     * @param file the file to wrap
     */
    public AdvancedFile(File file) {
        this(file, false);
    }

    /**
     * Creates a new {@link AdvancedFile} instance.
     * <p>
     * The constructor is also, if does not exist, creates a new folder or file depending on what was provided in the constructor's
     * "folder" parameter. If this is a file that's created by the developers, the folder should be specified by them, however if the {@link AdvancedFile}
     * is created from mapping a folder for example, the folder parameter should be from {@link File#isDirectory()}
     *
     * @param file   the file to wrap
     * @param folder whether the file should be treated as a folder or not
     */
    public AdvancedFile(File file, boolean folder) {
        Asserts.notNull(file, "File must not be null!");

        this.file = file;
        this.folder = folder;

        if (folder) FileUtils.createFolderIfNotExists(file, false);
        else FileUtils.createFileIfNotExists(file, false);
    }

    /**
     * Deletes this {@link AdvancedFile} by checking whether it's a directory ({@link #isFolder()}), if it is {@link FileUtils#deleteFolder(File)} will be called,
     * otherwise Java's default {@link File#delete()} will be used.
     */
    public void delete() {
        if (isFolder()) FileUtils.deleteFolder(getFile());
        else getFile().delete();
    }

    /**
     * This method creates an exact copy of this file. Exact copy means:
     * creating a file in the specified destination with the current file's name and content.
     * <p>
     * "ignore" is only used if the file is a folder, for information please see {@link #copyTo(AdvancedFile, String...)}
     *
     * @param destination the destination to copy to
     * @param ignore      the files to ignore
     * @return the resulting {@link AdvancedFile}, <b>Does not match original file</b>
     */
    public AdvancedFile makeExactCopy(File destination, String... ignore) {
        AdvancedFile copy = new AdvancedFile(new File(destination, this.file.getName()), isFolder());
        this.copyTo(copy, ignore);
        return copy;
    }

    /**
     * This method is similar to {@link #createOrGetChild(String)} and {@link #createOrGetChild(String, boolean)}, with the difference being it
     * just creates a new {@link File}, rather than an {@link AdvancedFile}, therefore automatic creation and type (directory, file) checking is not included.
     *
     * @param name the file name
     * @return the resulting {@link File}
     */
    public File getChild(String name) {
        Asserts.notNull(name, "Name must not be null!");
        Asserts.state(folder, "File must be a folder!");

        return new File(getFile(), name);
    }

    /**
     * This method will basically create a new advanced file with the specified name, and the current file being the parent folder.
     * This will call {@link #createOrGetChild(String, boolean)} with the default value of "false" for "folder".
     *
     * @param name the file name
     * @return the resulting {@link AdvancedFile}
     */
    public AdvancedFile createOrGetChild(String name) {
        return this.createOrGetChild(name, false);
    }

    /**
     * This method will basically create a new advanced file with the specified name, and the current file being the parent folder.
     * This method does not create the folder or file, that's handled in the {@link AdvancedFile} constructor!
     *
     * @param name   the fileName
     * @param folder whether the file is a folder or not
     * @return the resulting {@link AdvancedFile}
     */
    public AdvancedFile createOrGetChild(String name, boolean folder) {
        Asserts.notNull(name, "Name must not be null!");

        return new AdvancedFile(new File(getFile(), name), folder);
    }

    /**
     * Copies this file to the specified destination file.
     * This method calls: {@link #copyTo(File, String...)}
     *
     * @param destination the destination to copy to
     * @param ignore      the file names to ignore in the root folder (only used when the file is a directory)
     */
    public void copyTo(AdvancedFile destination, String... ignore) {
        copyTo(destination.getFile(), ignore);
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
     * @param ignore      the file names to ignore in the root folder (only used when the file is a directory)
     */
    public void copyTo(File destination, String... ignore) {
        Asserts.notNull(destination, "Destination must not be null!");
        Asserts.state(destination.isDirectory() == getFile().isDirectory(), "Current and destination file does not match file type!");

        if (folder) {
            FileUtils.createFolderIfNotExists(destination, false);
            FileUtils.copyFolder(file, destination, ignore);
        } else {
            FileUtils.createFileIfNotExists(destination, false);
            FileUtils.copyFileContent(file, destination);
        }
    }

    /**
     * Moves this {@link AdvancedFile} to the specified destination, by making an exact copy using {@link #makeExactCopy(File, String...)} and deleting
     * the original ones either with {@link File#delete()} if it's a file, or {@link FileUtils#deleteFolder(File)} if it's a directory.
     *
     * @param destination the destination to move to (must be a folder)
     * @return the resulting copy file <b>IT IS DIFFERENT from the original one!</b>
     */
    public AdvancedFile moveFile(AdvancedFile destination) {
        Asserts.state(destination.folder, "Destination must be a folder!");

        AdvancedFile copy = this.makeExactCopy(destination.getFile());

        if (this.folder) FileUtils.deleteFolder(this.file);
        else {
            if (!this.file.delete())
                throw new IllegalStateException("Could not delete file: " + file.getName());

        }

        return copy;
    }

    /**
     * Appends the specified content (lines) to the file, leaving the already existing content intact.
     * This method calls {@link FileUtils#appendFile(File, String...)}
     *
     * @param content the lines to append
     */
    public void appendToFile(String... content) {
        Asserts.state(!folder, "Cannot write content into folders!");
        FileUtils.appendFile(file, content);
    }

    /**
     * Returns this file's name without the file extension using {@link FileUtils#getFileNameWithoutExtension(File)}
     *
     * @return the fileName
     */
    public String getFilenameWithoutExtension() {
        return FileUtils.getFileNameWithoutExtension(file);
    }

    /**
     * Sets the content of the file, overwriting the already existing content.
     * If you don't want to overwrite the content refer to {@link #appendToFile(String...)}
     *
     * @param content the content to set to
     */
    public void setContent(String content) {
        Asserts.state(!folder, "Cannot set content of folders!");

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred when attempting to set content of file: " + file.getName(), e);
        }
    }

    /**
     * Returns the content of the file as a {@link String}.
     * If appendLineSeparator is set to true, after each line {@link System#lineSeparator()} will be appended.
     *
     * @param appendLineSeparator whether to apply line separator
     * @return the resulting {@link String}
     */
    public String getContent(boolean appendLineSeparator) {
        return FileUtils.getContent(this.file, appendLineSeparator);
    }

    public byte[] readByteContent() {
        try (InputStream input = new FileInputStream(this.file)) {
            return input.readAllBytes();
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred when attempting to read byte content of file: " + file.getName(), e);
        }
    }

    public void setByteContent(byte[] content) {
        try (OutputStream out = new FileOutputStream(this.file)) {
            out.write(content);
            out.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred when attempting to set byte content of file: " + file.getName(), e);
        }
    }

    public void compressSelf() {
        //TODO set byte content first or what
    }

    public void compressTo(AdvancedFile output) {
        this.compressTo(output.getFile());
    }

    public void compressTo(File file) {
        try {
            FileZipper.compress(getFile(), file);
        } catch (IOException e) {
            throw new IllegalStateException("Error occurred when attempting to compress file " + this.file.getName() + " to " + file.getName(), e);
        }
    }
}