/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.commands.middleware;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @since Nov. 1, 2021
 */
public class MiddlewareStore {

    private final Map<Class<?>, CommandMiddlewareTemplate> middlewares = new Object2ObjectArrayMap<>(8); // Array map is probably better, since
                                                                                                                // there is not gonna be a large number of modifications,
                                                                                                                // and there are going to be a lot of accesses.

    public void registerMiddleware(CommandMiddlewareTemplate commandMiddlewareTemplate, Class<?> middlewareClass, CommandMiddleware commandMiddleware) {
        Class<?> toReplace = commandMiddleware.replaces();

        if (toReplace != CommandMiddleware.Nothing.class) {
            middlewares.remove(toReplace);
        }

        middlewares.put(middlewareClass, commandMiddlewareTemplate);
        System.out.println("Registered: " + middlewareClass.getName());
    }

    public CommandMiddlewareTemplate[] getMiddlewares() {
        return middlewares.values().toArray(CommandMiddlewareTemplate[]::new);
    }
}
