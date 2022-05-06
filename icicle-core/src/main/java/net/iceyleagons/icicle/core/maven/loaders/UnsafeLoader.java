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
import sun.misc.Unsafe;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Feb. 06, 2022
 */
public class UnsafeLoader implements AdvancedClassLoader {

    private static Unsafe unsafe;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            unsafe = null;
        }
    }

    private final ArrayDeque<URL> unopenedURLs;
    private final ArrayList<URL> pathURLs;
    private final URLClassLoader origin;

    @SuppressWarnings("unchecked")
    public UnsafeLoader(URLClassLoader origin) {
        this.origin = origin;
        ArrayDeque<URL> unopenedURLs;
        ArrayList<URL> pathURLs;

        try {
            Object ucp = getField(URLClassLoader.class, origin, "ucp");
            unopenedURLs = (ArrayDeque<URL>) getField(ucp.getClass(), ucp, "unopenedUrls");
            pathURLs = (ArrayList<URL>) getField(ucp.getClass(), ucp, "path");
        } catch (Exception e) {
            unopenedURLs = null;
            pathURLs = null;
        }

        this.unopenedURLs = unopenedURLs;
        this.pathURLs = pathURLs;
    }

    public static boolean isSupported() {
        return unsafe != null;
    }

    private static Object getField(final Class<?> clazz, final Object object, final String name) throws NoSuchFieldException {
        Field f = clazz.getDeclaredField(name);
        long o = unsafe.objectFieldOffset(f);
        return unsafe.getObject(object, o);
    }

    @Override
    public void addUrl(@NonNull URL url) {
        synchronized (unopenedURLs) {
            if (!pathURLs.contains(url)) {
                unopenedURLs.addLast(url);
                pathURLs.add(url);
            }
        }

        // this.unopenedURLs.add(url);
        // this.pathURLs.add(url);
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
