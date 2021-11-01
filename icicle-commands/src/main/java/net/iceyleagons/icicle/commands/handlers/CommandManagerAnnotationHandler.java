package net.iceyleagons.icicle.commands.handlers;

import net.iceyleagons.icicle.commands.annotations.manager.CommandManager;
import net.iceyleagons.icicle.commands.CommandService;
import net.iceyleagons.icicle.core.annotations.Autowired;
import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

/**
 * @since Nov. 1, 2021
 */
@AnnotationHandler
public class CommandManagerAnnotationHandler implements CustomAutoCreateAnnotationHandler {

    private final CommandService commandService;

    @Autowired
    public CommandManagerAnnotationHandler(CommandService commandService) {
        this.commandService = commandService;
    }

    @Override
    public @NotNull Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Collections.singleton(CommandManager.class);
    }

    @Override
    public void onCreated(Object bean, Class<?> type) {

    }
}
