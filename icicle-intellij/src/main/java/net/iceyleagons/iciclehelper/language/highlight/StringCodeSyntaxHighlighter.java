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

package net.iceyleagons.iciclehelper.language.highlight;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import net.iceyleagons.iciclehelper.language.parser.StringCodeLexerAdapter;
import net.iceyleagons.iciclehelper.language.psi.StringCodeTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 21, 2022
 */
public class StringCodeSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey BRACES = createTextAttributesKey("STRINGCODE_BRACKETS", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey COMMA = createTextAttributesKey("STRINGCODE_COMMA", DefaultLanguageHighlighterColors.COMMA);
    public static final TextAttributesKey KEYWORD = createTextAttributesKey("STRINGCODE_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey STRING = createTextAttributesKey("STRINGCODE_STRING", DefaultLanguageHighlighterColors.STRING);


    private static final TextAttributesKey[] BRACES_KEYS = new TextAttributesKey[]{BRACES};
    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] COMMA_KEYS = new TextAttributesKey[]{COMMA};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new StringCodeLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType token) {
        if (token.equals(StringCodeTypes.KEYWORD)) {
            return KEYWORD_KEYS;
        }

        if (token.equals(StringCodeTypes.CODE_PART_START) || token.equals(StringCodeTypes.CODE_PART_END)) {
            return BRACES_KEYS;
        }

        if (token.equals(StringCodeTypes.STRING_LITERAL)) {
            return STRING_KEYS;
        }

        if (token.equals(StringCodeTypes.COMMA)) {
            return COMMA_KEYS;
        }

        return EMPTY_KEYS;
    }
}
