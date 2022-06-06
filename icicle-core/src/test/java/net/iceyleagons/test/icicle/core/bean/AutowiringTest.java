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

package net.iceyleagons.test.icicle.core.bean;

import net.iceyleagons.icicle.core.AbstractIcicleApplication;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.Icicle;
import net.iceyleagons.icicle.core.beans.BeanRegistry;
import net.iceyleagons.icicle.core.utils.ExecutionUtils;
import net.iceyleagons.test.icicle.core.bean.resolvable.custom.CustomAutowiringHandlerService;
import net.iceyleagons.test.icicle.core.bean.resolvable.DependantConstructorService;
import net.iceyleagons.test.icicle.core.bean.resolvable.EmptyConstructorService;
import net.iceyleagons.test.icicle.core.bean.resolvable.qualifier.QualificationTestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Feb. 01, 2022
 */
@DisplayName("Beans & AutoWiring")
public class AutowiringTest {

    private static Application application;
    private static BeanRegistry registry;

    @BeforeAll
    public static void setupIcicle() {
        if (!Icicle.LOADED) Icicle.loadIcicle(null);
        application = new AbstractIcicleApplication("net.iceyleagons.test.icicle.core.bean.resolvable", ExecutionUtils.debugHandler(), null) {
            @Override
            public String getName() {
                return "test";
            }
        };
        registry = application.getBeanManager().getBeanRegistry();
        Assertions.assertDoesNotThrow(application::start);
    }

    @Test
    @DisplayName("Empty constructor bean")
    public void testEmptyConstructor() {
        Assertions.assertNotNull(registry.getBeanNullable(EmptyConstructorService.class));
    }

    @Test
    @DisplayName("Dependency required constructor bean")
    public void testDependencyRequiredConstructor() {
        DependantConstructorService service = registry.getBeanNullable(DependantConstructorService.class);
        Assertions.assertNotNull(service);
        Assertions.assertNotNull(service.application);
        Assertions.assertNotNull(service.emptyConstructorService);
        Assertions.assertEquals(application, service.application);
    }

    @Test
    @DisplayName("@Bean proxy")
    public void testBeanProxy() {
        EmptyConstructorService service = registry.getBeanNullable(EmptyConstructorService.class);
        Assertions.assertNotNull(service);

        EmptyConstructorService.TestBean tb = service.testBean();
        EmptyConstructorService.TestBean tb2 = service.testBean();

        Assertions.assertNotNull(tb);
        Assertions.assertNotNull(tb2);
        Assertions.assertEquals(tb.uuid, tb.uuid);
    }

    @Test
    @DisplayName("Custom AutoWiring-Annotation Handler")
    public void testCustomAutoWiringHandler() {
        CustomAutowiringHandlerService service = registry.getBeanNullable(CustomAutowiringHandlerService.class);
        Assertions.assertNotNull(service);

        Assertions.assertEquals("should", service.should);
        Assertions.assertEquals("asd", service.asd);
    }

    @Test
    @DisplayName("Qualification Test")
    public void testQualifications() {
        QualificationTestService service = registry.getBeanNullable(QualificationTestService.class);
        Assertions.assertNotNull(service);

        Assertions.assertEquals("notQualified", service.getNonQualified().id());
        Assertions.assertEquals("qualified", service.getQualified().id());
        Assertions.assertEquals("qualified2", service.getQualified2().id());
    }
}
