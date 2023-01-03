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

package net.iceyleagons.icicle.commands.manager;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.iceyleagons.icicle.commands.annotations.CommandContainer;
import net.iceyleagons.icicle.core.annotations.PostAppConstruct;
import net.iceyleagons.icicle.core.annotations.bean.Autowired;
import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import net.iceyleagons.icicle.utilities.datastores.tuple.Tuple;
import net.iceyleagons.icicle.utilities.datastores.tuple.UnmodifiableTuple;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 */
@AnnotationHandler
public class CommandContainerAutoCreateHandler implements CustomAutoCreateAnnotationHandler {

    private final CommandService commandService;

    // Hacky way but I have no f*cking clue why some command param resolvers don't get registered in time.
    private final Set<Tuple<Object, Class<?>>> cache = new ObjectOpenHashSet<>();

    @Autowired
    public CommandContainerAutoCreateHandler(CommandService commandService) {
        this.commandService = commandService;
    }

    @Override
    public @NotNull Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Collections.singleton(CommandContainer.class);
    }

    @Override
    public void onCreated(Object bean, Class<?> type) {
        cache.add(new UnmodifiableTuple<>(bean, type));
    }

    @PostAppConstruct(highPriority = true)
    public void registerCommands() {
        for (Tuple<Object, Class<?>> objectClassTuple : cache) {
            commandService.registerCommandContainer(objectClassTuple.getB(), objectClassTuple.getA());
        }
        cache.clear();
    }
}
