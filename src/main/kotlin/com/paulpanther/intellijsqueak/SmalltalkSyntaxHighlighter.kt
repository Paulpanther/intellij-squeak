package com.paulpanther.intellijsqueak

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.paulpanther.intellijsqueak.lang.SmalltalkLexerAdapter
import com.paulpanther.intellijsqueak.lang.SmalltalkTypes

class SmalltalkSyntaxHighlighter: SyntaxHighlighterBase() {
    companion object {
        val identifier = TextAttributesKey
            .createTextAttributesKey("IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
        val statementSeperator = TextAttributesKey
            .createTextAttributesKey("STATEMENT_SEPERATOR", DefaultLanguageHighlighterColors.SEMICOLON)
        val number = TextAttributesKey
            .createTextAttributesKey("NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val methodDeclaration = TextAttributesKey
            .createTextAttributesKey("METHOD_DECLARATION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
        val parenthesis = TextAttributesKey
            .createTextAttributesKey("PARENTHESIS", DefaultLanguageHighlighterColors.PARENTHESES)
        val keyword = TextAttributesKey
            .createTextAttributesKey("KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val string = TextAttributesKey
            .createTextAttributesKey("STRING", DefaultLanguageHighlighterColors.STRING)
        val assignment = TextAttributesKey
            .createTextAttributesKey("ASSIGNMENT", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val comment = TextAttributesKey
            .createTextAttributesKey("COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)
        val temporaryDefinition = TextAttributesKey
            .createTextAttributesKey("TEMPORARY_DEFINITION", DefaultLanguageHighlighterColors.REASSIGNED_LOCAL_VARIABLE)
        val temporaryVariable = TextAttributesKey
            .createTextAttributesKey("TEMPORARY_VARIABLE", DefaultLanguageHighlighterColors.REASSIGNED_LOCAL_VARIABLE)
    }

    override fun getHighlightingLexer() = SmalltalkLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        return when (tokenType) {
            SmalltalkTypes.IDENTIFIER -> key(identifier)
            SmalltalkTypes.STATEMENT_SEPERATOR -> key(statementSeperator)
            SmalltalkTypes.NUMBER -> key(number)
            SmalltalkTypes.METHOD -> key(methodDeclaration)
            SmalltalkTypes.PARENTHESIS_END -> key(parenthesis)
            SmalltalkTypes.PARENTHESIS_START -> key(parenthesis)
            SmalltalkTypes.NIL -> key(keyword)
            SmalltalkTypes.TRUE -> key(keyword)
            SmalltalkTypes.FALSE -> key(keyword)
            SmalltalkTypes.SELF -> key(keyword)
            SmalltalkTypes.SUPER -> key(keyword)
            SmalltalkTypes.STRING -> key(string)
            SmalltalkTypes.ASSIGNMENT_OPERATOR -> key(assignment)
            SmalltalkTypes.COMMENT -> key(comment)
            SmalltalkTypes.TEMPORARIES -> key(temporaryDefinition)
            else -> arrayOf()
        }
    }

    private fun key(key: TextAttributesKey) = arrayOf(key)
}
