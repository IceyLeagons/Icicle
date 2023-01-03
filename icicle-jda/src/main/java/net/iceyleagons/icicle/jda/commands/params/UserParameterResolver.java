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

package net.iceyleagons.icicle.jda.commands.params;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.iceyleagons.icicle.commands.annotations.ParameterResolver;
import net.iceyleagons.icicle.commands.params.ParamParsingException;
import net.iceyleagons.icicle.commands.params.ParameterInfo;
import net.iceyleagons.icicle.commands.params.resolvers.ParameterResolverTemplate;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 */
@ParameterResolver({User.class})
public class UserParameterResolver implements ParameterResolverTemplate<User> {
    @Override
    public User parse(Parameter parameter, Class<?> type, Object value, ParameterInfo info, Map<Class<?>, Object> additionalParameters) throws ParamParsingException {
        return Objects.requireNonNull(((SlashCommandInteractionEvent) additionalParameters.get(SlashCommandInteractionEvent.class)).getOption(info.getName())).getAsUser();
    }
}
