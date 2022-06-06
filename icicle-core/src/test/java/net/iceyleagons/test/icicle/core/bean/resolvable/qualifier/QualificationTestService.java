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

package net.iceyleagons.test.icicle.core.bean.resolvable.qualifier;

import net.iceyleagons.icicle.core.annotations.Qualifier;
import net.iceyleagons.icicle.core.annotations.service.Service;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 06, 2022
 */
@Service
public class QualificationTestService {

    private final TestService test;
    private final TestService test2;
    private final TestService test3;

    public QualificationTestService(
            @Qualifier("qualified") TestService test, // Should be QualifiedService
            @Qualifier("qualified2") TestService test2, // Should be SecondQualifiedService
            TestService test3 // Should be NotQualifiedService
    ){
        this.test = test;
        this.test2 = test2;
        this.test3 = test3;
    }

    public TestService getQualified() {
        return this.test;
    }

    public TestService getQualified2() {
        return this.test2;
    }

    public TestService getNonQualified() {
        return this.test3;
    }
}
