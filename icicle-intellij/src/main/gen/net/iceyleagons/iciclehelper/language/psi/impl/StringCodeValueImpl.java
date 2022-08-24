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

// This is a generated file. Not intended for manual editing.
package net.iceyleagons.iciclehelper.language.psi.impl;

import net.iceyleagons.iciclehelper.language.psi.StringCodeFunction;
import net.iceyleagons.iciclehelper.language.psi.StringCodeTypes;
import net.iceyleagons.iciclehelper.language.psi.StringCodeValue;
import net.iceyleagons.iciclehelper.language.psi.StringCodeVisitor;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;

public class StringCodeValueImpl extends ASTWrapperPsiElement implements StringCodeValue {

  public StringCodeValueImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull StringCodeVisitor visitor) {
    visitor.visitValue(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof StringCodeVisitor) accept((StringCodeVisitor)visitor);
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
    return findChildByType(StringCodeTypes.IDENTIFIER_LITERAL);
  }

  @Override
  @Nullable
  public PsiElement getIntegerLiteral() {
    return findChildByType(StringCodeTypes.INTEGER_LITERAL);
  }

  @Override
  @Nullable
  public PsiElement getStringLiteral() {
    return findChildByType(StringCodeTypes.STRING_LITERAL);
  }

}
