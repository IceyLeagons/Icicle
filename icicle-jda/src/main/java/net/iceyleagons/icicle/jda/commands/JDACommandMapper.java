/*
 * MIT License
 *
 * Copyright (c) 2023 IceyLeagons and Contributors
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

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.iceyleagons.icicle.commands.RegisteredCommand;
import net.iceyleagons.icicle.commands.manager.CommandService;
import net.iceyleagons.icicle.utilities.lang.Utility;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 */
@Utility
public class JDACommandMapper {

    private JDACommandMapper() {
    }

    public static ListenerAdapter createCommandListener(final CommandService commandService) {
        return new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
                Object[] inputs = event.getOptions().stream().map(OptionMapping::getAsString).toArray(Object[]::new);
                try {
                    commandService.execute(event.getName(), event.getUser(), inputs, Map.of(SlashCommandInteractionEvent.class, event));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public static List<SlashCommandData> mapCommands(CommandService commandService) {
        return commandService.getCommandRegistry().getElements().values().stream().map(JDACommandMapper::map).collect(Collectors.toList());
    }

    public static SlashCommandData map(RegisteredCommand command) {
        SlashCommandData data = Commands.slash(command.getName(), command.getDescription());
        OptionData[] options = command.getParameters().stream()
                .map(info -> {
                    OptionData od = new OptionData(getType(info.getType()), info.getName(), info.getDescription(), info.isRequired(), false);
                    if (info.getOptions() != null && !info.getOptions().isEmpty()) {
                        for (String option : info.getOptions()) {
                            od.addChoice(option, option);
                        }
                    }
                    return od;
                })
                .toArray(OptionData[]::new);

        data.addOptions(options);
        return data;
    }

    private static OptionType getType(Class<?> type) {
        if (type.equals(String.class)) {
            return OptionType.STRING;
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            return OptionType.INTEGER;
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return OptionType.BOOLEAN;
        } else if (Number.class.isAssignableFrom(type)) {
            return OptionType.NUMBER;
        } else if (type.equals(User.class) || type.equals(Member.class)) {
            return OptionType.USER;
        } else if (GuildChannel.class.isAssignableFrom(type)) {
            return OptionType.CHANNEL;
        } else if (type.equals(Message.Attachment.class)) {
            return OptionType.ATTACHMENT;
        } else if (Role.class.isAssignableFrom(type)) {
            return OptionType.ROLE;
        } else if (IMentionable.class.isAssignableFrom(type)) {
            return OptionType.MENTIONABLE;
        } else {
            if (type.isEnum()) {
                return OptionType.STRING;
            }
            throw new IllegalArgumentException("Unsupported parameter type for discord: " + type.getName());
        }
    }
}
