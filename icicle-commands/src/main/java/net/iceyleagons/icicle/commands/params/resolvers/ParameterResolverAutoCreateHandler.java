/*
 * MIT License
 *
 * Copyright (c) 2023 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.commands.params.resolvers;

import net.iceyleagons.icicle.commands.annotations.ParameterResolver;
import net.iceyleagons.icicle.commands.manager.CommandService;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.bean.Autowired;
import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.CreateChildren;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 */
@CreateChildren
@AnnotationHandler
public class ParameterResolverAutoCreateHandler implements CustomAutoCreateAnnotationHandler {

    private final CommandService commandService;
    private final Application application;

    @Autowired
    public ParameterResolverAutoCreateHandler(CommandService commandService, Application application) {
        this.commandService = commandService;
        this.application = application;
    }

    @Override
    public @NotNull Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Collections.singleton(ParameterResolver.class);
    }

    @Override
    public void onCreated(Object bean, Class<?> type) throws Exception {
        if (!(bean instanceof ParameterResolverTemplate<?>)) {
            throw new IllegalStateException("Class " + type.getName() + " is annotated with @ParameterResolver but does not implement ParameterResolverTemplate!");
        }
        commandService.getParameterResolverRegistry().registerParameterResolver((ParameterResolverTemplate<?>) bean, type.getAnnotation(ParameterResolver.class));
    }
}
