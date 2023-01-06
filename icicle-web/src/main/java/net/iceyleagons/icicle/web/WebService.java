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

import io.javalin.Javalin;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.bean.Autowired;
import net.iceyleagons.icicle.core.annotations.bean.Bean;
import net.iceyleagons.icicle.core.annotations.service.Service;

/**
 * The actual bean responsible for booting up a {@link Javalin} instance.
 * Port of the server can be set with config property "icicle.web.server.port". Default is 8080
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 12, 2022
 */
@Service
public class WebService {
    private final int port;

    /**
     * This constructor is called by Icicle automatically.
     *
     * @param application the current {@link Application} instance
     * @see Autowired
     */
    @Autowired
    public WebService(Application application) {
        this.port = application.getConfigurationEnvironment().getProperty("icicle.web.server.port", Integer.class).orElse(8080);
    }

    /**
     * @return a new {@link Javalin} instance or the proxied bean after the initial load.
     */
    @Bean
    public Javalin getJavalin() {
        Javalin j = Javalin.create(conf -> conf.showJavalinBanner = false);
        return j.start(port);
    }
}
