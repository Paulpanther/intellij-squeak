package com.paulpanther.intellijsqueak

import com.intellij.ide.wizard.AbstractNewProjectWizardStep
import com.intellij.ide.wizard.NewProjectWizardLanguageStep
import com.intellij.openapi.project.Project

class SmalltalkNewProjectWizardLanguageStep(
    parent: NewProjectWizardLanguageStep
): AbstractNewProjectWizardStep(parent) {

    override fun setupProject(project: Project) {
        super.setupProject(project)

        val builder = SmalltalkModuleBuilder()
        builder.commit(project)
    }
}
