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

package net.iceyleagons.icicle.core.beans.handlers;

import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.bean.Autowired;
import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import net.iceyleagons.icicle.core.annotations.service.GlobalService;
import net.iceyleagons.icicle.core.beans.GlobalServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 25, 2022
 */
@AnnotationHandler
public class GlobalServiceAnnotationHandler implements CustomAutoCreateAnnotationHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalServiceAnnotationHandler.class);
    private final GlobalServiceProvider globalServiceProvider;
    private final Application application;

    @Autowired
    public GlobalServiceAnnotationHandler(Application application) {
        this.globalServiceProvider = application.getGlobalServiceProvider();
        this.application = application;
    }

    @Override
    public @NotNull Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Collections.singleton(GlobalService.class);
    }

    @Override
    public void onCreated(Object bean, Class<?> type) throws Exception {
        if (globalServiceProvider == null) {
            logger.warn("GlobalServiceProvider is unavailable, not registering type: " + type);
            return;
        }
        for (Class<?> anInterface : type.getInterfaces()) {
            if (this.globalServiceProvider.isRegistered(anInterface)) {
                throw new IllegalStateException("Global service " + anInterface.getName() + " is already registered!");
            }

            logger.info("Registering type: " + type + " to GlobalServiceProvider");
            this.globalServiceProvider.registerService(anInterface, type, bean, this.application);
        }
    }
}
