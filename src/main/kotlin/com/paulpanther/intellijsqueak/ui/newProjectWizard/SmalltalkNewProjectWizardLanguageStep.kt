package com.paulpanther.intellijsqueak.ui.newProjectWizard

import com.intellij.ide.wizard.AbstractNewProjectWizardStep
import com.intellij.ide.wizard.NewProjectWizardLanguageStep
import com.intellij.openapi.project.Project
import com.paulpanther.intellijsqueak.ui.module.SmalltalkModuleBuilder

class SmalltalkNewProjectWizardLanguageStep(
    parent: NewProjectWizardLanguageStep
): AbstractNewProjectWizardStep(parent) {

    override fun setupProject(project: Project) {
        super.setupProject(project)

        val builder = SmalltalkModuleBuilder()
        builder.commit(project)
    }
}
