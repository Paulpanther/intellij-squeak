package com.paulpanther.intellijsqueak

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.openapi.options.colors.RainbowColorSettingsPage

class SmalltalkColorSettingsPage: ColorSettingsPage, RainbowColorSettingsPage {

    companion object {
        val descriptors = arrayOf(
            AttributesDescriptor("Identifier", SmalltalkSyntaxHighlighter.identifier),
            AttributesDescriptor("Dot", SmalltalkSyntaxHighlighter.statementSeperator),
            AttributesDescriptor("Number", SmalltalkSyntaxHighlighter.number),
            AttributesDescriptor("Method declaration", SmalltalkSyntaxHighlighter.methodDeclaration),
            AttributesDescriptor("Parenthesis", SmalltalkSyntaxHighlighter.parenthesis),
            AttributesDescriptor("String", SmalltalkSyntaxHighlighter.string),
            AttributesDescriptor("Keyword", SmalltalkSyntaxHighlighter.keyword),
            AttributesDescriptor("Assignment", SmalltalkSyntaxHighlighter.assignment),
        )
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName() = "Smalltalk"

    override fun getIcon() = SmalltalkIcons.file

    override fun getHighlighter(): SyntaxHighlighter =
        SyntaxHighlighterFactory.getSyntaxHighlighter(SmalltalkLanguage, null, null)

    override fun getAttributeDescriptors() = descriptors

    override fun getDemoText(): String {
        return "foo\n" +
                "    | a |\n" +
                "    a := (42).\n" +
                "    'Hello World'.\n" +
                "    true."
    }

    override fun getAdditionalHighlightingTagToDescriptorMap() = null

    override fun isRainbowType(type: TextAttributesKey?) =
        type == SmalltalkSyntaxHighlighter.temporaryVariable

    override fun getLanguage() = SmalltalkLanguage
}
