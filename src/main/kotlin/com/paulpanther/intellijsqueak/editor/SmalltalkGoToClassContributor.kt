package com.paulpanther.intellijsqueak.editor

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.paulpanther.intellijsqueak.lang.psi.SmalltalkClass
import com.paulpanther.intellijsqueak.services.squeak

class SmalltalkGoToClassContributor: ChooseByNameContributor {
    override fun getNames(project: Project?, includeNonProjectItems: Boolean): Array<String> {
        return squeak.fileSystem.classes.map { it.name }.toTypedArray()
    }

    override fun getItemsByName(
        name: String?,
        pattern: String?,
        project: Project?,
        includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        if (name == null || project == null) return arrayOf()
        val clazz = squeak.fileSystem.classWithName(name) ?: return arrayOf()
        return arrayOf(SmalltalkClass(project, clazz))
    }
}
