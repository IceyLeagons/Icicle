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

package net.iceyleagons.icicle.core.proxy.interceptor.python;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.*;
import net.iceyleagons.icicle.core.annotations.execution.python.PythonFile;
import net.iceyleagons.icicle.core.modifiers.ValueModifier;
import net.iceyleagons.icicle.core.modifiers.ValueModifierAutoCreateHandler;
import net.iceyleagons.icicle.core.python.PythonExecutable;
import net.iceyleagons.icicle.core.python.PythonFileHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 16, 2022
 */
@RequiredArgsConstructor
public class PythonDelegation {

    private PythonExecutable pythonExecutable = null;

    @RuntimeType
    public Object run(@Origin Method method, @AllArguments Object[] params) throws Exception {
        if (pythonExecutable == null) {
            pythonExecutable = PythonFileHandler.of(method.getAnnotation(PythonFile.class).value());
        }
        return pythonExecutable.call(Arrays.stream(params).map(Object::toString).toArray(String[]::new));
    }
}
