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

package net.iceyleagons.icicle.core.beans.handlers;

import it.unimi.dsi.fastutil.objects.ObjectSets;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import net.iceyleagons.icicle.core.annotations.service.Service;
import net.iceyleagons.icicle.core.beans.BeanRegistry;
import net.iceyleagons.icicle.core.beans.QualifierKey;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 25, 2022
 */
@AnnotationHandler
@RequiredArgsConstructor
public class ServiceAnnotationHandler implements CustomAutoCreateAnnotationHandler {

    private final BeanRegistry beanRegistry;

    @Override
    public @NotNull Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return ObjectSets.singleton(Service.class);
    }

    @Override
    public void onCreated(Object bean, Class<?> type) throws Exception {
        String qualifier = QualifierKey.getQualifier(type);
        for (Class<?> anInterface : type.getInterfaces()) {
            if (this.beanRegistry.isRegistered(anInterface, qualifier)) {
                throw new IllegalStateException("Service " + anInterface.getName() + " is already registered! (Qualifier: " + qualifier + ")");
            }

            this.beanRegistry.registerBean(anInterface, bean, qualifier);
        }
    }
}
