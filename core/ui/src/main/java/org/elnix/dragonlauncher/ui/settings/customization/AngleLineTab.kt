@file:Suppress("AssignedValueIsNeverRead")

package org.elnix.dragonlauncher.ui.settings.customization

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.elnix.dragonlauncher.common.R
import org.elnix.dragonlauncher.settings.stores.UiSettingsStore
import org.elnix.dragonlauncher.ui.MainScreenOverlay
import org.elnix.dragonlauncher.ui.components.dragon.DragonColumnGroup
import org.elnix.dragonlauncher.ui.components.settings.SettingsSwitchRow
import org.elnix.dragonlauncher.ui.components.settings.asState
import org.elnix.dragonlauncher.ui.helpers.settings.SettingsLazyHeader
import org.elnix.dragonlauncher.ui.modifiers.settingsGroup


@Composable
fun AngleLineTab(onBack: () -> Unit) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    val showAppAnglePreview by UiSettingsStore.showAnglePreview.asState()
    val showLinePreview by UiSettingsStore.showLinePreview.asState()

    var dummyStart by remember { mutableStateOf(Offset.Zero) }
    var dummyEnd by remember { mutableStateOf(Offset.Infinite) }
    var size by remember { mutableStateOf(IntSize.Zero) }


    SettingsLazyHeader(
        title = stringResource(R.string.angle_line),
        onBack = onBack,
        helpText = stringResource(R.string.angle_line_help),
        onReset = {
            scope.launch {
                UiSettingsStore.resetAll(ctx)
            }
        }, content = {
            SettingsSwitchRow(
                setting = UiSettingsStore.showLinePreview,
                title = stringResource(R.string.show_app_line_preview),
                description = stringResource(R.string.show_app_line_preview_description)
            )

            AnimatedVisibility(showLinePreview) {


                Column {
                    // Preview of the line
                    Box(
                        modifier = Modifier
                            .settingsGroup()
                            .height(200.dp)
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                val rect = coordinates.boundsInRoot()
                                dummyStart = Offset(
                                    rect.left + rect.width / 2f,
                                    rect.top + rect.height / 2f
                                )

                                dummyEnd = Offset(
                                    rect.left + rect.width / 2f,
                                    rect.top + rect.height / 2f
                                )
                            }
                            .onSizeChanged { size = it }
                    ) {
                        MainScreenOverlay(
                            start = dummyStart,
                            current = dummyEnd,
                            nestId = 0,
                            isDragging = true,
                            surface = size,
                            onLaunch = null
                        )
                    }


                    DragonColumnGroup {
                        SettingsSwitchRow(
                            setting = UiSettingsStore.rgbLoading,
                            title = stringResource(R.string.rgb_loading_settings),
                            description = stringResource(R.string.rgb_loading_description)
                        )

                        SettingsSwitchRow(
                            setting = UiSettingsStore.rgbLine,
                            title = stringResource(R.string.rgb_line_selector),
                            description = stringResource(R.string.rgb_line_selector_description)
                        )

                        SettingsSwitchRow(
                            setting = UiSettingsStore.showAppLaunchingPreview,
                            title = stringResource(R.string.show_app_launch_preview),
                            description = stringResource(R.string.show_app_launch_preview_description)
                        )

                        SettingsSwitchRow(
                            setting = UiSettingsStore.showCirclePreview,
                            title = stringResource(R.string.show_app_circle_preview),
                            description = stringResource(R.string.show_app_circle_preview_description)
                        )


                        SettingsSwitchRow(
                            setting = UiSettingsStore.showAnglePreview,
                            title = stringResource(
                                R.string.show_app_angle_preview,
                                if (!showAppAnglePreview) stringResource(R.string.do_you_hate_it) else ""
                            ),
                            description = stringResource(R.string.show_app_angle_preview_description)
                        )

                        SettingsSwitchRow(
                            setting = UiSettingsStore.showAppPreviewIconCenterStartPosition,
                            title = stringResource(R.string.show_app_icon_start_drag_position),
                            description = stringResource(R.string.show_app_icon_start_drag_position_description)
                        )

                        SettingsSwitchRow(
                            setting = UiSettingsStore.linePreviewSnapToAction,
                            title = stringResource(R.string.line_preview_snap_to_action),
                            description = stringResource(R.string.line_preview_snap_to_action_description)
                        )

                        SettingsSwitchRow(
                            setting = UiSettingsStore.showAllActionsOnCurrentCircle,
                            title = stringResource(R.string.show_all_actions_on_current_circle),
                            description = stringResource(R.string.show_all_actions_on_current_circle_description)
                        )
                    }
                }
            }
        }
    )
}
