package com.paulpanther.intellijsqueak.runConfiguration

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.paulpanther.intellijsqueak.ui.SmalltalkIcons

class SmalltalkRunConfigurationType: ConfigurationType {
    companion object {
        val id = "SmalltalkRunConfiguration"
    }

    override fun getDisplayName() = "Smalltalk"

    override fun getConfigurationTypeDescription() =
        "Smalltalk run configuration type"

    override fun getIcon() = SmalltalkIcons.smalltalk

    override fun getId() = SmalltalkRunConfigurationType.id

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf(SmalltalkRunConfigurationFactory(this))
    }
}
