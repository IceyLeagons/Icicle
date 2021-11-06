package net.iceyleagons.icicle.commands.middleware;

import java.util.HashMap;
import java.util.Map;

/**
 * @since Nov. 1, 2021
 */
public class MiddlewareStore {

    private final Map<Class<?>, CommandMiddlewareTemplate> middlewares = new HashMap<>();

    public void registerMiddleware(CommandMiddlewareTemplate commandMiddlewareTemplate, Class<?> middlewareClass, CommandMiddleware commandMiddleware) {
        Class<?> toReplace = commandMiddleware.replaces();

        if (toReplace != CommandMiddleware.Nothing.class) {
            middlewares.remove(toReplace);
        }

        middlewares.put(middlewareClass, commandMiddlewareTemplate);
        System.out.println("Registered: " + middlewareClass.getName());
    }

    public CommandMiddlewareTemplate[] getMiddlewares() {
        return middlewares.values().toArray(CommandMiddlewareTemplate[]::new);
    }
}
