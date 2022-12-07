package com.paulpanther.intellijsqueak.lang.util

import com.paulpanther.intellijsqueak.lang.SmalltalkVariable
import com.paulpanther.intellijsqueak.lang.references.SmalltalkArgumentVariableReference
import com.paulpanther.intellijsqueak.lang.references.SmalltalkClassReference
import com.paulpanther.intellijsqueak.lang.references.SmalltalkTemporaryVariableReference

fun SmalltalkVariable.isTemporary() =
    SmalltalkTemporaryVariableReference(this).resolve() != null

fun SmalltalkVariable.isClassRef() =
    SmalltalkClassReference(this).resolve() != null

fun SmalltalkVariable.isArgument() =
    SmalltalkArgumentVariableReference(this).resolve() != null
