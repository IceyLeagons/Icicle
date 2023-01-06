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

package net.iceyleagons.icicle.core.maven.loaders;

import lombok.NonNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

/**
 * This is used as an abstraction for us to be able to switch between supported class loaders.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Feb. 06, 2022
 */
public interface AdvancedClassLoader {

    /**
     * Adds the URL to the classloader
     *
     * @param url the url to add
     */
    void addUrl(@NonNull URL url);

    /**
     * Loads a library from the given file.
     * It calls {@link #addUrl(URL)}
     *
     * @param file the file
     * @throws MalformedURLException if a URL cannot be constructed from the file
     */
    void loadLibrary(@NonNull File file) throws MalformedURLException;

    /**
     * Loads a library from the given path.
     *
     * @param file the path
     * @throws MalformedURLException if a URL cannot be constructed from the file
     */
    default void loadLibrary(@NonNull Path file) throws MalformedURLException {
        loadLibrary(file.toFile());
    }

    /**
     * @return the parent class loader
     */
    URLClassLoader getOrigin();

}
