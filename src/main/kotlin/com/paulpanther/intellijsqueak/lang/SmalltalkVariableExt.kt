package com.paulpanther.intellijsqueak.lang

fun SmalltalkVariable.isTemporary() =
    SmalltalkTemporaryVariableReference(this).resolve() != null
