package net.iceyleagons.icicle.nms;

import lombok.Getter;
import net.iceyleagons.icicle.nms.utils.AdvancedClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
@Getter
public abstract class WrapperClass {

    private static final Map<String, AdvancedClass<?>> cache = new ConcurrentHashMap<>();
    private final AdvancedClass<?> clazz;
    private final Object origin;

    public WrapperClass(String name, WrapType wrapType, Object origin) {
        if (!cache.containsKey(name)) {
            cache.put(name, wrapType.getClazzProvider().get(name));
        }

        this.clazz = cache.get(name);
        this.origin = origin;
    }

    protected void preDiscoverMethod(String as, String name, Class<?>... paramTypes) {
        clazz.preDiscoverMethod(as, name, paramTypes);
    }

    protected static AdvancedClass<?> getExtraClass(String name, WrapType wrapType) {
        if (!cache.containsKey(name)) {
            cache.put(name, wrapType.getClazzProvider().get(name));
        }

        return cache.get(name);
    }

    protected <A> A executeMethod(String name, Class<A> returnType, Object... params) {
        return clazz.executeMethod(name, origin, returnType, params);
    }

    protected <A> A getFieldValue(String name, Class<A> wantedType) {
        return clazz.getFieldValue(name, origin, wantedType);
    }

    protected void setFieldValue(String name, Object value) {
        clazz.setFieldValue(name, origin, value);
    }
}
