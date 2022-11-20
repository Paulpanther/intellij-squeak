package com.paulpanther.intellijsqueak.runConfiguration

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.paulpanther.intellijsqueak.services.squeak
import com.paulpanther.intellijsqueak.ui.runConfiguration.SmalltalkRunConfigurationSettingsEditor
import java.io.File

class SmalltalkRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
): RunConfigurationBase<SmalltalkRunConfigurationOptions>(project, factory, name) {
    var code by options::code

    override fun getState(
        executor: Executor,
        environment: ExecutionEnvironment
    ) = RunProfileState { _, _ ->
        val toEvaluate = code
        if (squeak.open && toEvaluate != null) {
            squeak.evaluate(toEvaluate) { /* Ignore result */ }
        }

        DefaultExecutionResult()
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return SmalltalkRunConfigurationSettingsEditor()
    }

    override fun getOptions(): SmalltalkRunConfigurationOptions {
        return super.getOptions() as SmalltalkRunConfigurationOptions
    }

    override fun checkConfiguration() {
    }
}
