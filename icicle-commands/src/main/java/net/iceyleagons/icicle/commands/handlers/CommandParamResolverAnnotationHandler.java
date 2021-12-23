package net.iceyleagons.icicle.commands.handlers;

import net.iceyleagons.icicle.commands.CommandService;
import net.iceyleagons.icicle.commands.annotations.CommandParamResolver;
import net.iceyleagons.icicle.commands.params.CommandParameterResolverTemplate;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.Autowired;
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
 * @since Dec. 16, 2021
 */
@AnnotationHandler
public class CommandParamResolverAnnotationHandler implements CustomAutoCreateAnnotationHandler {

    private final CommandService commandService;
    private final Application application;

    @Autowired
    public CommandParamResolverAnnotationHandler(CommandService commandService, Application application) {
        this.commandService = commandService;
        this.application = application;
    }

    @Override
    public @NotNull Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Collections.singleton(CommandParamResolver.class);
    }

    @Override
    public void onCreated(Object bean, Class<?> type) throws Exception {
        PerformanceLog.begin(this.application, "Creating CommandParamResolver", CommandManagerAnnotationHandler.class);

        if (!(bean instanceof CommandParameterResolverTemplate)) {
            throw new IllegalStateException("Class " + type.getName() + " is annotated with @CommandParamResolver but does not implement CommandParameterResolverTemplate!");
        }

        CommandParameterResolverTemplate resolver = (CommandParameterResolverTemplate) bean;

        for (Class<?> aClass : type.getAnnotation(CommandParamResolver.class).value()) {
            commandService.getParamResolvers().put(aClass, resolver);
        }

        PerformanceLog.end(this.application);
    }
}
