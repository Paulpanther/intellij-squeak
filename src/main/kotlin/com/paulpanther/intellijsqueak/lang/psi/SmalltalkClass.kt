package com.paulpanther.intellijsqueak.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.PsiManagerImpl
import com.intellij.psi.impl.file.PsiDirectoryImpl
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileClass

class SmalltalkClass(
    project: Project,
    val classFile: SmalltalkVirtualFileClass
): PsiDirectoryImpl(PsiManager.getInstance(project) as PsiManagerImpl, classFile) {

    override fun navigate(requestFocus: Boolean) {
        classFile.currentNode?.navigate(requestFocus)
    }
}
