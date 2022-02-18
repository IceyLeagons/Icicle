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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class FileZipper {

    /**
     * This will compress the file into the given output file.
     * The input file will not be deleted or modified in any way!
     *
     * @param file   the input
     * @param output the output
     * @throws IOException if something happens during the compression process.
     */
    public static void compress(Path file, Path output) throws IOException {
        try (InputStream inputStream = Files.newInputStream(file)) {
            try (OutputStream outputStream = Files.newOutputStream(output)) {
                try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        gzipOutputStream.write(buffer, 0, len);
                    }
                }
            }
        }
    }

    /**
     * This will decompress the file into the given output file.
     * The input file will not be deleted or modified in any way!
     *
     * @param file   the input
     * @param output the output
     * @throws IOException if something happens during the decompression process
     */
    public static void decompress(Path file, Path output) throws IOException {
        try (InputStream inputStream = Files.newInputStream(file)) {
            try (GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)) {
                try (OutputStream outputStream = Files.newOutputStream(output)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = gzipInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }
                }
            }
        }
    }


    /**
     * Used for checking whether a file is Gzipped or not.
     *
     * @param path the {@link Path} to check
     * @return true if it's gzipped otherwise false
     */
    public static boolean isZipped(Path path) {
        try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")) {
            int val = raf.read() & 0xff | ((raf.read() << 8) & 0xff00);
            return val == GZIPInputStream.GZIP_MAGIC;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
