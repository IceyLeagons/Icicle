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

package net.iceyleagons.icicle.commands.params.builtin;

import net.iceyleagons.icicle.commands.CommandManager;
import net.iceyleagons.icicle.commands.exception.ParamParsingException;
import net.iceyleagons.icicle.commands.params.CommandParameterResolver;
import net.iceyleagons.icicle.commands.params.CommandParameterResolverTemplate;
import net.iceyleagons.icicle.commands.utils.StandardCommandErrors;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sept. 11, 2022
 */
@CommandParameterResolver({boolean.class, Boolean.class})
public class BooleanParamResolver implements CommandParameterResolverTemplate {

    @Override
    public Object resolveParameter(Class<?> type, CommandManager commandManager, String arg, Object sender) throws ParamParsingException {
        if (!arg.equalsIgnoreCase("true") && !arg.equalsIgnoreCase("false")) {
            throw new ParamParsingException(StandardCommandErrors.BOOLEAN_PARSER_KEY, StandardCommandErrors.BOOLEAN_PARSER_DEFAULT, Map.of("expected", "true/false"));
        }

        return Boolean.parseBoolean(arg);
    }

    @Override
    public List<String> getOptions(String[] args) {
        return Arrays.asList("true", "false");
    }
}
