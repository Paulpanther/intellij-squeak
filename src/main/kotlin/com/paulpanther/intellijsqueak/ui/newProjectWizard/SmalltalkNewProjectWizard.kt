package com.paulpanther.intellijsqueak.ui.newProjectWizard

import com.intellij.ide.wizard.LanguageNewProjectWizard
import com.intellij.ide.wizard.NewProjectWizardLanguageStep
import com.intellij.ide.wizard.NewProjectWizardStep

class SmalltalkNewProjectWizard: LanguageNewProjectWizard {
    override val name = "Smalltalk"

    override val ordinal = 2

    override fun createStep(parent: NewProjectWizardLanguageStep): NewProjectWizardStep {
        return SmalltalkNewProjectWizardLanguageStep(parent)
    }
}
