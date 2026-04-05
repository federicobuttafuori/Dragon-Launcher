package org.elnix.dragonlauncher.common.navigaton


// NOT USED FROM NOW
// i STARTED TO IMPLEMENT THIS BUT CHANGE MY MIND AS BE WAS BECOMING OVERCOMPLICATED,
// I KEEP IT HERE JUST IN CASE, MAYBE ONE DAY I'LL REWORK ON IT



//sealed class NavigationRoute(
//    open val path: String,
//    open val parent: NavigationRoute?,
//    @StringRes open val labelResId: Int,
//    open val hasId: Boolean = false
//) {
//    // ────────────────── MAIN ROUTES ──────────────────
//    object Main : NavigationRoute("main", null, R.string.main_screen)
//    object Drawer : NavigationRoute("drawer", null, R.string.app_drawer)
//    object Welcome : NavigationRoute("welcome", null, R.string.welcome_screen)
//
//    // ────────────────── SETTINGS HIERARCHY ──────────────────
//    sealed class Settings(
//        path: String,
//        parent: NavigationRoute?,
//        @StringRes labelResId: Int,
//        hasId: Boolean = false
//    ) : NavigationRoute(
//        path = buildPath("settings", path),
//        parent = parent,
//        labelResId = labelResId,
//        hasId = hasId
//    ) {
//
//        object Root : Settings("", null, R.string.points_settings)
//
//        sealed class Advanced(
//            path: String,
//            parent: NavigationRoute? = Settings.Root,
//            @StringRes labelResId: Int,
//            hasId: Boolean = false
//        ) : Settings(
//            path = buildPath("advanced", path),
//            parent = parent,
//            labelResId = labelResId,
//            hasId = hasId
//        ) {
//
//            object Root : Advanced("", Settings.Root, R.string.settings)
//
//            // Appearance sub-hierarchy
//            sealed class Appearance(
//                path: String,
//                parent: NavigationRoute? = Advanced.Root,
//                @StringRes labelResId: Int,
//                hasId: Boolean = false
//            ) : Advanced(
//                path = buildPath("appearance", path),
//                parent = parent,
//                labelResId = labelResId,
//                hasId = hasId
//            ) {
//
//                object Root : Appearance("", Advanced.Root, R.string.appearance)
//                object Wallpaper : Appearance("wallpaper", Root, R.string.wallpaper)
//                object IconPack : Appearance("icon_pack", Root, R.string.icon_pack)
//                object StatusBar : Appearance("status_bar", Root, R.string.status_bar)
//                object Fonts : Appearance("fonts", Root, R.string.font_selector)
//                object Colors : Appearance("colors", Root, R.string.color_selector)
//            }
//
//            // Other advanced settings (no nesting needed)
//            object Behavior : Advanced("behavior", Root, R.string.behavior)
//            object Drawer : Advanced("drawer", Root, R.string.app_drawer)
//            object Workspace : Advanced("workspace", Root, R.string.workspaces)
//            object WorkspaceDetail : Advanced("workspace/{id}", Root, R.string.workspaces, hasId = true)
//            object Permissions : Advanced("permissions", Root, R.string.permissions)
//            object WidgetsFloatingApps : Advanced("widgets_floating_apps/{id}", Root, R.string.widgets_floating_apps, hasId = true)
//            object Backup : Advanced("backup", Root, R.string.backup_restore)
//            object Wellbeing : Advanced("wellbeing", Root, R.string.wellbeing)
//
//            // Debug sub-hierarchy
//            sealed class Debug(
//                path: String,
//                parent: NavigationRoute? = Advanced.Root,
//                @StringRes labelResId: Int,
//                hasId: Boolean = false
//            ) : Advanced(
//                path = buildPath("debug", path),
//                parent = parent,
//                labelResId = labelResId,
//                hasId = hasId
//            ) {
//
//                object Root : Debug("", Advanced.Root, R.string.debug)
//                object Logs : Debug("logs", Root, R.string.logs)
//                object SettingsJson : Debug("settings_json", Root, R.string.settings_json)
//            }
//
//            // Other screens
//            object Language : Advanced("language", Root, R.string.settings_language_title)
//            object Changelogs : Advanced("changelogs", Root, R.string.changelogs)
//            object Extensions : Advanced("extensions", Root, R.string.extensions)
//            object AngleLineEdit : Advanced("angleLineEdit", Root, R.string.angle_line)
//            object HoldToActivateArc : Advanced("hold_to_activate", Root, R.string.hold_settings)
//            object MainScreenLayers : Advanced("main_screen_layers", Root, R.string.main_screen_layers)
//            object NestEdit : Advanced("nest/{id}", Root, R.string.edit_nest, hasId = true)
//        }
//    }
//
//    companion object {
//        /**
//         * Helper function to build paths without manual concatenation.
//         * Handles empty fragments gracefully.
//         */
//        fun buildPath(prefix: String, fragment: String): String {
//            return if (fragment.isEmpty()) prefix else "$prefix/$fragment"
//        }
//
//        /**
//         * Find a route by its path string.
//         * Useful for deserialization or storage.
//         */
//        fun fromPath(path: String?): NavigationRoute? {
//            return when (path) {
//                "main" -> Main
//                "drawer" -> Drawer
//                "welcome" -> Welcome
//                "settings" -> Settings.Root
//                "settings/advanced" -> Settings.Advanced.Root
//                "settings/advanced/appearance" -> Settings.Advanced.Appearance.Root
//                "settings/advanced/appearance/wallpaper" -> Settings.Advanced.Appearance.Wallpaper
//                "settings/advanced/appearance/icon_pack" -> Settings.Advanced.Appearance.IconPack
//                "settings/advanced/appearance/status_bar" -> Settings.Advanced.Appearance.StatusBar
//                "settings/advanced/appearance/fonts" -> Settings.Advanced.Appearance.Fonts
//                "settings/advanced/appearance/colors" -> Settings.Advanced.Appearance.Colors
//                "settings/advanced/behavior" -> Settings.Advanced.Behavior
//                "settings/advanced/drawer" -> Settings.Advanced.Drawer
//                "settings/advanced/workspace" -> Settings.Advanced.Workspace
//                "settings/advanced/workspace/{id}" -> Settings.Advanced.WorkspaceDetail
//                "settings/advanced/permissions" -> Settings.Advanced.Permissions
//                "settings/advanced/widgets_floating_apps/{id}" -> Settings.Advanced.WidgetsFloatingApps
//                "settings/advanced/backup" -> Settings.Advanced.Backup
//                "settings/advanced/wellbeing" -> Settings.Advanced.Wellbeing
//                "settings/advanced/debug" -> Settings.Advanced.Debug.Root
//                "settings/advanced/debug/logs" -> Settings.Advanced.Debug.Logs
//                "settings/advanced/debug/settings_json" -> Settings.Advanced.Debug.SettingsJson
//                "settings/advanced/language" -> Settings.Advanced.Language
//                "settings/advanced/changelogs" -> Settings.Advanced.Changelogs
//                "settings/advanced/extensions" -> Settings.Advanced.Extensions
//                "settings/advanced/angleLineEdit" -> Settings.Advanced.AngleLineEdit
//                "settings/advanced/hold_to_activate" -> Settings.Advanced.HoldToActivateArc
//                "settings/advanced/main_screen_layers" -> Settings.Advanced.MainScreenLayers
//                "settings/advanced/nest/{id}" -> Settings.Advanced.NestEdit
//                else -> null
//            }
//        }
//
//        /**
//         * Get all available routes (for debugging, logging, etc.)
//         */
//        fun allRoutes(): List<NavigationRoute> = listOf(
//            Main, Drawer, Welcome,
//            Settings.Root,
//            Settings.Advanced.Root,
//            Settings.Advanced.Appearance.Root,
//            Settings.Advanced.Appearance.Wallpaper,
//            Settings.Advanced.Appearance.IconPack,
//            Settings.Advanced.Appearance.StatusBar,
//            Settings.Advanced.Appearance.Fonts,
//            Settings.Advanced.Appearance.Colors,
//            Settings.Advanced.Behavior,
//            Settings.Advanced.Drawer,
//            Settings.Advanced.Workspace,
//            Settings.Advanced.WorkspaceDetail,
//            Settings.Advanced.Permissions,
//            Settings.Advanced.WidgetsFloatingApps,
//            Settings.Advanced.Backup,
//            Settings.Advanced.Wellbeing,
//            Settings.Advanced.Debug.Root,
//            Settings.Advanced.Debug.Logs,
//            Settings.Advanced.Debug.SettingsJson,
//            Settings.Advanced.Language,
//            Settings.Advanced.Changelogs,
//            Settings.Advanced.Extensions,
//            Settings.Advanced.AngleLineEdit,
//            Settings.Advanced.HoldToActivateArc,
//            Settings.Advanced.MainScreenLayers,
//            Settings.Advanced.NestEdit
//        )
//    }
//}
//
//// ────────────────── CONVENIENCE EXTENSIONS ──────────────────
//
///**
// * Type-safe navigation helpers
// */
//fun NavigationRoute.Settings.Advanced.WorkspaceDetail.createRoute(id: String): String =
//    this.path.replace("{id}", id)
//
//fun NavigationRoute.Settings.Advanced.WidgetsFloatingApps.createRoute(id: String): String =
//    this.path.replace("{id}", id)
//
//fun NavigationRoute.Settings.Advanced.NestEdit.createRoute(id: String): String =
//    this.path.replace("{id}", id)
//
///**
// * Extract ID from parametrized routes
// */
//fun extractIdFromRoute(route: String): String? {
//    val parts = route.split("/")
//    return parts.lastOrNull()?.takeIf { it.isNotEmpty() && it != "{id}" }
//}

//// ────────────────── LEGACY ALIASES (for migration) ──────────────────
//object ROUTES {
//    val MAIN = NavigationRoute.Main.path
//    val DRAWER = NavigationRoute.Drawer.path
//    val WELCOME = NavigationRoute.Welcome.path
//}
//
//object SETTINGS {
//    val ROOT = NavigationRoute.Settings.Root.path
//    val ADVANCED_ROOT = NavigationRoute.Settings.Advanced.Root.path
//    val APPEARANCE = NavigationRoute.Settings.Advanced.Appearance.Root.path
//    val WALLPAPER = NavigationRoute.Settings.Advanced.Appearance.Wallpaper.path
//    val ICON_PACK = NavigationRoute.Settings.Advanced.Appearance.IconPack.path
//    val STATUS_BAR = NavigationRoute.Settings.Advanced.Appearance.StatusBar.path
//    val FONTS = NavigationRoute.Settings.Advanced.Appearance.Fonts.path
//    val PERMISSIONS = NavigationRoute.Settings.Advanced.Permissions.path
//    val WIDGETS_FLOATING_APPS = NavigationRoute.Settings.Advanced.WidgetsFloatingApps.path
//    val BEHAVIOR = NavigationRoute.Settings.Advanced.Behavior.path
//    val COLORS = NavigationRoute.Settings.Advanced.Appearance.Colors.path
//    val DRAWER = NavigationRoute.Settings.Advanced.Drawer.path
//    val WORKSPACE = NavigationRoute.Settings.Advanced.Workspace.path
//    val WORKSPACE_DETAIL = NavigationRoute.Settings.Advanced.WorkspaceDetail.path
//    val BACKUP = NavigationRoute.Settings.Advanced.Backup.path
//    val WELLBEING = NavigationRoute.Settings.Advanced.Wellbeing.path
//    val DEBUG = NavigationRoute.Settings.Advanced.Debug.Root.path
//    val LOGS = NavigationRoute.Settings.Advanced.Debug.Logs.path
//    val SETTINGS_JSON = NavigationRoute.Settings.Advanced.Debug.SettingsJson.path
//    val LANGUAGE = NavigationRoute.Settings.Advanced.Language.path
//    val CHANGELOGS = NavigationRoute.Settings.Advanced.Changelogs.path
//    val EXTENSIONS = NavigationRoute.Settings.Advanced.Extensions.path
//    val NESTS_EDIT = NavigationRoute.Settings.Advanced.NestEdit.path
//    val ANGLE_LINE_EDIT = NavigationRoute.Settings.Advanced.AngleLineEdit.path
//    val HOLD_TO_ACTIVATE_ARC = NavigationRoute.Settings.Advanced.HoldToActivateArc.path
//    val MAINS_SCREEN_LAYERS = NavigationRoute.Settings.Advanced.MainScreenLayers.path
//}