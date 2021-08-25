package net.iceyleagons.icicle.core.annotations;

import lombok.Getter;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
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
        Set<Class<?>> result = new HashSet<>();

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

    private Set<Class<? extends Annotation>> getChildren(Reflections reflections) {
        Set<Class<? extends Annotation>> children = new HashSet<>();
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
