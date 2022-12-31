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

package net.iceyleagons.icicle.jda.commands.params.impl;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.iceyleagons.icicle.jda.commands.annotations.CommandParamHandler;
import net.iceyleagons.icicle.jda.commands.annotations.CommandParameter;
import net.iceyleagons.icicle.jda.commands.params.CommandParamResolverTemplate;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Parameter;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 28, 2022
 */
@CommandParamHandler({ Message.Attachment.class })
public class AttachmentHandler implements CommandParamResolverTemplate<Message.Attachment> {

    @Override
    public OptionData buildFromParameter(Parameter param, boolean autoComplete) {
        CommandParameter cp = getParamAnnotation(param);
        return new OptionData(OptionType.ATTACHMENT, cp.name(), cp.description(), isRequired(param), autoComplete);
    }

    @Override
    @Nullable
    public Message.Attachment parse(Parameter parameter, SlashCommandInteractionEvent event) {
        OptionMapping om = event.getOption(getParamAnnotation(parameter).name());
        if (om == null) return null;

        return om.getAsAttachment();
    }
}

