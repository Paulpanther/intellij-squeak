package com.paulpanther.intellijsqueak.runConfiguration

import com.intellij.execution.configurations.RunConfigurationOptions
import com.paulpanther.intellijsqueak.getValue
import com.paulpanther.intellijsqueak.setValue

class SmalltalkRunConfigurationOptions: RunConfigurationOptions() {
    var code by string("").provideDelegate(this, "code")
}

