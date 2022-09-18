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

package net.iceyleagons.iciclehelper;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightInfoFilter;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.iceyleagons.iciclehelper.utils.AnnotationSearchUtil;
import net.iceyleagons.iciclehelper.utils.IcicleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 21, 2022
 */
public class BeanErrorFilter implements HighlightInfoFilter {

    private final Map<HighlightSeverity, Map<TextAttributesKey, List<IcicleHighlightFilter>>> filters;
    public BeanErrorFilter() {
        filters = new Object2ObjectOpenHashMap<>();
        for (IcicleHighlightFilter value : IcicleHighlightFilter.values()) {
            filters.computeIfAbsent(value.severity, s -> new Object2ObjectOpenHashMap<>())
                    .computeIfAbsent(value.key, k -> new ArrayList<>())
                    .add(value);
        }
    }

    @Override
    public boolean accept(@NotNull HighlightInfo highlightInfo, @Nullable PsiFile psiFile) {
        if (psiFile == null) return true;

        final Project project = psiFile.getProject();
        if (!IcicleUtil.hasIcicleInstalled(project)) {
            return true;
        }

        final PsiElement element = psiFile.findElementAt(highlightInfo.getStartOffset());
        if (element == null) {
            return true;
        }

        return filters
                .getOrDefault(highlightInfo.getSeverity(), Collections.emptyMap())
                .getOrDefault(highlightInfo.type.getAttributesKey(), Collections.emptyList())
                .stream()
                .filter(f -> f.descriptionCheck(highlightInfo.getDescription(), element))
                .allMatch(f -> f.accept(element));
    }

    private enum IcicleHighlightFilter {

        UNUSED_TYPE(HighlightSeverity.WARNING, CodeInsightColors.NOT_USED_ELEMENT_ATTRIBUTES) {

            // TODO find key for Class is never used error message.
            //private final Pattern pattern = Pattern.compile(JavaBundle.message("inspection.unused.assignment.problem.descriptor1", "(.+)"));
            @Override
            public boolean descriptionCheck(@Nullable String description, PsiElement highlightedElement) {
                return description != null;
            }

            @Override
            public boolean accept(@NotNull PsiElement highlightedElement) {
                PsiElement element = highlightedElement.getContext();
                if (element instanceof PsiClass) {
                    return !AnnotationSearchUtil.isAutoCreated((PsiClass) element);
                }
                return true;
            }
        };

        private final HighlightSeverity severity;
        private final TextAttributesKey key;

        IcicleHighlightFilter(@NotNull HighlightSeverity severity, @Nullable TextAttributesKey key) {
            this.severity = severity;
            this.key = key;
        }

        /**
         * @param description            of the current highlighted element
         * @param highlightedElement     the current highlighted element
         * @return true if the filter can handle current type of the highlight info with that kind of the description
         */
        abstract public boolean descriptionCheck(@Nullable String description, PsiElement highlightedElement);

        /**
         * @param highlightedElement the deepest element (it's the leaf element in PSI tree where the highlight was occurred)
         * @return false if the highlight should be suppressed
         */
        abstract public boolean accept(@NotNull PsiElement highlightedElement);
    }
}
