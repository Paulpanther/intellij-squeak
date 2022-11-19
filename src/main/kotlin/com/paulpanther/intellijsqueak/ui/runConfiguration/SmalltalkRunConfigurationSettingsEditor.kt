package com.paulpanther.intellijsqueak.ui.runConfiguration;

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.paulpanther.intellijsqueak.runConfiguration.SmalltalkRunConfiguration
import javax.swing.JPanel


class SmalltalkRunConfigurationSettingsEditor:
	SettingsEditor<SmalltalkRunConfiguration>() {
	private lateinit var myPanel: JPanel
	private lateinit var myCode: LabeledComponent<TextFieldWithBrowseButton>

	override fun resetEditorFrom(demoRunConfiguration: SmalltalkRunConfiguration) {
		myCode.component.text = demoRunConfiguration.code ?: return
	}

	override fun applyEditorTo(runConfig: SmalltalkRunConfiguration) {
		runConfig.code = myCode.component.text
	}

	override fun createEditor() = myPanel

	private fun createUIComponents() {
		myCode = LabeledComponent()
		myCode.component = TextFieldWithBrowseButton()
	}
}
