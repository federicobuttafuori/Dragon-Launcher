package org.elnix.dragonlauncher.common.serializables

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.elnix.dragonlauncher.common.logging.logE
import org.elnix.dragonlauncher.common.utils.Constants.Logging.FLOATING_APPS_TAG

@Serializable
data class FloatingAppObject(
    val id: Int,
    val appWidgetId: Int? = null,
    val nestId: Int?,
    val action: SwipeActionSerializable,
    val spanX: Float = 1f,
    val spanY: Float = 1f,
    val x: Float = 0f,
    val y: Float = 0f,
    val angle: Float = 0f,
    val ghosted: Boolean? = false,
    val foreground: Boolean? = true
)


object FloatingAppsJson {
    private val jsonConfig = Json {
        explicitNulls = false
        ignoreUnknownKeys = true
    }

    fun encodeFloatingApps(floatingAppObjects: List<FloatingAppObject>): String =
        jsonConfig.encodeToString(floatingAppObjects)

    fun decodeFloatingApps(json: String): List<FloatingAppObject> {
        return try {
            jsonConfig.decodeFromString<List<FloatingAppObject>>(json)
        } catch (e: Exception) {
            logE(FLOATING_APPS_TAG, e) { "Floating Apps decode failed, using empty list" }
            emptyList()
        }
    }
}