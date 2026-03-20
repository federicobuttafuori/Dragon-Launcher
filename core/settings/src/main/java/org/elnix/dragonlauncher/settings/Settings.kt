package org.elnix.dragonlauncher.settings

import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import org.elnix.dragonlauncher.common.serializables.IconShape
import org.elnix.dragonlauncher.common.serializables.IconShapeGson
import org.elnix.dragonlauncher.common.serializables.SwipeActionSerializable
import org.elnix.dragonlauncher.common.serializables.SwipeJson
import org.elnix.dragonlauncher.common.utils.colors.toHexWithAlpha
import org.elnix.dragonlauncher.settings.bases.BaseSettingObject

/**
 * Factory object for creating typed [org.elnix.dragonlauncher.settings.bases.BaseSettingObject] instances backed by DataStore.
 *
 * This object provides convenient functions to create strongly-typed settings without
 * manually specifying generic parameters or creating dedicated subclasses for every type.
 *
 * Supported types include:
 * - Primitive types: [Boolean], [Int], [Long], [Float], [Double], [String], [Set<String>]
 * - Enum types: any [Enum] using its name as the stored string
 * - Complex types: [Color] and [SwipeActionSerializable], with proper encode/decode handling
 *
 * Each function returns a [org.elnix.dragonlauncher.settings.bases.BaseSettingObject] which can be used to get/set values, reset
 * the setting, or observe changes via flows.
 *
 * Example usage:
 * ```
 * val primaryColor = Settings.color(
 *     key = "primary_color",
 *     dataStoreName = dataStoreName,
 *     default = AmoledDefault.Primary
 * )
 *
 * val swipeAction = Settings.swipeAction(
 *     key = "main_swipe_action",
 *     dataStoreName = dataStoreName,
 *     default = SwipeActionSerializable.OpenDragonLauncherSettings
 * )
 * ```
 */
object Settings {

    fun boolean(
        key: String,
        dataStoreName: DataStoreName,
        default: Boolean,
        onChange: (() -> Unit)? = null
    ): BaseSettingObject<Boolean, Boolean> =
        BaseSettingObject(
            key = key,
            dataStoreName = dataStoreName,
            default = default,
            preferenceKey = booleanPreferencesKey(key),
            encode = { it },
            decode = { raw -> getBooleanStrict(raw, default) },
            onChanged = onChange
        )

    fun int(
        key: String,
        dataStoreName: DataStoreName,
        default: Int,
        allowedRange: ClosedRange<Int>,
        onChange: (() -> Unit)? = null
    ): BaseSettingObject<Int, Int> =
        BaseSettingObject(
            key = key,
            dataStoreName = dataStoreName,
            default = default,
            preferenceKey = intPreferencesKey(key),
            encode = { it },
            decode = { raw -> getIntStrict(raw, default).coerceIn(allowedRange) },
            onChanged = onChange
        )


    fun float(
        key: String,
        dataStoreName: DataStoreName,
        default: Float,
        allowedRange: ClosedRange<Float>,
        onChange: (() -> Unit)? = null
    ): BaseSettingObject<Float, Float> =
        BaseSettingObject(
            key = key,
            dataStoreName = dataStoreName,
            default = default,
            preferenceKey = floatPreferencesKey(key),
            encode = { it },
            decode = { raw -> getFloatStrict(raw, default).coerceIn(allowedRange) },
            onChanged = onChange
        )

    fun long(
        key: String,
        dataStoreName: DataStoreName,
        default: Long,
        allowedRange: ClosedRange<Long>,
        onChange: (() -> Unit)? = null
    ): BaseSettingObject<Long, Long> =
        BaseSettingObject(
            key = key,
            dataStoreName = dataStoreName,
            default = default,
            preferenceKey = longPreferencesKey(key),
            encode = { it },
            decode = { raw -> getLongStrict(raw, default).coerceIn(allowedRange) },
            onChanged = onChange
        )

    fun double(
        key: String,
        dataStoreName: DataStoreName,
        default: Double,
        allowedRange: ClosedRange<Double>,
        onChange: (() -> Unit)? = null
    ): BaseSettingObject<Double, Double> =
        BaseSettingObject(
            key = key,
            dataStoreName = dataStoreName,
            default = default,
            preferenceKey = doublePreferencesKey(key),
            encode = { it },
            decode = { raw -> getDoubleStrict(raw, default).coerceIn(allowedRange) },
            onChanged = onChange
        )


    fun string(
        key: String,
        dataStoreName: DataStoreName,
        default: String,
        onChange: (() -> Unit)? = null
    ): BaseSettingObject<String, String> =
        BaseSettingObject(
            key = key,
            dataStoreName = dataStoreName,
            default = default,
            preferenceKey = stringPreferencesKey(key),
            encode = { it },
            decode = { raw -> getStringStrict(raw, default) },
            onChanged = onChange
        )

    fun stringSet(
        key: String,
        dataStoreName: DataStoreName,
        default: Set<String>,
        onChange: (() -> Unit)? = null
    ): BaseSettingObject<Set<String>, Set<String>> =
        BaseSettingObject(
            key = key,
            dataStoreName = dataStoreName,
            default = default,
            preferenceKey = stringSetPreferencesKey(key),
            encode = { it },
            decode = { raw -> getStringSetStrict(raw, default) },
            onChanged = onChange
        )

    fun <E : Enum<E>> enum(
        key: String,
        dataStoreName: DataStoreName,
        default: E,
        enumClass: Class<E>,
        onChange: (() -> Unit)? = null
    ): BaseSettingObject<E, String> =
        BaseSettingObject(
            key = key,
            dataStoreName = dataStoreName,
            default = default,
            preferenceKey = stringPreferencesKey(key),
            encode = { it.name },
            decode = { raw -> getEnumStrict(raw, default, enumClass) },
            onChanged = onChange
        )

    fun color(
        key: String,
        dataStoreName: DataStoreName,
        default: Color,
        onChange: (() -> Unit)? = null
    ): BaseSettingObject<Color, String> =
        BaseSettingObject(
            key = key,
            dataStoreName = dataStoreName,
            default = default,
            preferenceKey = stringPreferencesKey(key),
            encode = { it.toHexWithAlpha(false) },
            decode = { raw -> getColorStrict(raw, default) },
            onChanged = onChange
        )

    fun swipeAction(
        key: String,
        dataStoreName: DataStoreName,
        default: SwipeActionSerializable,
        onChange: (() -> Unit)? = null
    ): BaseSettingObject<SwipeActionSerializable, String> =
        BaseSettingObject(
            key = key,
            dataStoreName = dataStoreName,
            default = default,
            preferenceKey = stringPreferencesKey(key),
            encode = { raw -> SwipeJson.encodeAction(raw) },
            decode = { raw -> getSwipeActionSerializableStrict(raw, default) },
            onChanged = onChange
        )


    fun shape(
        key: String,
        dataStoreName: DataStoreName,
        default: IconShape,
        onChange: (() -> Unit)? = null
    ): BaseSettingObject<IconShape, String> =
        BaseSettingObject(
            key = key,
            dataStoreName = dataStoreName,
            default = default,
            preferenceKey = stringPreferencesKey(key),
            encode = { value -> IconShapeGson.encode(value) },
            decode = { raw -> IconShapeGson.decode(raw, default) },
            onChanged = onChange
        )
}


// TODO LATER REFACTOR AGAIN TO USE THAT

//class BooleanSetting(
//    private val dataStoreName: DataStoreName,
//    private val default: Boolean
//) {
//    operator fun provideDelegate(
//        thisRef: Any?,
//        prop: KProperty<*>
//    ): BaseSettingObject<Boolean, Boolean> {
//        val key = prop.name
//        return Settings.boolean(
//            key = key,
//            dataStoreName = dataStoreName,
//            default = default
//        )
//    }
//}
//
//
//
//class IntSetting(
//    private val dataStoreName: DataStoreName,
//    private val default: Int
//) {
//    operator fun provideDelegate(
//        thisRef: Any?,
//        prop: KProperty<*>
//    ): BaseSettingObject<Int, Int> {
//        val key = prop.name
//        return Settings.int(
//            key = key,
//            dataStoreName = dataStoreName,
//            default = default
//        )
//    }
//}
//
//
//class FloatSetting(
//    private val dataStoreName: DataStoreName,
//    private val default: Float
//) {
//    operator fun provideDelegate(
//        thisRef: Any?,
//        prop: KProperty<*>
//    ): BaseSettingObject<Float, Float> {
//        val key = prop.name
//        return Settings.float(
//            key = key,
//            dataStoreName = dataStoreName,
//            default = default
//        )
//    }
//}
//
//class LongSetting(
//    private val dataStoreName: DataStoreName,
//    private val default: Long
//) {
//    operator fun provideDelegate(
//        thisRef: Any?,
//        prop: KProperty<*>
//    ): BaseSettingObject<Long, Long> {
//        val key = prop.name
//        return Settings.long(
//            key = key,
//            dataStoreName = dataStoreName,
//            default = default
//        )
//    }
//}
//
//class DoubleSetting(
//    private val dataStoreName: DataStoreName,
//    private val default: Double
//) {
//    operator fun provideDelegate(
//        thisRef: Any?,
//        prop: KProperty<*>
//    ): BaseSettingObject<Double, Double> {
//        val key = prop.name
//        return Settings.double(
//            key = key,
//            dataStoreName = dataStoreName,
//            default = default
//        )
//    }
//}
//
//class StringSetting(
//    private val dataStoreName: DataStoreName,
//    private val default: String
//) {
//    operator fun provideDelegate(
//        thisRef: Any?,
//        prop: KProperty<*>
//    ): BaseSettingObject<String, String> {
//        val key = prop.name
//        return Settings.string(
//            key = key,
//            dataStoreName = dataStoreName,
//            default = default
//        )
//    }
//}
//
//
//class StringSetSetting(
//    private val dataStoreName: DataStoreName,
//    private val default: Set<String>
//) {
//    operator fun provideDelegate(
//        thisRef: Any?,
//        prop: KProperty<*>
//    ): BaseSettingObject<Set<String>, Set<String>> {
//        val key = prop.name
//        return Settings.stringSet(
//            key = key,
//            dataStoreName = dataStoreName,
//            default = default
//        )
//    }
//}
//
//
//
//class StringSetSetting(
//    private val dataStoreName: DataStoreName,
//    private val default: Set<String>
//) {
//    operator fun provideDelegate(
//        thisRef: Any?,
//        prop: KProperty<*>
//    ): BaseSettingObject<Set<String>, Set<String>> {
//        val key = prop.name
//        return Settings.stringSet(
//            key = key,
//            dataStoreName = dataStoreName,
//            default = default
//        )
//    }
//}
