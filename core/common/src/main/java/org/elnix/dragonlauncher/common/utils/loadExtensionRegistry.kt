package org.elnix.dragonlauncher.common.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.elnix.dragonlauncher.common.serializables.ExtensionModel
import java.io.InputStreamReader

suspend fun loadExtensionRegistry(context: Context): List<ExtensionModel>? = withContext(Dispatchers.IO) {
    try {
        context.assets.open("extensions-registry.json").use { inputStream ->
            val reader = InputStreamReader(inputStream)
            val listType = object : TypeToken<List<ExtensionModel>>() {}.type
            Gson().fromJson(reader, listType)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
