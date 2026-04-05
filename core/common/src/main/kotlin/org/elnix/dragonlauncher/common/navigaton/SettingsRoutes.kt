package org.elnix.dragonlauncher.common.navigaton

import org.elnix.dragonlauncher.common.R

val settingsRoutes = listOf(
    SETTINGS.ROOT,
    SETTINGS.ADVANCED_ROOT,
    SETTINGS.APPEARANCE,
    SETTINGS.WALLPAPER,
    SETTINGS.ICON_PACK,
    SETTINGS.STATUS_BAR,
    SETTINGS.THEME,
    SETTINGS.WIDGETS_FLOATING_APPS,
    SETTINGS.COLORS,
    SETTINGS.BEHAVIOR,
    SETTINGS.DRAWER,
    SETTINGS.WORKSPACE,
    SETTINGS.BACKUP,
    SETTINGS.WELLBEING,
    SETTINGS.DEBUG,
    SETTINGS.LOGS,
    SETTINGS.SETTINGS_JSON,
    SETTINGS.LANGUAGE,
    SETTINGS.CHANGELOGS,
    SETTINGS.EXTENSIONS,
    SETTINGS.ANGLE_LINE_EDIT,
    SETTINGS.HOLD_TO_ACTIVATE_ARC
)

fun routeResId(route: String): Int {

    return when (route) {
        SETTINGS.EXTENSIONS ->R.string.extensions
        SETTINGS.ROOT ->R.string.points_settings
        SETTINGS.ADVANCED_ROOT ->R.string.settings
        SETTINGS.APPEARANCE ->R.string.appearance
        SETTINGS.WALLPAPER ->R.string.wallpaper
        SETTINGS.ICON_PACK ->R.string.icon_pack
        SETTINGS.STATUS_BAR ->R.string.status_bar
        SETTINGS.THEME ->R.string.theme_selector
        SETTINGS.WIDGETS_FLOATING_APPS ->R.string.widgets_floating_apps
        SETTINGS.COLORS ->R.string.color_selector
        SETTINGS.BEHAVIOR ->R.string.behavior
        SETTINGS.DRAWER ->R.string.app_drawer
        SETTINGS.WORKSPACE ->R.string.workspaces
        SETTINGS.BACKUP ->R.string.backup_restore
        SETTINGS.WELLBEING ->R.string.wellbeing
        SETTINGS.DEBUG ->R.string.debug
        SETTINGS.LOGS ->R.string.logs
        SETTINGS.SETTINGS_JSON ->R.string.settings_json
        SETTINGS.LANGUAGE ->R.string.settings_language_title
        SETTINGS.CHANGELOGS ->R.string.changelogs
        SETTINGS.ANGLE_LINE_EDIT -> R.string.angle_line
        SETTINGS.HOLD_TO_ACTIVATE_ARC -> R.string.hold_settings
        else -> -1
    }
}