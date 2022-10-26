package com.paulpanther.intellijsqueak.run;

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import javax.swing.JPanel


class SmalltalkRunConfigurationSettingsEditor:
	SettingsEditor<SmalltalkRunConfiguration>() {
	private lateinit var myPanel: JPanel
	private lateinit var myScriptName: LabeledComponent<TextFieldWithBrowseButton>
	private lateinit var myEntryPoint: LabeledComponent<TextFieldWithBrowseButton>

	override fun resetEditorFrom(demoRunConfiguration: SmalltalkRunConfiguration) {
		myScriptName.component.text = demoRunConfiguration.scriptName ?: return
		myEntryPoint.component.text = demoRunConfiguration.entryPoint ?: return
	}

	override fun applyEditorTo(runConfig: SmalltalkRunConfiguration) {
		runConfig.scriptName = myScriptName.component.text
		runConfig.entryPoint = myEntryPoint.component.text
	}

	override fun createEditor() = myPanel

	private fun createUIComponents() {
		myScriptName = LabeledComponent()
		myScriptName.component = TextFieldWithBrowseButton()
		myEntryPoint = LabeledComponent()
		myEntryPoint.component = TextFieldWithBrowseButton()
	}
}
