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

package net.iceyleagons.icicle.utilities.generic;

import net.iceyleagons.icicle.utilities.ArrayUtils;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import net.iceyleagons.icicle.utilities.generic.acessors.OneTypeAccessor;
import net.iceyleagons.icicle.utilities.lang.Experimental;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author TOTHTOMI
 * @version 0.1.0
 * @since Apr. 14, 2022
 */
@Experimental
public class GenericList<T> extends OneTypeAccessor<T> implements Iterable<T> {

    private final Object genericArray;

    public GenericList() {
        this(20);
    }

    public GenericList(int size) {
        this.genericArray = GenericUtils.createGenericArrayWithoutCasting(super.getATypeClass(), size);
    }

    public GenericList(Object from) {
        this.genericArray = from;
    }

    public T get(int index) {
        return ReflectionUtils.castIfNecessary(super.getATypeClass(), Array.get(this.genericArray, index));
    }

    public void add(T... object) {
        ArrayUtils.appendToGenericArray(this.genericArray, super.getATypeClass(), (Object[]) object);
    }

    public int size() {
        return Array.getLength(genericArray);
    }

    public T[] getAsNormalArray() {
        return GenericUtils.genericArrayToNormalArray(this.genericArray, super.getATypeClass());
    }

    public List<T> getAsList() {
        return Arrays.asList(this.getAsNormalArray());
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return getAsList().iterator();
    }
}
