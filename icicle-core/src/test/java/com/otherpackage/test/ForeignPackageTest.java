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

package com.otherpackage.test;

import net.iceyleagons.icicle.core.AbstractIcicleApplication;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.Icicle;
import net.iceyleagons.icicle.core.beans.BeanRegistry;
import net.iceyleagons.icicle.core.performance.PerformanceLog;
import net.iceyleagons.icicle.core.utils.ExecutionUtils;
import net.iceyleagons.test.icicle.core.bean.resolvable.EmptyConstructorService;
import org.junit.jupiter.api.*;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 14, 2022
 */
@DisplayName("Foreign Package Test")
public class ForeignPackageTest {

    private static Application application;
    private static BeanRegistry registry;

    @BeforeAll
    public static void setupIcicle() {
        if (!Icicle.LOADED) Icicle.loadIcicle(null);
        application = new AbstractIcicleApplication("com.otherpackage.test", ExecutionUtils.debugHandler(), null) {
            @Override
            public String getName() {
                return "test";
            }
        };
        registry = application.getBeanManager().getBeanRegistry();
        try {
            application.start();
        } catch (Exception ignore) {
            // Because of tests inside icicle package, this will throw an exception. However we do not care here, so just ignore it.
        }
    }

    @AfterAll
    public static void printPerformance() {
        String log = PerformanceLog.dumpExecutionLog(application);
        System.out.println(log);
    }

    @Test
    public void test() {
        Assertions.assertNotNull(registry.getBeanNullable(ForeignService.class));
    }

}
