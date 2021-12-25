package net.iceyleagons.icicle.nms.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.utilities.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
@Getter
@RequiredArgsConstructor
public class AdvancedClass<T> {

    private final Class<T> clazz;
    private final Map<String, Field> fieldCache = new ConcurrentHashMap<>();
    private final Map<String, Method> methodCache = new ConcurrentHashMap<>();

    public Field getField(String name) {
        if (fieldCache.containsKey(name)) {
            return fieldCache.get(name);
        }

        Field field = ReflectionUtils.getField(clazz, name, true);
        fieldCache.put(name, field);
        return field;
    }

    public void preDiscoverMethod(String as, String name, Class<?>... paramTypes) {
        if (methodCache.containsKey(as)) return;
        methodCache.put(as, ReflectionUtils.getMethod(clazz, name, true, paramTypes));
    }

    public Method getMethod(String name, Class<?>... paramTypes) {
        if (methodCache.containsKey(name)) {
            return methodCache.get(name);
        }

        Method method = ReflectionUtils.getMethod(clazz, name, true, paramTypes);
        methodCache.put(name, method);
        return method;
    }

    public <A> A executeMethod(String name, Object parent, Class<A> returnType, Object... params) {
        return ReflectionUtils.execute(getMethod(name), parent, returnType, params);
    }

    public <A> A getFieldValue(String name, Object parent, Class<A> wantedType) {
        return ReflectionUtils.get(getField(name), parent, wantedType);
    }

    public void setFieldValue(String name, Object parent, Object value) {
        ReflectionUtils.set(getField(name), parent, value);
    }
}
