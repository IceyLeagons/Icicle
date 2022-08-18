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

import static org.reflections.scanners.Scanners.TypesAnnotated;

/**
 * The MergedAnnotationResolver is responsible for searching all the annotations (using DFS), that annotate an annotation specified in the constructor.
 * This way we can get classes, that have for ex. @{@link AutoCreate} in their annotation tree somewhere.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 25, 2021
 */
@Getter
public class MergedAnnotationResolver {

    private final Class<? extends Annotation> annotation;
    private final Set<Class<? extends Annotation>> childrenAnnotations;
    private final Reflections reflections;

    /**
     * Creates a new instance of {@link MergedAnnotationResolver}
     *
     * @param annotation  the annotation to search for in the tree
     * @param reflections the reflections instance to use
     */
    public MergedAnnotationResolver(Class<? extends Annotation> annotation, Reflections reflections) {
        this.annotation = annotation;
        this.reflections = reflections;

        this.childrenAnnotations = getChildren(reflections);
        //System.out.println("Reflections working with annotations: [" + this.childrenAnnotations.stream().map(Class::getName).collect(Collectors.joining(", ")) + "]");

        this.childrenAnnotations.add(annotation); //we want to add the "root" annotation as well, because non-annotation types may annotate it
    }

    /**
     * Returns all the non-annotation types, that have the specified annotation somewhere in their annotation tree.
     *
     * @return the resulting {@link Set}
     */
    public Set<Class<?>> getAllTypesAnnotated() {
        Set<Class<?>> result = new ObjectOpenHashSet<>();

        for (Class<? extends Annotation> childrenAnnotation : childrenAnnotations) {
            result.addAll(
                    reflections.get(TypesAnnotated.with(childrenAnnotation).asClass())
                            .stream().filter(c -> !c.isAnnotation()).collect(Collectors.toSet())
            );
        }

        /*
        System.out.println("[!!!] Found AutoCreate instances: ");
        result.forEach(s -> {
            System.out.println(s.getName());
        });
        System.out.println("=======================================");
        System.out.println();
        System.out.println();


         */
        return result;
    }

    /**
     * Checks whether the supplied type has the specified annotation somewhere in their annotation tree.
     *
     * @param clazz the type to check
     * @return true if it does annotate the specified annotation, false otherwise.
     */
    public boolean isAnnotated(Class<?> clazz) {
        for (Annotation clazzAnnotation : clazz.getAnnotations()) {
            if (childrenAnnotations.contains(clazzAnnotation.annotationType())) return true;
        }

        return false;
    }

    /**
     * Clears the children annotations set.
     * Shouldn't be called outside Icicle!
     *
     * @see Internal
     */
    @Internal
    public void cleanUp() {
        this.childrenAnnotations.clear();
    }

    /**
     * The actual DFS implementation.
     *
     * @param reflections the reflections to use
     * @return the children annotations.
     */
    @SuppressWarnings("unchecked")
    private Set<Class<? extends Annotation>> getChildren(Reflections reflections) {
        Set<Class<? extends Annotation>> children = new ObjectOpenHashSet<>();
        Stack<Class<? extends Annotation>> stack = new Stack<>();

        stack.add(annotation);

        while (!stack.isEmpty()) {
            Class<? extends Annotation> current = stack.pop();

            if (!children.contains(current)) {
                children.add(current);

                for (Class<?> aClass : reflections.get(TypesAnnotated.with(current).asClass())) {
                    if (!children.contains(aClass) && aClass.isAnnotation()) {
                        stack.add((Class<? extends Annotation>) aClass);
                    }
                }
            }
        }

        return children;
    }
}
