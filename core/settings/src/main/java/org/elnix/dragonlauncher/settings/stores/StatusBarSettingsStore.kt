package org.elnix.dragonlauncher.settings.stores

import androidx.compose.ui.graphics.Color
import org.elnix.dragonlauncher.settings.DataStoreName
import org.elnix.dragonlauncher.settings.Settings
import org.elnix.dragonlauncher.settings.bases.BaseSettingObject
import org.elnix.dragonlauncher.settings.bases.MapSettingsStore

enum class DateFormat(val pattern: String, val displayName: String) {
    SHORT("MMM dd", "Short (Dec 25)"),
    MEDIUM("MMM dd, yyyy", "Medium (Dec 25, 2023)"),
    LONG("EEEE, MMMM dd, yyyy", "Long (Monday, December 25, 2023)"),
    ISO("yyyy-MM-dd", "ISO (2023-12-25)"),
    US("MM/dd/yyyy", "US (12/25/2023)"),
    EUROPEAN("dd/MM/yyyy", "European (25/12/2023)"),
    CUSTOM("", "Custom")
}

enum class TimeFormat(val pattern: String, val displayName: String) {
    H12("hh:mm a", "12-hour (02:30 PM)"),
    H24("HH:mm", "24-hour (14:30)"),
    H12_SECONDS("hh:mm:ss a", "12-hour with seconds (02:30:45 PM)"),
    H24_SECONDS("HH:mm:ss", "24-hour with seconds (14:30:45)"),
    H12_SHORT("h:mm a", "12-hour short (2:30 PM)"),
    H24_SHORT("H:mm", "24-hour short (14:30)"),
    CUSTOM("", "Custom")
}

object StatusBarSettingsStore : MapSettingsStore() {

    override val name: String = "Status Bar"
    override val dataStoreName: DataStoreName = DataStoreName.STATUS_BAR

    val showStatusBar = Settings.boolean(
        key = "showStatusBar",
        dataStoreName = dataStoreName,
        default = false
    )

    val barBackgroundColor = Settings.color(
        key = "barBackgroundColor",
        dataStoreName = dataStoreName,
        default = Color.Transparent
    )

    val barTextColor = Settings.color(
        key = "barTextColor",
        dataStoreName = dataStoreName,
        default = Color.White
    )

    val leftPadding = Settings.int(
        key = "leftPadding",
        dataStoreName = dataStoreName,
        default = 5,
        allowedRange = 0..300
    )

    val rightPadding = Settings.int(
        key = "rightPadding",
        dataStoreName = dataStoreName,
        default = 5,
        allowedRange = 0..300
    )

    val topPadding = Settings.int(
        key = "topPadding",
        dataStoreName = dataStoreName,
        default = 2,
        allowedRange = 0..300
    )

    val bottomPadding = Settings.int(
        key = "bottomPadding",
        dataStoreName = dataStoreName,
        default = 2,
        allowedRange = 0..300
    )

    val connectivityUpdateFrequency = Settings.int(
        key = "connectivityUpdateFrequency",
        dataStoreName = dataStoreName,
        default = 5,
        allowedRange = 1..60
    )

    val showAirplaneMode = Settings.boolean(
        key = "showAirplaneMode",
        dataStoreName = dataStoreName,
        default = true
    )

    val showWifi = Settings.boolean(
        key = "showWifi",
        dataStoreName = dataStoreName,
        default = true
    )

    val showBluetooth = Settings.boolean(
        key = "showBluetooth",
        dataStoreName = dataStoreName,
        default = true
    )

    val showVpn = Settings.boolean(
        key = "showVpn",
        dataStoreName = dataStoreName,
        default = true
    )

    val showMobileData = Settings.boolean(
        key = "showMobileData",
        dataStoreName = dataStoreName,
        default = true
    )

    val showHotspot = Settings.boolean(
        key = "showHotspot",
        dataStoreName = dataStoreName,
        default = true
    )

    val dateFormat = Settings.enum(
        key = "dateFormat",
        dataStoreName = dataStoreName,
        default = DateFormat.SHORT,
        enumClass = DateFormat::class.java
    )

    val timeFormat = Settings.enum(
        key = "timeFormat",
        dataStoreName = dataStoreName,
        default = TimeFormat.H24,
        enumClass = TimeFormat::class.java
    )

    val customDateFormat = Settings.string(
        key = "customDateFormat",
        dataStoreName = dataStoreName,
        default = "MMM dd"
    )

    val customTimeFormat = Settings.string(
        key = "customTimeFormat",
        dataStoreName = dataStoreName,
        default = "HH:mm"
    )

    val mergeBandwidth = Settings.boolean(
        key = "mergeBandwidth",
        dataStoreName = dataStoreName,
        default = false
    )


    override val ALL: List<BaseSettingObject<*,*>> = listOf(
        showStatusBar,
        barBackgroundColor,
        barTextColor,
        leftPadding,
        rightPadding,
        topPadding,
        bottomPadding,
        connectivityUpdateFrequency,
        showAirplaneMode,
        showWifi,
        showBluetooth,
        showVpn,
        showMobileData,
        showHotspot,
        dateFormat,
        timeFormat,
        customDateFormat,
        customTimeFormat,
        mergeBandwidth
    )
}
