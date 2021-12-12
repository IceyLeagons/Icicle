package net.iceyleagons.icicle.commands;

import lombok.Getter;
import net.iceyleagons.icicle.commands.manager.RegisteredCommandManager;
import net.iceyleagons.icicle.commands.middleware.MiddlewareStore;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.Autowired;
import net.iceyleagons.icicle.core.annotations.Service;
import net.iceyleagons.icicle.core.translations.TranslationService;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * @since Nov. 1, 2021
 */
@Getter
@Service
public class CommandService {

    private final Application application;
    private final MiddlewareStore middlewareStore;
    private final JavaPlugin javaPlugin;
    private final TranslationService translationService;
    private final CommandInjector injector;
    private final Set<RegisteredCommandManager> commandManagers = new HashSet<>();

    @Autowired
    public CommandService(Application application, JavaPlugin javaPlugin, TranslationService translationService) {
        this.application = application;
        this.middlewareStore = new MiddlewareStore();
        this.javaPlugin = javaPlugin;
        this.injector = new CommandInjector(javaPlugin);
        this.translationService = translationService;
    }
}
