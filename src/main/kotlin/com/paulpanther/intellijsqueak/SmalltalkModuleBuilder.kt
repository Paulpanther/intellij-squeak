package com.paulpanther.intellijsqueak

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable

class SmalltalkModuleBuilder: ModuleBuilder() {
    override fun getModuleType() = SmalltalkModuleType

    override fun getCustomOptionsStep(
        context: WizardContext?,
        parentDisposable: Disposable?
    ) = SmalltalkModuleBuilderStep()
}
