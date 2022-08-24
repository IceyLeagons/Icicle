package net.iceyleagons.iciclehelper.language.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static net.iceyleagons.iciclehelper.language.psi.StringCodeTypes.*;

%%

%{
  public StringCodeLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class StringCodeLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

KEYWORD=TRUE|FALSE|AND|NOT|OR|GT|GTEQ|LT|LTEQ|ADD|DIV|MOD|MUL|SUB|CONCAT|EW|EQ|IF|ISEMPTY|JOIN|NE|SW
SPACE_LITERAL=[ \t\n\x0B\f\r]+
IDENTIFIER_LITERAL=[a-zA-Z_][a-zA-Z0-9_]*
INTEGER_LITERAL=(0|[1-9][0-9]*)|(0[xX][0-9a-fA-F]+)|(0[0-7]+)
STRING_LITERAL=('([^'\\])*')

%%
<YYINITIAL> {
  {WHITE_SPACE}             { return WHITE_SPACE; }

  ","                       { return COMMA; }
  "{"                       { return CODE_PART_START; }
  "}"                       { return CODE_PART_END; }
  "("                       { return FUNC_PART_START; }
  ")"                       { return FUNC_PART_END; }

  {KEYWORD}                 { return KEYWORD; }
  {SPACE_LITERAL}           { return SPACE_LITERAL; }
  {IDENTIFIER_LITERAL}      { return IDENTIFIER_LITERAL; }
  {INTEGER_LITERAL}         { return INTEGER_LITERAL; }
  {STRING_LITERAL}          { return STRING_LITERAL; }

}

[^] { return BAD_CHARACTER; }
