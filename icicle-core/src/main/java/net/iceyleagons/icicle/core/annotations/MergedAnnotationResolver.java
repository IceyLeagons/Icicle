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

package net.iceyleagons.icicle.core.annotations;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import net.iceyleagons.icicle.utilities.lang.Internal;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

@Getter
public class MergedAnnotationResolver {

    private final Class<? extends Annotation> annotation;
    private final Set<Class<? extends Annotation>> childrenAnnotations;
    private final Reflections reflections;

    public MergedAnnotationResolver(Class<? extends Annotation> annotation, Reflections reflections) {
        this.annotation = annotation;
        this.reflections = reflections;

        this.childrenAnnotations = getChildren(reflections);
        this.childrenAnnotations.add(annotation); //we want to add the "root" annotation as well, because non-annotation types may annotate it
    }

    public Set<Class<?>> getAllTypesAnnotated() {
        Set<Class<?>> result = new ObjectOpenHashSet<>();

        for (Class<? extends Annotation> childrenAnnotation : childrenAnnotations) {
            result.addAll(
                    reflections.getTypesAnnotatedWith(childrenAnnotation)
                            .stream().filter(c -> !c.isAnnotation()).collect(Collectors.toSet())
            );
        }

        return result;
    }

    public boolean isAnnotated(Class<?> clazz) {
        for (Annotation clazzAnnotation : clazz.getAnnotations()) {
            if (childrenAnnotations.contains(clazzAnnotation.annotationType())) return true;
        }

        return false;
    }

    @Internal
    public void cleanUp() {
        this.childrenAnnotations.clear();
    }

    @SuppressWarnings("unchecked")
    private Set<Class<? extends Annotation>> getChildren(Reflections reflections) {
        Set<Class<? extends Annotation>> children = new ObjectOpenHashSet<>();
        Stack<Class<? extends Annotation>> stack = new Stack<>();

        stack.add(annotation);

        while (!stack.isEmpty()) {
            Class<? extends Annotation> current = stack.pop();

            if (!children.contains(current)) {
                children.add(current);

                for (Class<?> aClass : reflections.getTypesAnnotatedWith(current)) {
                    if (!children.contains(aClass) && aClass.isAnnotation()) {
                        stack.add((Class<? extends Annotation>) aClass);
                    }
                }
            }
        }

        return children;
    }
}
