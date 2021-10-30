package net.iceyleagons.icicle.core.utils;

import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KFunction;
import kotlin.reflect.KParameter;
import kotlin.reflect.full.KClasses;
import kotlin.reflect.jvm.KCallablesJvm;
import kotlin.reflect.jvm.ReflectJvmMapping;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for Kotlin reflection.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Oct. 30, 2021
 */
public final class Kotlin {

    @Nullable
    private static final Class<? extends Annotation> kotlinMeta;
    private static final boolean kotlinReflectionPresent;

    static {
        final ClassLoader classLoader = Kotlin.class.getClassLoader();

        kotlinMeta = getKotlinMeta(classLoader);
        kotlinReflectionPresent = ReflectionUtils.isClassPresent("kotlin.reflect.full.KClasses", classLoader);
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Annotation> getKotlinMeta(ClassLoader cl) {
        Class<?> meta = null;
        try {
            meta = Class.forName("kotlin.Metadata", false, cl);
        } catch (Exception ignored) {}

        return (Class<? extends Annotation>) meta;
    }

    /**
     * @return true if Kotlin is present in general
     */
    public static boolean isKotlinPresent() {
        return kotlinMeta != null;
    }

    /**
     * @return true if Kotlin reflection is present
     */
    public static boolean isKotlinReflectionPresent() {
        return kotlinReflectionPresent;
    }

    /**
     * @param clazz the class to check
     * @return true if the class provided is a Kotlin type
     */
    public static boolean isKotlinType(Class<?> clazz) {
        return kotlinMeta != null && clazz.getDeclaredAnnotation(kotlinMeta) != null;
    }

    public static boolean isSuspendingMethod(Method method) {
        if (isKotlinType(method.getDeclaringClass())) {
            Class<?>[] types = method.getParameterTypes();
            return types.length > 0 && "kotlin.coroutines.Continuation".equals(types[types.length - 1].getName());
        }

        return false;
    }

    @Nullable
    public static <T> Constructor<T> findPrimaryConstructor(Class<T> clazz) {
        if (clazz == null || !isKotlinPresent() || !isKotlinReflectionPresent() || !isKotlinType(clazz)) return null;

        KFunction<T> primaryConst = KClasses.getPrimaryConstructor(JvmClassMappingKt.getKotlinClass(clazz));
        if (primaryConst == null) return null;

        return ReflectJvmMapping.getJavaConstructor(primaryConst);
    }

    public static <T> T instantiateKotlinClass(Constructor<T> constructor, Object... args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        KFunction<T> kotlinConstructor = ReflectJvmMapping.getKotlinFunction(constructor);
        if (kotlinConstructor == null) {
            return constructor.newInstance(args);
        }

        if ((!Modifier.isPublic(constructor.getModifiers()))) {
            KCallablesJvm.setAccessible(kotlinConstructor, true);
        }

        List<KParameter> params = kotlinConstructor.getParameters();
        Map<KParameter, Object> objectParams = new HashMap<>(params.size());

        for (int i = 0; i < args.length; i++) {
            if (!(params.get(i).isOptional() && args[i] == null)) {
                objectParams.put(params.get(i), args[i]);
            }
        }

        return kotlinConstructor.callBy(objectParams);
    }
}
