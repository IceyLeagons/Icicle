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

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.iceyleagons.icicle.jda.commands.CommandServiceImpl;
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
@CommandParamHandler({Enum.class})
public class EnumHandler implements CommandParamResolverTemplate<Object> {

    @Override
    public OptionData buildFromParameter(Parameter param, boolean autoComplete) {
        CommandParameter cp = getParamAnnotation(param);
        OptionData od = new OptionData(OptionType.STRING, cp.name(), cp.description(), isRequired(param), autoComplete);

        for (Object enumConstant : isRequired(param) ? param.getType().getEnumConstants() : CommandServiceImpl.getOptionalInternalClass(param).getEnumConstants()) {
            String val = enumConstant.toString().toLowerCase();
            od.addChoice(val.substring(0, 1).toUpperCase() + val.substring(1), val);
        }

        return od;
    }

    @Override
    @Nullable
    public Object parse(Parameter parameter, SlashCommandInteractionEvent event) {
        OptionMapping om = event.getOption(getParamAnnotation(parameter).name());
        if (om == null) return null;

        return Enum.valueOf((Class<? extends Enum>) (isRequired(parameter) ? parameter.getType() : CommandServiceImpl.getOptionalInternalClass(parameter)), om.getAsString().toUpperCase());
    }
}

