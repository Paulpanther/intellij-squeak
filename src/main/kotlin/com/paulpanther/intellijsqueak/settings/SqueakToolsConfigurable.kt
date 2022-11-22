package com.paulpanther.intellijsqueak.settings

import com.intellij.openapi.options.Configurable
import com.paulpanther.intellijsqueak.services.squeak
import com.paulpanther.intellijsqueak.ui.settings.SqueakToolsSettings

class SqueakToolsConfigurable: Configurable {
    private val component = SqueakToolsSettings()

    override fun getDisplayName() = "Squeak/ Smalltalk"
    override fun createComponent() = component.panel

    override fun isModified(): Boolean {
        return squeak.state.squeakPath != component.squeakPath.text
                || squeak.state.squeakImage != component.squeakImage.text
                || squeak.isEnabled != component.enableSqueak.isSelected
    }

    override fun apply() {
        squeak.state.squeakPath = component.squeakPath.text
        squeak.state.squeakImage = component.squeakImage.text
        squeak.isEnabled = component.enableSqueak.isSelected
    }

    override fun reset() {
        component.squeakPath.text = squeak.state.squeakPath
        component.squeakImage.text = squeak.state.squeakImage
        component.enableSqueak.isSelected = squeak.isEnabled
    }
}
