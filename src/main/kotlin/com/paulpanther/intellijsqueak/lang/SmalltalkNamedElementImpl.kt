package com.paulpanther.intellijsqueak.lang

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

abstract class SmalltalkNamedElementImpl(node: ASTNode): ASTWrapperPsiElement(node), SmalltalkNamedElement
