/*
 * MIT License
 *
 * Copyright (c) 2023 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.core.modifiers.impl.asserts;

import net.iceyleagons.icicle.core.modifiers.MethodValueModifier;
import net.iceyleagons.icicle.core.modifiers.ValueModifier;

import javax.script.*;
import java.lang.reflect.Parameter;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 16, 2022
 */
@MethodValueModifier(AssertArgument.class)
public class AssertArgumentHandler implements ValueModifier {

    @Override
    public Object modify(Object input, Parameter parameter) throws ScriptException {
        final AssertArgument annotation = parameter.getAnnotation(AssertArgument.class);

        final ScriptEngineManager scriptingEngine = new ScriptEngineManager();
        final ScriptEngine engine = scriptingEngine.getEngineByName(annotation.scriptEngine());
        if (engine == null) {
            throw new IllegalStateException("No scripting engine named '" + annotation.scriptEngine() + "' found for AssertArgument expression.");
        }

        final ScriptContext context = new SimpleScriptContext();
        context.setAttribute("value", input, ScriptContext.ENGINE_SCOPE);

        Object result = engine.eval(annotation.expression(), context);
        if (!(result instanceof Boolean)) {
            throw new IllegalStateException("Non-boolean value returned from expression.");
        }

        if (!((boolean) result)) {
            throw new IllegalArgumentException(annotation.error());
        }

        return input;
    }
}
