// This is a generated file. Not intended for manual editing.
package net.iceyleagons.icicle.intellij.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import net.iceyleagons.icicle.intellij.language.psi.StringCodeFunctionBody;
import net.iceyleagons.icicle.intellij.language.psi.StringCodeValue;
import net.iceyleagons.icicle.intellij.language.psi.StringCodeVisitor;
import org.jetbrains.annotations.NotNull;

public class StringCodeFunctionBodyImpl extends ASTWrapperPsiElement implements StringCodeFunctionBody {

  public StringCodeFunctionBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull StringCodeVisitor visitor) {
    visitor.visitFunctionBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof StringCodeVisitor) accept((StringCodeVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public StringCodeValue getValue() {
    return findNotNullChildByClass(StringCodeValue.class);
  }

}
