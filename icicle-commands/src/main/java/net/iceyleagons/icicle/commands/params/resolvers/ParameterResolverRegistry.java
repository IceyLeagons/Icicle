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

package net.iceyleagons.icicle.commands.params.resolvers;

import net.iceyleagons.icicle.commands.annotations.ParameterResolver;
import net.iceyleagons.icicle.core.utils.Store;

/**
 * Used for holding all the {@link ParameterResolver} instances
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 * @see Store
 */
public class ParameterResolverRegistry extends Store<Class<?>, ParameterResolverTemplate<?>> {

    /**
     * Registers a new {@link ParameterResolver}.
     * If the resolver is handling multiple parameter types all of them will be registered as seperate entries in the store.
     *
     * @param resolverTemplate the resolver instance
     * @param annotation the annotation
     */
    public void registerParameterResolver(ParameterResolverTemplate<?> resolverTemplate, ParameterResolver annotation) {
        for (Class<?> aClass : annotation.value()) {
            super.elements.put(aClass, resolverTemplate);
        }
    }
}
