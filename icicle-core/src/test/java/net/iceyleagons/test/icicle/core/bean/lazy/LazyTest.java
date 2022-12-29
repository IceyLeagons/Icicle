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

package net.iceyleagons.test.icicle.core.bean.lazy;

import net.iceyleagons.icicle.core.AbstractIcicleApplication;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.Icicle;
import net.iceyleagons.icicle.core.beans.BeanRegistry;
import net.iceyleagons.icicle.core.utils.ExecutionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Feb. 01, 2022
 */
@DisplayName("Lazy Initialization Test")
public class LazyTest {

    @Test
    public void testLazy() throws Exception {
        if (!Icicle.LOADED)
            Icicle.loadIcicle(null);
        Application app = new AbstractIcicleApplication("net.iceyleagons.test.icicle.core.bean.lazy", ExecutionUtils.debugHandler(), null) {
            @Override
            public String getName() {
                return "test";
            }
        };
        app.start();

        BeanRegistry registry = app.getBeanManager().getBeanRegistry();
        Lazy1 lazy1 = registry.getBeanNullable(Lazy1.class);
        Lazy2 lazy2 = registry.getBeanNullable(Lazy2.class);

        // I test for the strings, as because of the proxying these won't be the same!
        Assertions.assertEquals("Lazy1String", lazy2.lazy1.getTestString());
        Assertions.assertEquals("Lazy1String", lazy1.getTestString());
        Assertions.assertEquals("Lazy2String", lazy1.lazy2.getTestString());
        Assertions.assertEquals("Lazy2String", lazy2.getTestString());

    }
}
