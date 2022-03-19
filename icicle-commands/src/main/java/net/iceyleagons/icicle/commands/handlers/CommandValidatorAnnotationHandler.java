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

package net.iceyleagons.icicle.commands.handlers;

import net.iceyleagons.icicle.commands.CommandService;
import net.iceyleagons.icicle.commands.annotations.CommandParamResolver;
import net.iceyleagons.icicle.commands.middleware.CommandMiddleware;
import net.iceyleagons.icicle.commands.middleware.CommandMiddlewareTemplate;
import net.iceyleagons.icicle.commands.params.CommandParameterResolverTemplate;
import net.iceyleagons.icicle.commands.validators.CommandParameterValidator;
import net.iceyleagons.icicle.commands.validators.CommandValidator;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import net.iceyleagons.icicle.core.performance.PerformanceLog;
import net.iceyleagons.icicle.utilities.lang.Autowired;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 19, 2022
 */
@AnnotationHandler
public class CommandValidatorAnnotationHandler implements CustomAutoCreateAnnotationHandler {

    private final CommandService commandService;
    private final Application application;

    @Autowired
    public CommandValidatorAnnotationHandler(CommandService commandService, Application application) {
        this.commandService = commandService;
        this.application = application;
    }

    @Override
    public @NotNull Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Collections.singleton(CommandValidator.class);
    }

    @Override
    public void onCreated(Object bean, Class<?> type) throws Exception {
        if (bean instanceof CommandParameterValidator) {
            commandService.getValidatorStore().registerValidator((CommandParameterValidator) bean, type.getAnnotation(CommandValidator.class));
        }
    }
}
