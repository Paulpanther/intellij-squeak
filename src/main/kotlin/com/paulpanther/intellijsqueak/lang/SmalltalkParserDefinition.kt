package com.paulpanther.intellijsqueak.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.paulpanther.intellijsqueak.SmalltalkLanguage

class SmalltalkParserDefinition: ParserDefinition {
    companion object {
//        val whiteSpace = TokenSet.create(SmalltalkTypes.WH)
        val comment = TokenSet.create(SmalltalkTypes.COMMENT)
        val file = IFileElementType(SmalltalkLanguage)
    }

    override fun createLexer(project: Project?) = SmalltalkLexerAdapter()
    override fun createParser(project: Project?) = SmalltalkParser()

    override fun getFileNodeType() = file
    override fun getCommentTokens() = comment
    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

    override fun createElement(node: ASTNode?): PsiElement =
        SmalltalkTypes.Factory.createElement(node)
    override fun createFile(viewProvider: FileViewProvider) =
        SmalltalkFile(viewProvider)
}
