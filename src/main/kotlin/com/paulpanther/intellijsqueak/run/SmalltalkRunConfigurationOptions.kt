package com.paulpanther.intellijsqueak.run

import com.intellij.execution.configurations.RunConfigurationOptions
import com.paulpanther.intellijsqueak.getValue
import com.paulpanther.intellijsqueak.setValue

class SmalltalkRunConfigurationOptions: RunConfigurationOptions() {
    var scriptName by string("").provideDelegate(this, "scriptName")
    var entryPoint by string("").provideDelegate(this, "entryPoint")
}

