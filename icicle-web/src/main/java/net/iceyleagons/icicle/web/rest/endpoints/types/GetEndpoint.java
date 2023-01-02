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

package net.iceyleagons.icicle.web.rest.endpoints.types;

import io.javalin.Javalin;
import net.iceyleagons.icicle.web.rest.endpoints.Endpoint;
import net.iceyleagons.icicle.web.rest.endpoints.types.annotations.Get;

import java.lang.reflect.Method;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 13, 2022
 */
public class GetEndpoint extends Endpoint {

    public GetEndpoint(Object parent, Method method) {
        super(parent, method);
    }

    public static boolean isType(Method method) {
        return method.isAnnotationPresent(Get.class);
    }

    @Override
    public void registerToJavalin(Javalin javalin) {
        String path = super.getMethod().getAnnotation(Get.class).value();
        javalin.get(path, ctx -> {
            Object val = super.invoke(ctx);
            if (val != null) {
                ctx.json(val);
            }
        });
    }
}
