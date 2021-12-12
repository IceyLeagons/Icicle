package net.iceyleagons.icicle.commands.handlers;

import net.iceyleagons.icicle.commands.CommandService;
import net.iceyleagons.icicle.commands.annotations.manager.CommandManager;
import net.iceyleagons.icicle.commands.manager.RegisteredCommandManager;
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
 * @since Nov. 1, 2021
 */
@AnnotationHandler
public class CommandManagerAnnotationHandler implements CustomAutoCreateAnnotationHandler {

    private final CommandService commandService;
    private final Application application;

    @Autowired
    public CommandManagerAnnotationHandler(CommandService commandService, Application application) {
        this.commandService = commandService;
        this.application = application;
    }

    @Override
    public @NotNull Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Collections.singleton(CommandManager.class);
    }

    @Override
    public void onCreated(Object bean, Class<?> type) throws Exception {
        PerformanceLog.begin(application, "Creating CommandManager", CommandManagerAnnotationHandler.class);

        RegisteredCommandManager registeredCommandManager =
                new RegisteredCommandManager(this.application, this.commandService, type.getAnnotation(CommandManager.class), type, bean);

        commandService.getCommandManagers().add(registeredCommandManager);

        PerformanceLog.end(application);
    }
}
