package net.iceyleagons.icicle.commands;

import lombok.Getter;
import net.iceyleagons.icicle.commands.middleware.MiddlewareStore;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.Autowired;
import net.iceyleagons.icicle.core.annotations.Service;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @since Nov. 1, 2021
 */
@Getter
@Service
public class CommandService {

    private final Application application;
    private final MiddlewareStore middlewareStore;
    private final JavaPlugin javaPlugin;

    @Autowired
    public CommandService(Application application, JavaPlugin javaPlugin) {
        this.application = application;
        this.middlewareStore = new MiddlewareStore();
        this.javaPlugin = javaPlugin;
    }
}
