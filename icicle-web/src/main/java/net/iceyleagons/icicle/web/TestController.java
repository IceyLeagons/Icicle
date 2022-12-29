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

package net.iceyleagons.icicle.web;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import net.iceyleagons.icicle.core.annotations.execution.python.PythonFile;
import net.iceyleagons.icicle.web.rest.RestController;
import net.iceyleagons.icicle.web.rest.endpoints.params.PathParam;
import net.iceyleagons.icicle.web.rest.endpoints.params.QueryParam;
import net.iceyleagons.icicle.web.rest.endpoints.types.annotations.Get;

import java.util.Collections;
import java.util.List;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 12, 2022
 */
@RestController
public class TestController {

    @Get("/test/{id}")
    public String test(@PathParam("id") String id, @QueryParam("asd") String asd) {
        return "Your id: " + id + " | asd is: " + asd;
    }

    @Get("/lol")
    public List<String> test() {
        return List.of("hi", "d");
    }

    @Get("/")
    @SneakyThrows
    public List<String> lol() {
        return callTest();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    static class TestResponse {
        private List<String> response;
    }

    @PythonFile("test.py")
    public List<String> callTest() {
        return Collections.emptyList();
    }
}
