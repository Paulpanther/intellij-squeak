package com.paulpanther.intellijsqueak.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.Configurable
import com.paulpanther.intellijsqueak.services.SqueakClientService
import com.paulpanther.intellijsqueak.ui.settings.SqueakToolsSettings

class SqueakToolsConfigurable: Configurable {
    private val component = SqueakToolsSettings()
    private val state get() = service<SqueakClientService>().state

    override fun getDisplayName() = "Squeak/ Smalltalk"
    override fun createComponent() = component.panel

    override fun isModified(): Boolean {
        return state.squeakPath != component.squeakPath.text || state.squeakImage != component.squeakImage.text
    }

    override fun apply() {
        state.squeakPath = component.squeakPath.text
        state.squeakImage = component.squeakImage.text
    }

    override fun reset() {
        component.squeakPath.text = state.squeakPath
        component.squeakImage.text = state.squeakImage
    }
}
