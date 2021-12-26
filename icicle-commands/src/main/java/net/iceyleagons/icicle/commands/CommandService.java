/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.commands;

import lombok.Getter;
import net.iceyleagons.icicle.commands.manager.RegisteredCommandManager;
import net.iceyleagons.icicle.commands.middleware.MiddlewareStore;
import net.iceyleagons.icicle.commands.params.CommandParameterResolverTemplate;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.lang.Autowired;
import net.iceyleagons.icicle.core.annotations.Service;
import net.iceyleagons.icicle.core.translations.TranslationService;
import net.iceyleagons.icicle.core.utils.Defaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
    private final Map<Class<?>, CommandParameterResolverTemplate> paramResolvers = new HashMap<>();

    @Autowired
    public CommandService(Application application, JavaPlugin javaPlugin, TranslationService translationService) {
        this.application = application;
        this.middlewareStore = new MiddlewareStore();
        this.javaPlugin = javaPlugin;
        this.injector = new CommandInjector(javaPlugin);
        this.translationService = translationService;

        this.paramResolvers.put(String.class, new CommandParameterResolverTemplate() {
            @Override
            public Object resolveParameter(Class<?> type, RegisteredCommandManager manager, String arg, CommandSender commandSender) {
                return arg;
            }

            @Nullable
            @Override
            public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
                return Collections.emptyList();
            }
        });
    }

    public Object resolveParameter(Class<?> type, RegisteredCommandManager manager, String arg, CommandSender commandSender) {
        if (arg == null || arg.isEmpty() || type == null || !paramResolvers.containsKey(type)) {
            return Defaults.DEFAULT_TYPE_VALUES.getOrDefault(type, null);
        }

        Object result = paramResolvers.get(type).resolveParameter(type, manager, arg, commandSender);
        return Defaults.DEFAULT_TYPE_VALUES.containsKey(type) && result == null ? Defaults.DEFAULT_TYPE_VALUES.get(type) : result;
    }
}
