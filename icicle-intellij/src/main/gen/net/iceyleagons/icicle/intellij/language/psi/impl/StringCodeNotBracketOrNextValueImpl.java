// This is a generated file. Not intended for manual editing.
package net.iceyleagons.icicle.intellij.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import net.iceyleagons.icicle.intellij.language.psi.StringCodeNotBracketOrNextValue;
import net.iceyleagons.icicle.intellij.language.psi.StringCodeVisitor;
import org.jetbrains.annotations.NotNull;

public class StringCodeNotBracketOrNextValueImpl extends ASTWrapperPsiElement implements StringCodeNotBracketOrNextValue {

    public StringCodeNotBracketOrNextValueImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull StringCodeVisitor visitor) {
        visitor.visitNotBracketOrNextValue(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof StringCodeVisitor) accept((StringCodeVisitor) visitor);
        else super.accept(visitor);
    }

}
