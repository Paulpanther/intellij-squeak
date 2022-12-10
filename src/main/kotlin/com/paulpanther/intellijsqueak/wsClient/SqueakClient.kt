package com.paulpanther.intellijsqueak.wsClient

import com.google.gson.annotations.SerializedName
import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.paulpanther.intellijsqueak.vfs.*

private class SqueakFileContentAction(
    val file: String,
    @SerializedName("class")
    val clazz: String,
    val isInstance: Boolean)

private class SqueakNewMethodAction(
    val method: String,
    val category: String,
    @SerializedName("class")
    val clazz: String,
    val isInstance: Boolean)

private class SqueakNewCategoryAction(
    val category: String,
    @SerializedName("class")
    val clazz: String,
    val isInstance: Boolean)

private class SqueakNewClassAction(
    @SerializedName("class")
    val clazz: String,
    @SerializedName("package")
    val packageName: String)

private class SqueakNewPackageAction(
    @SerializedName("package")
    val packageName: String)

private class SqueakRenameMethodAction(
    val method: String,
    @SerializedName("class")
    val clazz: String,
    val isInstance: Boolean,
    val newName: String)

private class SqueakRenameCategoryAction(
    val category: String,
    @SerializedName("class")
    val clazz: String,
    val isInstance: Boolean,
    val newName: String)

private class SqueakRenameClassAction(
    @SerializedName("class")
    val clazz: String,
    val newName: String)

private class SqueakRenamePackageAction(
    @SerializedName("package")
    val packageName: String,
    val newName: String)

private class SqueakWriteFileAction(
    val file: String,
    @SerializedName("class")
    val clazz: String,
    val isInstance: Boolean,
    val content: String)

private class SqueakRefreshCategoryAction(
    @SerializedName("class")
    val clazz: String,
    val category: String,
    val isInstance: Boolean)

private class SqueakRefreshClassAction(
    @SerializedName("class")
    val clazz: String)

private class SqueakRefreshPackageAction(
    @SerializedName("package")
    val packageName: String)

private class SqueakRemoveMethodAction(
    @SerializedName("class")
    val clazz: String,
    val isInstance: Boolean,
    val method: String)

private class SqueakRemoveCategoryAction(
    @SerializedName("class")
    val clazz: String,
    val isInstance: Boolean,
    val category: String)

private class SqueakRemoveClassAction(
    @SerializedName("class")
    val clazz: String)

private class SqueakRemovePackageAction(
    @SerializedName("package")
    val packageName: String)

private class SqueakClassDataAction(
    @SerializedName("class")
    val clazz: String)

data class SqueakMethodData(
    val name: String)

data class SqueakCategoryData(
    val name: String,
    val isInstance: Boolean,
    val methods: List<SqueakMethodData>)

data class SqueakClassData(
    val name: String,
    val instanceVariables: List<String>,
    val classVariables: List<String>,
    val superClass: String,
    val comment: String,
    val categories: List<SqueakCategoryData>)

data class SqueakPackageData(
    val name: String,
    val classes: List<SqueakClassData>)

data class SqueakFileSystemData(
    val children: List<SqueakPackageData>)

/**
 * Connection to squeak.
 * Methods:
 * - blocking file_content(clazz, file) -> String
 * - write_file(clazz, file, content)
 * - async refresh_file_system -> RefreshFileSystemResult
 * - repeating transcript -> String
 */
class SqueakClient(parent: Disposable): WSClient(parent) {
    init {
        Disposer.register(parent, this)
    }

    fun evaluate(code: String, callback: (result: String) -> Unit) {
        sendAsync("evaluate", code, callback)
    }

    fun fileContent(clazz: SmalltalkVirtualFileClass, file: SmalltalkVirtualFileMethod): String? {
        return sendBlocking("file_content", SqueakFileContentAction(file.name, clazz.name, file.isInstance))
    }

    fun newMethod(clazz: SmalltalkVirtualFileClass, category: SmalltalkVirtualFileCategory, method: String, callback: (success: Boolean) -> Unit) {
        sendAsync("new_method", SqueakNewMethodAction(method, category.name, clazz.name, category.isInstance), callback)
    }

    fun newCategory(clazz: SmalltalkVirtualFileClass, category: String, isInstance: Boolean, callback: (success: Boolean) -> Unit) {
        sendAsync("new_category", SqueakNewCategoryAction(category, clazz.name, isInstance), callback)
    }

    fun newClass(packagee: SmalltalkVirtualFilePackage, clazz: String, callback: (success: Boolean) -> Unit) {
        sendAsync("new_class", SqueakNewClassAction(clazz, packagee.name), callback)
    }

    fun newPackage(packagee: String, callback: (success: Boolean) -> Unit) {
        sendAsync("new_package", SqueakNewPackageAction(packagee), callback)
    }

    fun renameMethod(clazz: SmalltalkVirtualFileClass, method: SmalltalkVirtualFileMethod, newName: String, callback: (success: Boolean) -> Unit) {
        sendAsync("rename_method", SqueakRenameMethodAction(method.name, clazz.name, method.isInstance, newName), callback)
    }

    fun renameCategory(clazz: SmalltalkVirtualFileClass, category: SmalltalkVirtualFileCategory, newName: String, callback: (success: Boolean) -> Unit) {
        sendAsync("rename_category", SqueakRenameCategoryAction(category.name, clazz.name, category.isInstance, newName), callback)
    }

    fun renameClass(clazz: SmalltalkVirtualFileClass, newName: String, callback: (success: Boolean) -> Unit) {
        sendAsync("rename_class", SqueakRenameClassAction(clazz.name, newName), callback)
    }

    fun renamePackage(packagee: SmalltalkVirtualFilePackage, newName: String, callback: (success: Boolean) -> Unit) {
        sendAsync("rename_package", SqueakRenamePackageAction(packagee.name, newName), callback)
    }

    fun writeFile(clazz: SmalltalkVirtualFileClass, file: SmalltalkVirtualFileMethod, content: String) {
        sendAsync<Boolean>("write_file", SqueakWriteFileAction(file.name, clazz.name, file.isInstance, content)) {}
    }

    fun refreshFileSystem(system: SmalltalkVirtualFileSystem, onResult: () -> Unit) {
        sendAsync<SqueakFileSystemData>("refresh_file_system") {
            system.root = it.toVirtualFileRoot(system)
            onResult()
        }
    }

    fun refreshPackage(packageNode: SmalltalkVirtualFilePackage, onResult: () -> Unit) {
        sendAsync<SqueakPackageData>(
            "refresh_package",
            SqueakRefreshPackageAction(packageNode.name)
        ) {
            val system = packageNode.fileSystem
            packageNode.myChildren.clear()
            packageNode.myChildren.addAll(it.toVirtualFilePackage(system, system.root).classes)
            onResult()
        }
    }

    fun refreshClass(classNode: SmalltalkVirtualFileClass, onResult: () -> Unit) {
        sendAsync<SqueakClassData>(
            "refresh_class",
            SqueakRefreshClassAction(classNode.name)
        ) {
            val system = classNode.fileSystem
            classNode.myChildren.clear()
            classNode.myChildren.addAll(it.toVirtualFileClass(system, classNode.packageNode).categories)
            onResult()
        }
    }

    fun refreshCategory(categoryNode: SmalltalkVirtualFileCategory, onResult: () -> Unit) {
        sendAsync<SqueakCategoryData>(
            "refresh_category",
            SqueakRefreshCategoryAction(categoryNode.classNode.name, categoryNode.name, categoryNode.isInstance)
        ) {
            val system = categoryNode.fileSystem
            categoryNode.myChildren.clear()
            categoryNode.myChildren.addAll(it.toVirtualFileCategory(system, categoryNode.classNode).methods)
            onResult()
        }
    }

    fun removeMethod(clazz: SmalltalkVirtualFileClass, method: SmalltalkVirtualFileMethod, callback: (success: Boolean) -> Unit) {
        sendAsync("remove_method", SqueakRemoveMethodAction(clazz.name, method.isInstance, method.name), callback)
    }

    fun removeCategory(clazz: SmalltalkVirtualFileClass, category: SmalltalkVirtualFileCategory, callback: (success: Boolean) -> Unit) {
        sendAsync("remove_category", SqueakRemoveCategoryAction(clazz.name, category.isInstance, category.name), callback)
    }

    fun removeClass(clazz: SmalltalkVirtualFileClass, callback: (success: Boolean) -> Unit) {
        sendAsync("remove_class", SqueakRemoveClassAction(clazz.name), callback)
    }

    fun removePackage(packagee: SmalltalkVirtualFilePackage, callback: (success: Boolean) -> Unit) {
        sendAsync("remove_package", SqueakRemovePackageAction(packagee.name), callback)
    }

    fun onTranscriptChange(listener: (msg: String) -> Unit) {
        onMessageWithType("transcript") {
            listener(gson.fromJson(it, String::class.java))
        }
    }

    private inline fun <reified T> sendAsync(action: String, msg: Any = "", crossinline callback: (msg: T) -> Unit) {
        sendAsyncWithRawResponse(action, msg) {
            callback(gson.fromJson(it, T::class.java))
        }
    }

    private inline fun <reified T> sendBlocking(action: String, msg: Any = ""): T? {
        val raw = sendBlockingWithRawResponse(action, msg) ?: return null
        return gson.fromJson(raw, T::class.java)
    }
}
