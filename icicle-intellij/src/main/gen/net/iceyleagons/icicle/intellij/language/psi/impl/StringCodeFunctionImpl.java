// This is a generated file. Not intended for manual editing.
package net.iceyleagons.icicle.intellij.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static net.iceyleagons.iciclehelper.language.psi.StringCodeTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import net.iceyleagons.icicle.intellij.language.psi.*;

public class StringCodeFunctionImpl extends ASTWrapperPsiElement implements StringCodeFunction {

  public StringCodeFunctionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull StringCodeVisitor visitor) {
    visitor.visitFunction(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof StringCodeVisitor) accept((StringCodeVisitor)visitor);
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
