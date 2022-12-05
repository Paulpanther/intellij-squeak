package com.paulpanther.intellijsqueak.lang.definition

fun isValidClassVariableName(name: String) = isValidVariableName(name)
fun isValidVariableName(name: String) = Regex("[a-zA-Z]\\w*").matches(name)
