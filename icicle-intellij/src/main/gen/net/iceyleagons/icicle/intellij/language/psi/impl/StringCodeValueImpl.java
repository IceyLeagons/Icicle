// This is a generated file. Not intended for manual editing.
package net.iceyleagons.icicle.intellij.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import net.iceyleagons.icicle.intellij.language.psi.StringCodeFunction;
import net.iceyleagons.icicle.intellij.language.psi.StringCodeValue;
import net.iceyleagons.icicle.intellij.language.psi.StringCodeVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.iceyleagons.iciclehelper.language.psi.StringCodeTypes.*;

public class StringCodeValueImpl extends ASTWrapperPsiElement implements StringCodeValue {

    public StringCodeValueImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull StringCodeVisitor visitor) {
        visitor.visitValue(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof StringCodeVisitor) accept((StringCodeVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public StringCodeFunction getFunction() {
        return findChildByClass(StringCodeFunction.class);
    }

    @Override
    @Nullable
    public PsiElement getIdentifierLiteral() {
        return findChildByType(IDENTIFIER_LITERAL);
    }

    @Override
    @Nullable
    public PsiElement getIntegerLiteral() {
        return findChildByType(INTEGER_LITERAL);
    }

    @Override
    @Nullable
    public PsiElement getStringLiteral() {
        return findChildByType(STRING_LITERAL);
    }

}
