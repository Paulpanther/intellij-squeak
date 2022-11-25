package com.paulpanther.intellijsqueak.editor

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.paulpanther.intellijsqueak.services.smalltalkService
import com.paulpanther.intellijsqueak.services.squeak

class SmalltalkGotoClassContributor: ChooseByNameContributor {
    override fun getNames(
        project: Project?,
        includeNonProjectItems: Boolean
    ) = squeak.fileSystem.classes.map { it.name }.toTypedArray()

    override fun getItemsByName(
        name: String?,
        pattern: String?,
        project: Project?,
        includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        name ?: return arrayOf()
        project ?: return arrayOf()

        val clazz = squeak.fileSystem.classByName(name) ?: return arrayOf()
        val nav = project.smalltalkService.structure.navigatableForClass(clazz) ?: return arrayOf()
        return arrayOf(nav)
    }
}
