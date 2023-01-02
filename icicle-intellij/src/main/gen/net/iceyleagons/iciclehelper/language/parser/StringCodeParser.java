// This is a generated file. Not intended for manual editing.
package net.iceyleagons.iciclehelper.language.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

import static net.iceyleagons.iciclehelper.language.parser.StringCodeParserUtil.*;
import static net.iceyleagons.iciclehelper.language.psi.StringCodeTypes.*;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class StringCodeParser implements PsiParser, LightPsiParser {

    static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
        return root(b, l + 1);
    }

    /* ********************************************************** */
    // keyword FUNC_PART_START function_body* FUNC_PART_END
    public static boolean function(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "function")) return false;
        if (!nextTokenIs(b, KEYWORD)) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, FUNCTION, null);
        r = consumeTokens(b, 1, KEYWORD, FUNC_PART_START);
        p = r; // pin = 1
        r = r && report_error_(b, function_2(b, l + 1));
        r = p && consumeToken(b, FUNC_PART_END) && r;
        exit_section_(b, l, m, r, p, null);
        return r || p;
    }

    // function_body*
    private static boolean function_2(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "function_2")) return false;
        while (true) {
            int c = current_position_(b);
            if (!function_body(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "function_2", c)) break;
        }
        return true;
    }

    /* ********************************************************** */
    // value (','|&')')
    public static boolean function_body(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "function_body")) return false;
        boolean r, p;
        Marker m = enter_section_(b, l, _NONE_, FUNCTION_BODY, "<function body>");
        r = value(b, l + 1);
        p = r; // pin = 1
        r = r && function_body_1(b, l + 1);
        exit_section_(b, l, m, r, p, StringCodeParser::not_bracket_or_next_value);
        return r || p;
    }

    // ','|&')'
    private static boolean function_body_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "function_body_1")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, COMMA);
        if (!r) r = function_body_1_1(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // &')'
    private static boolean function_body_1_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "function_body_1_1")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _AND_);
        r = consumeToken(b, FUNC_PART_END);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    /* ********************************************************** */
    // !(','|FUNC_PART_END|value)
    public static boolean not_bracket_or_next_value(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "not_bracket_or_next_value")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NOT_, NOT_BRACKET_OR_NEXT_VALUE, "<not bracket or next value>");
        r = !not_bracket_or_next_value_0(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    // ','|FUNC_PART_END|value
    private static boolean not_bracket_or_next_value_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "not_bracket_or_next_value_0")) return false;
        boolean r;
        r = consumeToken(b, COMMA);
        if (!r) r = consumeToken(b, FUNC_PART_END);
        if (!r) r = value(b, l + 1);
        return r;
    }

    /* ********************************************************** */
    // ((CODE_PART_START value CODE_PART_END) | value | stringCode)*
    static boolean root(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "root")) return false;
        while (true) {
            int c = current_position_(b);
            if (!root_0(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "root", c)) break;
        }
        return true;
    }

    // (CODE_PART_START value CODE_PART_END) | value | stringCode
    private static boolean root_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "root_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = root_0_0(b, l + 1);
        if (!r) r = value(b, l + 1);
        if (!r) r = stringCode(b, l + 1);
        exit_section_(b, m, null, r);
        return r;
    }

    // CODE_PART_START value CODE_PART_END
    private static boolean root_0_0(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "root_0_0")) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, CODE_PART_START);
        r = r && value(b, l + 1);
        r = r && consumeToken(b, CODE_PART_END);
        exit_section_(b, m, null, r);
        return r;
    }

    /* ********************************************************** */
    // CODE_PART_START function* CODE_PART_END
    public static boolean stringCode(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "stringCode")) return false;
        if (!nextTokenIs(b, CODE_PART_START)) return false;
        boolean r;
        Marker m = enter_section_(b);
        r = consumeToken(b, CODE_PART_START);
        r = r && stringCode_1(b, l + 1);
        r = r && consumeToken(b, CODE_PART_END);
        exit_section_(b, m, STRING_CODE, r);
        return r;
    }

    // function*
    private static boolean stringCode_1(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "stringCode_1")) return false;
        while (true) {
            int c = current_position_(b);
            if (!function(b, l + 1)) break;
            if (!empty_element_parsed_guard_(b, "stringCode_1", c)) break;
        }
        return true;
    }

    /* ********************************************************** */
    // IDENTIFIER_LITERAL | STRING_LITERAL | INTEGER_LITERAL | function
    public static boolean value(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "value")) return false;
        boolean r;
        Marker m = enter_section_(b, l, _NONE_, VALUE, "<value>");
        r = consumeToken(b, IDENTIFIER_LITERAL);
        if (!r) r = consumeToken(b, STRING_LITERAL);
        if (!r) r = consumeToken(b, INTEGER_LITERAL);
        if (!r) r = function(b, l + 1);
        exit_section_(b, l, m, r, false, null);
        return r;
    }

    public ASTNode parse(IElementType t, PsiBuilder b) {
        parseLight(t, b);
        return b.getTreeBuilt();
    }

    public void parseLight(IElementType t, PsiBuilder b) {
        boolean r;
        b = adapt_builder_(t, b, this, null);
        Marker m = enter_section_(b, 0, _COLLAPSE_, null);
        r = parse_root_(t, b);
        exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
    }

    protected boolean parse_root_(IElementType t, PsiBuilder b) {
        return parse_root_(t, b, 0);
    }

}
