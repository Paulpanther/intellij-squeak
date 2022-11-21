package com.paulpanther.intellijsqueak.ui.module

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable

class SmalltalkModuleBuilder: ModuleBuilder() {
    override fun getModuleType() = SmalltalkModuleType.instance

    override fun getCustomOptionsStep(
        context: WizardContext?,
        parentDisposable: Disposable?
    ) = SmalltalkModuleBuilderStep()

    override fun getWeight() = 2000
}
