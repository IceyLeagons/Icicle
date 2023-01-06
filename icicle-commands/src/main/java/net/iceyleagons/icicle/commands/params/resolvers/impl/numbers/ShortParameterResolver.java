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

package net.iceyleagons.icicle.commands.params.resolvers.impl.numbers;

import net.iceyleagons.icicle.commands.annotations.ParameterResolver;

/**
 * Icicle's default parameter resolver for shorts.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 */
@ParameterResolver({short.class, Short.class})
public class ShortParameterResolver extends AbstractNumberParser<Short> {

    /**
     * Creates a new ShortParameterResolver instance.
     * Actual parsing logic is implemented in the {@link AbstractNumberParser} abstraction layer.
     */
    public ShortParameterResolver() {
        super(Short::parseShort, "short");
    }
}
