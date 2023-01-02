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

package net.iceyleagons.iciclehelper.utils;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.util.containers.ContainerUtil;
import net.iceyleagons.iciclehelper.IcicleClasses;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 21, 2022
 */
public class AnnotationSearchUtil {

    public static boolean isAnnotatedWith(PsiModifierListOwner modifierListOwner, String annotation) {
        return modifierListOwner.hasAnnotation(annotation);
    }

    public static PsiAnnotation findAnnotation(PsiModifierListOwner modifierListOwner, String... annotations) {
        return Stream.of(annotations).map(modifierListOwner::getAnnotation).filter(Objects::nonNull).findAny().orElse(null);
    }

    public static boolean isAnnotatedWith(PsiModifierListOwner modifierListOwner, String... annotations) {
        return findAnnotation(modifierListOwner, annotations) != null;
    }

    public static List<PsiAnnotation> getAllAnnotations(PsiModifierListOwner modifierListOwner, Collection<String> annotations) {
        if (annotations.isEmpty()) return Collections.emptyList();

        List<PsiAnnotation> result = new ArrayList<>();
        for (PsiAnnotation annotation : modifierListOwner.getAnnotations()) {
            if (ContainerUtil.exists(annotations, annotation::hasQualifiedName)) {
                result.add(annotation);
            }
        }

        return result;
    }

    public static boolean isAutoCreated(PsiClass element) {
        Stack<PsiAnnotation> stack = new Stack<>();
        stack.addAll(Arrays.asList(element.getAnnotations()));

        while (!stack.isEmpty()) {
            PsiAnnotation annotation = stack.pop();
            if (annotation.hasQualifiedName(IcicleClasses.AUTOCREATE)) {
                return true;
            }

            for (PsiAnnotation psiAnnotation : Objects.requireNonNull(annotation.resolveAnnotationType()).getAnnotations()) {
                String qualifiedName = psiAnnotation.getQualifiedName();
                if (qualifiedName != null && !qualifiedName.contains("java.lang")) {
                    stack.add(psiAnnotation);
                }
            }
        }

        return false;
    }
}
