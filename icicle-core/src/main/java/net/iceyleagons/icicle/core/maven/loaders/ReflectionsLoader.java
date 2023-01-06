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
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.utilities.AdvancedClass;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Primary {@link AdvancedClassLoader}. If supported this is favoured over {@link UnsafeLoader}
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Feb. 06, 2022
 */
@RequiredArgsConstructor
public class ReflectionsLoader implements AdvancedClassLoader {

    private static AdvancedClass<?> clazz;

    static {
        try {
            clazz = new AdvancedClass<>(URLClassLoader.class);
            clazz.preDiscoverMethod("addURL", "addURL", URL.class);
        } catch (Exception e) {
            clazz = null;
        }
    }

    private final URLClassLoader origin;

    /**
     * @return true if this loader is supported by the underlying JVM runtime.
     */
    public static boolean isSupported() {
        return clazz != null;
    }

    @Override
    public void addUrl(@NonNull URL url) {
        clazz.executeMethod("addURL", origin, Void.class, url);
    }

    @Override
    public void loadLibrary(@NonNull File file) throws MalformedURLException {
        this.addUrl(file.toURI().toURL());
    }

    @Override
    public URLClassLoader getOrigin() {
        return this.origin;
    }
}
