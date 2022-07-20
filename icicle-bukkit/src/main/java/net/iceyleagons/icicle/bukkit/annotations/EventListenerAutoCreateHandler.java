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

package net.iceyleagons.icicle.bukkit.annotations;

import it.unimi.dsi.fastutil.objects.ObjectSets;
import net.iceyleagons.icicle.core.annotations.Autowired;
import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 17, 2021
 */
@AnnotationHandler
public class EventListenerAutoCreateHandler implements CustomAutoCreateAnnotationHandler {

    private final JavaPlugin plugin;

    @Autowired
    public EventListenerAutoCreateHandler(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
    }

    @Override
    public Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return ObjectSets.singleton(EventListener.class);
    }

    @Override
    public void onCreated(Object bean, Class<?> type) throws Exception {
        if (!(bean instanceof Listener)) {
            throw new IllegalStateException("Class " + type.getName() + " is marked with @EventListener but does not implement org.bukkit.event.Listener!");
        }

        this.plugin.getServer().getPluginManager().registerEvents((Listener) bean, this.plugin);
    }
}
