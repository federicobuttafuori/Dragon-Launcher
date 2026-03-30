package org.elnix.dragonlauncher.settings.stores

import org.elnix.dragonlauncher.settings.DataStoreName
import org.elnix.dragonlauncher.settings.Settings
import org.elnix.dragonlauncher.settings.bases.BaseSettingObject
import org.elnix.dragonlauncher.settings.bases.JsonArraySettingsStore

object WidgetsSettingsStore : JsonArraySettingsStore() {

    override val name: String = "Widgets"
    override val dataStoreName: DataStoreName
        get() = DataStoreName.WIDGETS


    override val jsonSetting = Settings.string(
        key = "widgets",
        dataStoreName = dataStoreName,
        default = ""
    )

    override val ALL: List<BaseSettingObject<*,*>>
        get() = listOf(this.jsonSetting)
}
