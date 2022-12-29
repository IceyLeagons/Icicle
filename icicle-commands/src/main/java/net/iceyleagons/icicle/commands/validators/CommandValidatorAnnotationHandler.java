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

package net.iceyleagons.icicle.commands.validators;

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
public class CommandValidatorAnnotationHandler implements CustomAutoCreateAnnotationHandler {

    private final Application application;
    private final CommandService commandService;

    @Autowired
    public CommandValidatorAnnotationHandler(Application application, CommandService commandService) {
        this.application = application;
        this.commandService = commandService;
    }

    @Override
    public @NotNull Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Collections.singleton(CommandValidator.class);
    }

    @Override
    public void onCreated(Object bean, Class<?> type) {
        PerformanceLog.begin(this.application, "Creating CommandValidator", CommandValidatorAnnotationHandler.class);

        if (!(bean instanceof CommandValidatorTemplate)) {
            throw new IllegalStateException("Class " + type.getName() + " is annotated with @CommandValidator but does not implement CommandValidatorTemplate!");
        }

        commandService.getValidatorStore().registerValidator((CommandValidatorTemplate) bean, type.getAnnotation(CommandValidator.class));
        PerformanceLog.end(this.application);
    }
}