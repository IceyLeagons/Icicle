/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.core.python;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.utilities.lang.Experimental;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for constructing {@link PythonExecutable} implementations based on the {@link net.iceyleagons.icicle.core.annotations.execution.PythonFile} annotation.
 * This is called by the {@link net.iceyleagons.icicle.core.proxy.interceptor.python.PythonFileHandler}.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 18, 2022
 */
@Experimental
public class PythonFileHandler {

    /**
     * Creates a new {@link PythonExecutable} instance.
     * Create a {@link Path} from the given rawPath, then calls {@link #createPythonSupplier(Path)}.
     *
     * @param rawPath the path to the python file
     * @return the created instance
     */
    @SneakyThrows
    public static PythonExecutable of(String rawPath) {
        final Path path = Path.of(rawPath);
        final PythonSupplier pythonSupplier = createPythonSupplier(path);

        return params -> pythonSupplier.supply(Arrays.stream(params).map(Object::toString).collect(Collectors.joining()));
    }

    /**
     * Creates a {@link PythonSupplier} instance.
     * This is responsible for actually executing the python file.
     *
     * @param path the path to the python file
     * @return the created instance
     */
    private static PythonSupplier createPythonSupplier(Path path) {
        return arguments -> {
            final ProcessBuilder processBuilder = new ProcessBuilder("python3", path.toAbsolutePath().toString(), arguments);
            processBuilder.redirectErrorStream(true);

            final Process process = processBuilder.start();
            List<String> results = readProcessOutput(process.getInputStream());

            int exitCode = process.waitFor();
            return results;
        };
    }

    /**
     * Utility method to map an {@link InputStream} to a {@link List} of {@link String}.
     * Each list entry corresponds to a line on the output.
     *
     * @param is the input stream
     * @return the resulting list
     * @throws IOException if any I/O error occurs
     */
    private static List<String> readProcessOutput(InputStream is) throws IOException {
        final List<String> result = new ArrayList<>(20);
        try (InputStreamReader inputStreamReader = new InputStreamReader(is)) {
            try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.add(line);
                }
            }
        }

        return result;
    }

    /**
     * Utility interface to abstract our code a little.
     * Once we have built in python interpreter, this will act as an abstraction layer.
     */
    private interface PythonSupplier {

        /**
         * Calls the python file and returns the resulting output.
         *
         * @param arguments the arguments to call the script with
         * @return the output lines
         * @throws Exception if any error occurs
         */
        List<String> supply(String arguments) throws Exception;
    }
}
