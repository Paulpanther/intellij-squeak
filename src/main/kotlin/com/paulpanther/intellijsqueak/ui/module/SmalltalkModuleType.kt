package com.paulpanther.intellijsqueak.ui.module

import com.intellij.openapi.module.ModuleType
import com.paulpanther.intellijsqueak.ui.SmalltalkIcons

object SmalltalkModuleType: ModuleType<SmalltalkModuleBuilder>("SMALLTALK_MODULE_TYPE") {
    override fun createModuleBuilder() = SmalltalkModuleBuilder()
    override fun getName() = "Smalltalk"
    override fun getDescription() = "Smalltalk module"
    override fun getNodeIcon(isOpened: Boolean) = SmalltalkIcons.smalltalk
}
