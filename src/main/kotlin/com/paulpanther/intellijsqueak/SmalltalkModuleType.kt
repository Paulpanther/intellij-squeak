package com.paulpanther.intellijsqueak

import com.intellij.openapi.module.ModuleType

object SmalltalkModuleType: ModuleType<SmalltalkModuleBuilder>("SMALLTALK_MODULE_TYPE") {
    override fun createModuleBuilder() = SmalltalkModuleBuilder()
    override fun getName() = "Smalltalk"
    override fun getDescription() = "Smalltalk module"
    override fun getNodeIcon(isOpened: Boolean) = SmalltalkIcons.smalltalk
}
