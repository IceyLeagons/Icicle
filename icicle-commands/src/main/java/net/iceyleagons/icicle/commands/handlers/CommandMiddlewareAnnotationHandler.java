package net.iceyleagons.icicle.commands.handlers;

import net.iceyleagons.icicle.commands.CommandService;
import net.iceyleagons.icicle.commands.middleware.CommandMiddleware;
import net.iceyleagons.icicle.commands.middleware.CommandMiddlewareTemplate;
import net.iceyleagons.icicle.core.annotations.Autowired;
import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

@AnnotationHandler
public class CommandMiddlewareAnnotationHandler implements CustomAutoCreateAnnotationHandler {

    private final CommandService commandService;

    @Autowired
    public CommandMiddlewareAnnotationHandler(CommandService commandService) {
        this.commandService = commandService;
    }

    @Override
    public @NotNull Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Collections.singleton(CommandMiddleware.class);
    }

    @Override
    public void onCreated(Object bean, Class<?> type) {
        if (bean instanceof CommandMiddlewareTemplate) {
            commandService.getMiddlewareStore().registerMiddleware((CommandMiddlewareTemplate) bean, type, type.getAnnotation(CommandMiddleware.class));
        }
    }
}
