package com.paulpanther.intellijsqueak.ui.module

import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeManager
import com.paulpanther.intellijsqueak.ui.SmalltalkIcons

const val id = "SMALLTALK_MODULE_TYPE"

class SmalltalkModuleType: ModuleType<SmalltalkModuleBuilder>(id) {
    override fun createModuleBuilder() = SmalltalkModuleBuilder()
    override fun getName() = "Smalltalk"
    override fun getDescription() = "Smalltalk module"
    override fun getNodeIcon(isOpened: Boolean) = SmalltalkIcons.smalltalk

    companion object {
        val instance get() = ModuleTypeManager.getInstance().findByID(id)
    }
}
