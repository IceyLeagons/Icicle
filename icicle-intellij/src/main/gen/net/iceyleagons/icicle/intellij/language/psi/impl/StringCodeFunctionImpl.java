// This is a generated file. Not intended for manual editing.
package net.iceyleagons.icicle.intellij.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import net.iceyleagons.icicle.intellij.language.psi.StringCodeFunction;
import net.iceyleagons.icicle.intellij.language.psi.StringCodeFunctionBody;
import net.iceyleagons.icicle.intellij.language.psi.StringCodeVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.iceyleagons.iciclehelper.language.psi.StringCodeTypes.KEYWORD;

public class StringCodeFunctionImpl extends ASTWrapperPsiElement implements StringCodeFunction {

    public StringCodeFunctionImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull StringCodeVisitor visitor) {
        visitor.visitFunction(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof StringCodeVisitor) accept((StringCodeVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<StringCodeFunctionBody> getFunctionBodyList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, StringCodeFunctionBody.class);
    }

    @Override
    @NotNull
    public PsiElement getKeyword() {
        return findNotNullChildByType(KEYWORD);
    }

}
