package com.paulpanther.intellijsqueak.ui.module

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import javax.swing.JComponent
import javax.swing.JLabel

class SmalltalkModuleBuilderStep: ModuleWizardStep() {
    override fun getComponent(): JComponent {
        return JLabel("Hello World")
    }

    override fun updateDataModel() {
    }
}
