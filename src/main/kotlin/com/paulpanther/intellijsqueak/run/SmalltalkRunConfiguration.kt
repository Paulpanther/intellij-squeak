package com.paulpanther.intellijsqueak.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project

private const val connector = "/home/paul/pluginTest.py"

class SmalltalkRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String
): RunConfigurationBase<SmalltalkRunConfigurationOptions>(project, factory, name) {
    var scriptName by options::scriptName
    var entryPoint by options::entryPoint

    override fun getState(
        executor: Executor,
        environment: ExecutionEnvironment
    ): RunProfileState {
        return object: CommandLineState(environment) {
            override fun startProcess(): ProcessHandler {
                val cmd = GeneralCommandLine(connector, SmalltalkRunData(scriptName, entryPoint).toJson())
                val process = ProcessHandlerFactory.getInstance().createColoredProcessHandler(cmd)
                ProcessTerminatedListener.attach(process)
                return process
            }
        }
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
