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

package net.iceyleagons.icicle.utilities.lang.processors;

import net.iceyleagons.icicle.utilities.lang.Broken;
import net.iceyleagons.icicle.utilities.lang.Experimental;
import net.iceyleagons.icicle.utilities.lang.Internal;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 09, 2022
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"net.iceyleagons.icicle.utilities.lang.Broken",
        "net.iceyleagons.icicle.utilities.lang.Experimental", "net.iceyleagons.icicle.utilities.lang.Internal"})
public class WarningProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Internal.class)) {
            String fullName = element.getEnclosingElement().asType().toString();

            if (!fullName.contains("net.iceyleagons.icicle")) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Element is marked with @Internal, and should not be used outside Icicle's core packages.", element);
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(Broken.class)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Element is marked with @Broken! Using it may cause serious issues!", element);
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(Experimental.class)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Element is marked with @Experimental, use it with caution!", element);
        }

        return true;
    }
}
