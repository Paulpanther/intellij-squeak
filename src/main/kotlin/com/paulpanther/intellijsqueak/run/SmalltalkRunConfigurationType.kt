package com.paulpanther.intellijsqueak.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.paulpanther.intellijsqueak.SmalltalkIcons

class SmalltalkRunConfigurationType: ConfigurationType {
    companion object {
        val id = "SmalltalkRunConfiguration"
    }

    override fun getDisplayName() = "Smalltalk"

    override fun getConfigurationTypeDescription() =
        "Smalltalk run configuration type"

    override fun getIcon() = SmalltalkIcons.file

    override fun getId() = SmalltalkRunConfigurationType.id

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf(SmalltalkRunConfigurationFactory(this))
    }
}
