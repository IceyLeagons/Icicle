// This is a generated file. Not intended for manual editing.
package net.iceyleagons.iciclehelper.language.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import net.iceyleagons.icicle.intellij.language.psi.impl.*;

public interface StringCodeTypes {

    IElementType FUNCTION = new StringCodeElementType("FUNCTION");
    IElementType FUNCTION_BODY = new StringCodeElementType("FUNCTION_BODY");
    IElementType NOT_BRACKET_OR_NEXT_VALUE = new StringCodeElementType("NOT_BRACKET_OR_NEXT_VALUE");
    IElementType STRING_CODE = new StringCodeElementType("STRING_CODE");
    IElementType VALUE = new StringCodeElementType("VALUE");

    IElementType CODE_PART_END = new StringCodeTokenType("}");
    IElementType CODE_PART_START = new StringCodeTokenType("{");
    IElementType COMMA = new StringCodeTokenType(",");
    IElementType FUNC_PART_END = new StringCodeTokenType(")");
    IElementType FUNC_PART_START = new StringCodeTokenType("(");
    IElementType IDENTIFIER_LITERAL = new StringCodeTokenType("IDENTIFIER_LITERAL");
    IElementType INTEGER_LITERAL = new StringCodeTokenType("INTEGER_LITERAL");
    IElementType KEYWORD = new StringCodeTokenType("keyword");
    IElementType STRING_LITERAL = new StringCodeTokenType("STRING_LITERAL");

    class Factory {
        public static PsiElement createElement(ASTNode node) {
            IElementType type = node.getElementType();
            if (type == FUNCTION) {
                return new StringCodeFunctionImpl(node);
            } else if (type == FUNCTION_BODY) {
                return new StringCodeFunctionBodyImpl(node);
            } else if (type == NOT_BRACKET_OR_NEXT_VALUE) {
                return new StringCodeNotBracketOrNextValueImpl(node);
            } else if (type == STRING_CODE) {
                return new StringCodeStringCodeImpl(node);
            } else if (type == VALUE) {
                return new StringCodeValueImpl(node);
            }
            throw new AssertionError("Unknown element type: " + type);
        }
    }
}
