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
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
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
import java.util.stream.Collectors;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 28, 2022
 */
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
                        CommandParamResolverTemplate<?> temp = parameter.getType().equals(Optional.class) ? parameterStore.get(getOptionalInternalClass(parameter)) : parameterStore.get(parameter.getType()); //
                        return temp != null ? temp.buildFromParameter(parameter, false) : null;
                    })
                    .filter(Objects::nonNull)
                    .toArray(OptionData[]::new);

            data.addOptions(od);
            return data;

        }).toList();

        System.out.println(cmds.size());
        jda.updateCommands().addCommands(cmds).queue(e -> {
            System.out.println("success");
        }, r -> {
            System.out.println(r.getMessage());
        });
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (commands.containsKey(event.getName())) {
            RegisteredCommand cmd = commands.get(event.getName());
            cmd.execute(buildParams(cmd.getMethod(), event));
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

    private Class<?> getOptionalInternalClass(Parameter parameter) {
        return (Class<?>) ((ParameterizedType) parameter.getParameterizedType()).getActualTypeArguments()[0];
    }
}