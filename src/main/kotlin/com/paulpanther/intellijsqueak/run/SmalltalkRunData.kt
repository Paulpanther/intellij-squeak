package com.paulpanther.intellijsqueak.run

import com.google.gson.Gson
import java.io.File


sealed class SmalltalkRunDataAction(
    val type: String)

data class SmalltalkRunDataActionAddMethod(
    val code: String,
    val clazz: String,
): SmalltalkRunDataAction("addMethod")

data class SmalltalkRunDataActionAddClass(
    val name: String
): SmalltalkRunDataAction("addClass")

sealed class SmalltalkRunDataType(
    val type: String)

data class SmalltalkRunDataRunScript(
    val actions: List<SmalltalkRunDataAction>,
    val entryPoint: String
): SmalltalkRunDataType("runScript")

data class SmalltalkRunDataEvaluate(
    val code: String
): SmalltalkRunDataType("evaluate")

class SmalltalkRunData(
    private val file: String?,
    private val entryPoint: String?
) {
    fun toJson(): String? {
        val code = File(file ?: return null).readText()
        val data = SmalltalkRunDataRunScript(
            listOf(
                SmalltalkRunDataActionAddClass("PluginRunner"),
                SmalltalkRunDataActionAddMethod(code, "PluginRunner")),
            entryPoint ?: return null)
        return Gson().toJson(data)
    }
}
