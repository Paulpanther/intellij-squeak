package com.paulpanther.intellijsqueak.editor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.paulpanther.intellijsqueak.ui.editor.ClassFileEditor
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileClass


class ClassFileEditorProvider: FileEditorProvider {
    override fun accept(project: Project, file: VirtualFile): Boolean {
        return file is SmalltalkVirtualFileClass
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        if (file !is SmalltalkVirtualFileClass) error("Should be Class")
        return ClassFileEditor(project, file)
    }

    override fun getEditorTypeId(): String {
        return "Smalltalk.ClassEditor"
    }

    override fun getPolicy(): FileEditorPolicy {
        return FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR
    }
}
