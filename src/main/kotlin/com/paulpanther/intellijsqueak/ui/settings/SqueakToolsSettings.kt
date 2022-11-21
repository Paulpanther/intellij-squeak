package com.paulpanther.intellijsqueak.ui.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import javax.swing.JCheckBox
import javax.swing.JPanel

// TODO change to kotlin UI dsl
class SqueakToolsSettings {
    lateinit var panel: JPanel
    lateinit var squeakPathComponent: LabeledComponent<TextFieldWithBrowseButton>
    lateinit var squeakImageComponent: LabeledComponent<TextFieldWithBrowseButton>
    lateinit var enableSqueak: JCheckBox

    val squeakPath get() = squeakPathComponent.component.textField
    val squeakImage get() = squeakImageComponent.component.textField

    private fun createUIComponents() {
        squeakPathComponent = createLabeledBrowseTextField(
            "Choose Squeak Executable",
            "This file will be run with the provided image")
        squeakImageComponent = createLabeledBrowseTextField(
            "Choose Squeak Image",
            "The Image that will connect to") {
            withFileFilter { it.extension == "image" }
        }
        enableSqueak = JCheckBox("Enable Squeak Integration")
    }

    private fun createLabeledBrowseTextField(
        title: String,
        description: String,
        choose: FileChooserDescriptor.() -> FileChooserDescriptor = { this },
    ): LabeledComponent<TextFieldWithBrowseButton> {
        return LabeledComponent<TextFieldWithBrowseButton>().apply {
            component = TextFieldWithBrowseButton().apply {
                val chooser =
                    FileChooserDescriptorFactory.createSingleFileDescriptor()
                        .withTitle(title)
                addBrowseFolderListener(title, description, null, choose(chooser))
            }
        }
    }
}
