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

package net.iceyleagons.icicle.core.standalone;

import net.iceyleagons.icicle.core.AbstractIcicleApplication;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sept. 24, 2022
 */
public class StandaloneIcicleApplication extends AbstractIcicleApplication {

    private final String name;
    public boolean running = false;

    public StandaloneIcicleApplication(String rootPackage, Class<?> clazz) {
        super(rootPackage, new StandaloneExecutionHandler(), null);
        this.name = clazz.getName();
    }

    @Override
    public void start() throws Exception {
        running = true;
        super.start();
    }

    @Override
    public void shutdown() {
        running = false;
        super.shutdown();
    }

    @Override
    public String getName() {
        return name;
    }
}
