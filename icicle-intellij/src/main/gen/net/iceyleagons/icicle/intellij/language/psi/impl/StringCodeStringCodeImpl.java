// This is a generated file. Not intended for manual editing.
package net.iceyleagons.icicle.intellij.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import net.iceyleagons.icicle.intellij.language.psi.StringCodeFunction;
import net.iceyleagons.icicle.intellij.language.psi.StringCodeStringCode;
import net.iceyleagons.icicle.intellij.language.psi.StringCodeVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StringCodeStringCodeImpl extends ASTWrapperPsiElement implements StringCodeStringCode {

    public StringCodeStringCodeImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull StringCodeVisitor visitor) {
        visitor.visitStringCode(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof StringCodeVisitor) accept((StringCodeVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<StringCodeFunction> getFunctionList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, StringCodeFunction.class);
    }

}
