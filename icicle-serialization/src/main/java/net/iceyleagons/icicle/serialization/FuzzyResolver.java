/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.serialization;

import net.iceyleagons.icicle.utilities.datastores.tuple.Tuple;
import net.iceyleagons.icicle.utilities.datastores.tuple.UnmodifiableTuple;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author TOTHTOMI
 * @version 1.1.0
 * @since Jul. 23, 2022
 */
public final class FuzzyResolver {

    // TODO Actually implement this.

    private static final Pattern GETTER_PATTERN = Pattern.compile("(get)[A-Z].*");
    private static final Pattern GETTER_IS_PATTERN = Pattern.compile("(is)[A-Z].*");
    private static final Pattern SETTER_PATTERN = Pattern.compile("(set)[A-Z].*");

    public static Optional<Tuple<Method, Method>> getGettersAndSetters(Field target, Method[] methods) {
        Method getter = findGetter(target, methods).orElse(null);
        Method setter = findSetter(target, methods).orElse(null);
        if (getter == null && setter == null) return Optional.empty();

        return Optional.of(new UnmodifiableTuple<>(getter, setter));
    }

    public static Optional<Method> findSetter(Field target, Method[] methods) {
        return findSetter(target.getName(), methods);
    }

    public static Optional<Method> findGetter(Field target, Method[] methods) {
        return findGetter(target.getName(), methods);
    }

    public static Optional<Method> findSetter(String target, Method[] methods) {
        final Pattern searchPattern = Pattern.compile("(set)(?i)" + target + ".*");
        return Arrays.stream(methods)
                .filter(m -> searchPattern.matcher(m.getName()).matches())
                .findFirst();
    }

    public static Optional<Method> findGetter(String target, Method[] methods) {
        final Pattern searchPattern = Pattern.compile("(get|is)(?i)" + target + ".*");
        return Arrays.stream(methods)
                .filter(m -> searchPattern.matcher(m.getName()).matches())
                .findFirst();
    }

    public static List<Tuple<Method, Method>> getStandaloneGettersAndSetters(Method[] methods, Field[] toIgnore) {
        final List<Tuple<Method, Method>> result = new ArrayList<>();
        final List<String> blackList = new ArrayList<>(Arrays.stream(toIgnore).map(Field::getName).toList());

        for (Method method : methods) {
            Optional<String> opt = getPropertyNameFromMethod(method);
            if (opt.isEmpty()) continue;

            String propName = opt.get();
            if (blackList.contains(propName)) continue;


            if (method.getReturnType().equals(void.class) || method.getReturnType().equals(Void.class)) {
                // It's a setter
                Optional<Method> getter = findGetter(propName, methods);
                if (getter.isEmpty()) {
                    blackList.add(propName);
                    continue;
                }

                result.add(new UnmodifiableTuple<>(getter.get(), method));
                continue;
            }

            Optional<Method> setter = findSetter(propName, methods);
            if (setter.isEmpty()) {
                blackList.add(propName);
                continue;
            }

            result.add(new UnmodifiableTuple<>(method, setter.get()));
        }

        return result;
    }

    public static Optional<String> getPropertyNameFromMethod(Method methodName) {
        final String mName = methodName.getName();

        if (GETTER_PATTERN.matcher(mName).matches() || SETTER_PATTERN.matcher(mName).matches()) {
            return Optional.of(mName.substring(3, 4).toLowerCase() + mName.substring(4));
        }

        if (GETTER_IS_PATTERN.matcher(mName).matches()) {
            return Optional.of(mName.substring(2, 3).toLowerCase() + mName.substring(4));
        }

        return Optional.empty();
    }
}
