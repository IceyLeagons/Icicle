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

package net.iceyleagons.icicle.web.rest.endpoints;

import io.javalin.Javalin;
import io.javalin.http.Context;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.web.rest.endpoints.params.PathParam;
import net.iceyleagons.icicle.web.rest.endpoints.params.QueryParam;
import net.iceyleagons.icicle.web.rest.endpoints.params.RequestBody;
import net.iceyleagons.icicle.web.rest.endpoints.types.GetEndpoint;
import net.iceyleagons.icicle.web.rest.endpoints.types.PostEndpoint;
import org.eclipse.jetty.http.HttpStatus;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 12, 2022
 */
@Getter
@RequiredArgsConstructor
public abstract class Endpoint {

    private final Object parent;
    private final Method method;

    public static Endpoint create(Method method, Object parent) {
        if (GetEndpoint.isType(method)) {
            return new GetEndpoint(parent, method);
        }
        if (PostEndpoint.isType(method)) {
            return new PostEndpoint(parent, method);
        }

        return null;
    }

    public abstract void registerToJavalin(Javalin javalin);

    public Object invoke(Context ctx) throws Exception {
        final Parameter[] parameters = method.getParameters();
        final Object[] params = new Object[method.getParameterCount()];

        // TODO bad request elswhere, and other status messages.

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Class<?> type = param.getType();

            if (type.equals(Context.class)) {
                params[i] = ctx;
                continue;
            }

            if (param.isAnnotationPresent(QueryParam.class)) {
                QueryParam qp = param.getAnnotation(QueryParam.class);
                String passedParam = ctx.queryParam(qp.value());

                if (passedParam == null && qp.required()) {
                    ctx.status(HttpStatus.BAD_REQUEST_400);
                    return null;
                }

                params[i] = passedParam;
                continue;
            }

            if (param.isAnnotationPresent(RequestBody.class)) {
                try {
                    params[i] = ctx.bodyAsClass(type);
                } catch (Exception e) {
                    // JSON exception --> bad request
                    ctx.status(HttpStatus.BAD_REQUEST_400);
                    return null;
                }
                continue;
            }

            if (param.isAnnotationPresent(PathParam.class)) {
                PathParam pp = param.getAnnotation(PathParam.class);
                params[i] = ctx.pathParam(pp.value());
            }
        }

        return method.invoke(parent, params);
    }
}
