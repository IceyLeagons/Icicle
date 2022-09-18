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

package net.iceyleagons.icicle.commands.middleware;

import net.iceyleagons.icicle.commands.CommandService;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.bean.Autowired;
import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import net.iceyleagons.icicle.core.performance.PerformanceLog;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sept. 11, 2022
 */
@AnnotationHandler
public class CommandMiddlewareAnnotationHandler implements CustomAutoCreateAnnotationHandler {

    private final CommandService commandService;
    private final Application application;

    @Autowired
    public CommandMiddlewareAnnotationHandler(Application application, CommandService commandService) {
        this.application = application;
        this.commandService = commandService;
    }

    @Override
    public @NotNull Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Collections.singleton(CommandMiddleware.class);
    }

    @Override
    public void onCreated(Object bean, Class<?> type) {
        PerformanceLog.begin(this.application, "Creating CommandMiddleware", CommandMiddlewareAnnotationHandler.class);

        if (!(bean instanceof CommandMiddlewareTemplate)) {
            throw new IllegalStateException("Class " + type.getName() + " is annotated with @CommandMiddleware but does not implement CommandMiddlewareTemplate!");
        }

        commandService.getMiddlewareStore().registerMiddleware((CommandMiddlewareTemplate) bean, type, type.getAnnotation(CommandMiddleware.class));
        PerformanceLog.end(this.application);
    }
}
