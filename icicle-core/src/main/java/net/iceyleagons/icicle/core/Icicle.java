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

package net.iceyleagons.icicle.core;


import net.iceyleagons.icicle.core.performance.PerformanceLog;
import net.iceyleagons.icicle.core.utils.ExecutionUtils;
import org.reflections.Reflections;

/**
 * Main class of Icicle.
 *
 * @author TOTHTOMI
 * @version 1.1.0
 * @since Aug. 23, 2021
 */
public class Icicle {

    public static final String ICICLE_VERSION = "1.0.0";

    public static final boolean PERFORMANCE_DEBUG = true;

    public static final ClassLoader[] ICICLE_CLASS_LOADERS = new ClassLoader[]{Icicle.class.getClassLoader()};
    public static final Reflections ICICLE_REFLECTIONS = new Reflections("net.iceyleagons.icicle", ICICLE_CLASS_LOADERS);

    public static String getCopyrightText() {
        return "Icicle is licensed under the terms of MIT License.";
    }

    public static String getLoadText() {
        return String.format("Loading Icicle v%s. %s", ICICLE_VERSION, getCopyrightText());
    }

    public static void main(String[] args) throws Exception {
        AbstractIcicleApplication abstractIcicleApplication = new AbstractIcicleApplication("net.iceyleagons.icicle.core", ExecutionUtils.debugHandler()) {
        };

        abstractIcicleApplication.start();
        System.out.println(PerformanceLog.dumpExecutionLog(abstractIcicleApplication));
    }
}
