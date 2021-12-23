package net.iceyleagons.icicle.bukkit.annotations;

import net.iceyleagons.icicle.core.annotations.Autowired;
import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 17, 2021
 */
@AnnotationHandler
public class EventListenerAutoCreateHandler implements CustomAutoCreateAnnotationHandler {

    private final JavaPlugin plugin;

    @Autowired
    public EventListenerAutoCreateHandler(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
    }

    @Override
    public Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Collections.singleton(EventListener.class);
    }

    @Override
    public void onCreated(Object bean, Class<?> type) throws Exception {
        if (!(bean instanceof Listener)) {
            throw new IllegalStateException("Class " + type.getName() + " is marked with @EventListener but does not implement org.bukkit.event.Listener!");
        }

        this.plugin.getServer().getPluginManager().registerEvents((Listener) bean, this.plugin);
    }
}
