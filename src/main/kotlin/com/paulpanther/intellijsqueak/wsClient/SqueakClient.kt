package com.paulpanther.intellijsqueak.wsClient

import com.google.gson.annotations.SerializedName
import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileCategory
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileClass
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFilePackage
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileSystem

private class SqueakFileContentAction(
    val file: String,
    @SerializedName("class")
    val clazz: String)

private class SqueakNewMethodAction(
    val method: String,
    val category: String,
    @SerializedName("class")
    val clazz: String)

private class SqueakNewCategoryAction(
    val category: String,
    @SerializedName("class")
    val clazz: String)

private class SqueakNewClassAction(
    @SerializedName("class")
    val clazz: String,
    @SerializedName("package")
    val packageName: String)

private class SqueakNewPackageAction(
    @SerializedName("package")
    val packageName: String)

private class SqueakWriteFileAction(
    val file: String,
    @SerializedName("class")
    val clazz: String,
    val content: String)

private class SqueakRefreshCategoryAction(
    @SerializedName("class")
    val clazz: String,
    val category: String)

private class SqueakRefreshClassAction(
    @SerializedName("class")
    val clazz: String)

private class SqueakRefreshPackageAction(
    @SerializedName("package")
    val packageName: String)

data class SqueakRefreshFileSystemResult(
    val name: String,
    val children: List<SqueakRefreshFileSystemResult>)

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

    fun fileContent(clazz: String, file: String): String? {
        return sendBlocking("file_content", SqueakFileContentAction(file, clazz))
    }

    fun newMethod(clazz: String, category: String, method: String, callback: (success: Boolean) -> Unit) {
        sendAsync("new_method", SqueakNewMethodAction(method, category, clazz), callback)
    }

    fun newCategory(clazz: String, category: String, callback: (success: Boolean) -> Unit) {
        sendAsync("new_category", SqueakNewCategoryAction(category, clazz), callback)
    }

    fun newClass(packageName: String, clazz: String, callback: (success: Boolean) -> Unit) {
        sendAsync("new_class", SqueakNewClassAction(clazz, packageName), callback)
    }

    fun newPackage(packageName: String, callback: (success: Boolean) -> Unit) {
        sendAsync("new_package", SqueakNewPackageAction(packageName), callback)
    }

    fun writeFile(clazz: String, file: String, content: String) {
        sendAsync<Boolean>("write_file", SqueakWriteFileAction(file, clazz, content)) {}
    }

    fun refreshFileSystem(system: SmalltalkVirtualFileSystem, onResult: () -> Unit) {
        sendAsync<SqueakRefreshFileSystemResult>("refresh_file_system") {
            system.root = it.toVirtualFileRoot(system)
            onResult()
        }
    }

    fun refreshPackage(packageNode: SmalltalkVirtualFilePackage, onResult: () -> Unit) {
        sendAsync<SqueakRefreshFileSystemResult>(
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
        sendAsync<SqueakRefreshFileSystemResult>(
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
        sendAsync<SqueakRefreshFileSystemResult>(
            "refresh_category",
            SqueakRefreshCategoryAction(categoryNode.classNode.name, categoryNode.name)
        ) {
            val system = categoryNode.fileSystem
            categoryNode.myChildren.clear()
            categoryNode.myChildren.addAll(it.toVirtualFileCategory(system, categoryNode.classNode).methods)
            onResult()
        }
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
