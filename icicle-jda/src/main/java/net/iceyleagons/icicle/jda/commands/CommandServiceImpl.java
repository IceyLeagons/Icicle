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

package net.iceyleagons.icicle.jda.commands;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.iceyleagons.icicle.core.annotations.bean.Autowired;
import net.iceyleagons.icicle.core.annotations.service.Service;
import net.iceyleagons.icicle.core.beans.BeanRegistry;
import net.iceyleagons.icicle.core.beans.QualifierKey;
import net.iceyleagons.icicle.jda.commands.annotations.Command;
import net.iceyleagons.icicle.jda.commands.params.CommandParamResolverTemplate;
import net.iceyleagons.icicle.jda.commands.params.ParameterStore;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 28, 2022
 */
@Slf4j
@Service
public class CommandServiceImpl implements CommandService {

    private final BeanRegistry beanRegistry;
    private final Map<String, RegisteredCommand> commands = new HashMap<>();

    @Getter
    private final ParameterStore parameterStore = new ParameterStore();

    @Autowired
    public CommandServiceImpl(BeanRegistry beanRegistry) {
        this.beanRegistry = beanRegistry;
    }

    public static Class<?> getOptionalInternalClass(Parameter parameter) {
        return (Class<?>) ((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments()[0];
    }

    @Override
    public void registerCommandContainer(Object bean, Class<?> type) {
        Arrays.stream(type.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Command.class))
                .forEach(m -> {
                    Command cmd = m.getAnnotation(Command.class);
                    commands.put(cmd.name(), new RegisteredCommand(cmd.name(), cmd.description(), bean, m));
                });
    }

    @Override
    public void registerToJda(JDA jda) {
        List<SlashCommandData> cmds = commands.values().stream().map(cmd -> {
            SlashCommandData data = Commands.slash(cmd.getName(), cmd.getDescription());
            OptionData[] od = Arrays.stream(cmd.getMethod().getParameters())
                    .map(parameter -> {
                        if (parameter.getType().isEnum() || (parameter.getType().equals(Optional.class) && getOptionalInternalClass(parameter).isEnum())) {
                            CommandParamResolverTemplate<?> temp = parameterStore.get(Enum.class);
                            return temp != null ? temp.buildFromParameter(parameter, false) : null;
                        }

                        CommandParamResolverTemplate<?> temp = parameter.getType().equals(Optional.class) ? parameterStore.get(getOptionalInternalClass(parameter)) : parameterStore.get(parameter.getType()); //
                        return temp != null ? temp.buildFromParameter(parameter, false) : null;
                    })
                    .filter(Objects::nonNull)
                    .toArray(OptionData[]::new);

            data.addOptions(od);
            return data;

        }).toList();

        if (!cmds.isEmpty()) {
            jda.updateCommands().addCommands(cmds).queue(e -> log.info("Successfully registered " + commands.size() + " commands!"), r -> log.error("Could not register commands, due to: " + r.getMessage()));
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (commands.containsKey(event.getName())) {
            RegisteredCommand cmd = commands.get(event.getName());
            try {
                cmd.execute(buildParams(cmd.getMethod(), event));
            } catch (Exception e) {
                event.reply("Dang it! We're sorry but an error happened on our side. Please contact a staff if you think this sould not happen!").queue();
                log.error("Error happened during command execution for " + event.getName() + " .", e);
            }
        }
    }

    private Object[] buildParams(Method method, SlashCommandInteractionEvent event) {
        Parameter[] params = method.getParameters();
        Object[] response = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            final Parameter param = params[i];
            final Class<?> type = param.getType();

            if (type.equals(SlashCommandInteractionEvent.class)) {
                response[i] = event;
                continue;
            }

            if (type.isEnum() || (type.equals(Optional.class) && getOptionalInternalClass(param).isEnum())) {
                Object parsed = parameterStore.get(Enum.class).parse(param, event);
                if (type.equals(Optional.class)) {
                    response[i] = Optional.ofNullable(parsed);
                    continue;
                }
                response[i] = parsed;
                continue;
            }


            final CommandParamResolverTemplate<?> resolver = param.getType().equals(Optional.class) ? parameterStore.get(getOptionalInternalClass(param)) : parameterStore.get(param.getType());

            if (resolver == null) {
                QualifierKey key = new QualifierKey(type, QualifierKey.getQualifier(param));
                if (beanRegistry.isRegistered(key)) {
                    if (type.equals(Optional.class)) {
                        response[i] = Optional.ofNullable(beanRegistry.getBeanNullable(key));
                        continue;
                    }

                    response[i] = beanRegistry.getBeanNullable(key);
                }

                throw new IllegalStateException("Unsatisfied dependency!");
            }

            final Object parsed = resolver.parse(param, event);
            if (type.equals(Optional.class)) {
                response[i] = Optional.ofNullable(parsed);
                continue;
            }

            response[i] = parsed;
        }

        return response;
    }
}
