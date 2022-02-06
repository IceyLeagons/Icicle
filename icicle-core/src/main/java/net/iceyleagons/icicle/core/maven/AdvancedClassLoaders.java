/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.core.maven;

import lombok.NonNull;
import net.iceyleagons.icicle.core.maven.loaders.AdvancedClassLoader;
import net.iceyleagons.icicle.core.maven.loaders.ReflectionsLoader;
import net.iceyleagons.icicle.core.maven.loaders.UnsafeLoader;
import net.iceyleagons.icicle.utilities.lang.Experimental;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 26, 2021
 */
@Experimental
public class AdvancedClassLoaders {

    public static AdvancedClassLoader get(URLClassLoader origin) {
        if (ReflectionsLoader.isSupported()) {
            return new ReflectionsLoader(origin);
        } else if (UnsafeLoader.isSupported()) {
            return new UnsafeLoader(origin);
        } else {
            return new AdvancedClassLoader() {
                @Override
                public void addUrl(@NonNull URL url) {
                    throw new UnsupportedOperationException("Current runtime does not support AdvancedClassLoaders!");
                }

                @Override
                public void loadLibrary(@NonNull File file) throws MalformedURLException {
                    throw new UnsupportedOperationException("Current runtime does not support AdvancedClassLoaders!");
                }

                @Override
                public URLClassLoader getOrigin() {
                    return null;
                }
            };
        }
    }
}
