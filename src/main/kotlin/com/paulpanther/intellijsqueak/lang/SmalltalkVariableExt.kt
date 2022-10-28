package com.paulpanther.intellijsqueak.lang

fun SmalltalkVariable.isTemporary() =
    SmalltalkTemporaryVariableReference(this).resolve() != null

fun SmalltalkVariable.isArgument() =
    SmalltalkArgumentVariableReference(this).resolve() != null
