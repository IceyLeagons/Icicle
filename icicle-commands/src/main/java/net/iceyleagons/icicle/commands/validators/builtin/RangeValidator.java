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

package net.iceyleagons.icicle.commands.validators.builtin;

import net.iceyleagons.icicle.commands.annotations.validators.Range;
import net.iceyleagons.icicle.commands.exception.ValidatorException;
import net.iceyleagons.icicle.commands.validators.CommandParameterValidator;
import net.iceyleagons.icicle.commands.validators.CommandValidator;

import java.lang.reflect.Parameter;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 19, 2022
 */
@CommandValidator(Range.class)
public class RangeValidator implements CommandParameterValidator {

    @Override
    public void validate(Parameter parameter, String input) throws Exception {
        Range range = parameter.getAnnotation(Range.class);
        double d = Double.parseDouble(input);

        if (d > range.value() || d < range.min())
            throw new ValidatorException(parameter.getName() + " must be between " + range.min() + " and " + range.value());
    }
}
