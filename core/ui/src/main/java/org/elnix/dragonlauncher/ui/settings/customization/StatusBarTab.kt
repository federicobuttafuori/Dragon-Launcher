package org.elnix.dragonlauncher.ui.settings.customization

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.elnix.dragonlauncher.common.R
import org.elnix.dragonlauncher.settings.stores.StatusBarJsonSettingsStore
import org.elnix.dragonlauncher.settings.stores.DateFormat
import org.elnix.dragonlauncher.settings.stores.StatusBarSettingsStore
import org.elnix.dragonlauncher.settings.stores.TimeFormat
import org.elnix.dragonlauncher.ui.components.generic.ActionSelectorRow
import org.elnix.dragonlauncher.ui.components.settings.SettingsColorPicker
import org.elnix.dragonlauncher.ui.components.settings.SettingsSlider
import org.elnix.dragonlauncher.ui.components.settings.SettingsSwitchRow
import org.elnix.dragonlauncher.ui.helpers.settings.SettingsLazyHeader
import org.elnix.dragonlauncher.ui.remembers.LocalShowStatusBar
import org.elnix.dragonlauncher.ui.remembers.rememberExpandableSection
import org.elnix.dragonlauncher.ui.statusbar.EditStatusBar
import org.elnix.dragonlauncher.ui.statusbar.StatusBar

@Composable
fun StatusBarTab(
    onBack: () -> Unit
) {
    val ctx = LocalContext.current
    val showStatusBar = LocalShowStatusBar.current
    val scope = rememberCoroutineScope()

    val systemInsets = WindowInsets.systemBars.asPaddingValues()
    val isRealFullscreen = systemInsets.calculateTopPadding() == 0.dp

    val paddingsSectionState = rememberExpandableSection(stringResource(R.string.padding))

    Column {
        AnimatedVisibility(showStatusBar && isRealFullscreen) {
            StatusBar(null)
        }

        SettingsLazyHeader(
            title = stringResource(R.string.status_bar),
            onBack = onBack,
            helpText = stringResource(R.string.status_bar_tab_text),
            onReset = {
                scope.launch {
                    StatusBarSettingsStore.resetAll(ctx)
                    StatusBarJsonSettingsStore.resetAll(ctx)
                }
            },
            modifier = Modifier.verticalScroll(rememberScrollState()),
            content = {
                SettingsSwitchRow(
                    setting = StatusBarSettingsStore.showStatusBar,
                    title = stringResource(R.string.show_status_bar),
                    description = stringResource(R.string.show_status_bar_desc),
                ) {
                    scope.launch {
                        StatusBarSettingsStore.showStatusBar.set(ctx, it)
                    }
                }


                AnimatedVisibility(showStatusBar) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {

                        SettingsColorPicker(
                            settingObject = StatusBarSettingsStore.barBackgroundColor,
                            label = stringResource(R.string.status_bar_background),
                            defaultColor = Color.Transparent
                        )

                        SettingsColorPicker(
                            settingObject = StatusBarSettingsStore.barTextColor,
                            label = stringResource(R.string.status_bar_text_color),
                            defaultColor = MaterialTheme.colorScheme.primary
                        )

                        EditStatusBar()

                        HorizontalDivider()

                        ExpandableSection(paddingsSectionState) {
                            SettingsSlider(
                                setting = StatusBarSettingsStore.leftPadding,
                                title = stringResource(R.string.left_padding),
                                valueRange = 0..200,
                            )

                            SettingsSlider(
                                setting = StatusBarSettingsStore.rightPadding,
                                title = stringResource(R.string.right_padding),
                                valueRange = 0..200,
                            )
                            SettingsSlider(
                                setting = StatusBarSettingsStore.topPadding,
                                title = stringResource(R.string.top_padding),
                                valueRange = 0..200,
                            )
                            SettingsSlider(
                                setting = StatusBarSettingsStore.bottomPadding,
                                title = stringResource(R.string.bottom_padding),
                                valueRange = 0..200,
                            )

                            SettingsSlider(
                                setting = StatusBarSettingsStore.connectivityUpdateFrequency,
                                title = stringResource(R.string.connectivity_update_frequency),
                                valueRange = 1..60,
                            )

                            Text(
                                text = stringResource(R.string.connectivity_indicators),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                            )

                            SettingsSwitch(
                                setting = StatusBarSettingsStore.showAirplaneMode,
                                title = stringResource(R.string.show_airplane_mode),
                            )

                            SettingsSwitch(
                                setting = StatusBarSettingsStore.showWifi,
                                title = stringResource(R.string.show_wifi),
                            )

                            SettingsSwitch(
                                setting = StatusBarSettingsStore.showBluetooth,
                                title = stringResource(R.string.show_bluetooth),
                            )

                            SettingsSwitch(
                                setting = StatusBarSettingsStore.showVpn,
                                title = stringResource(R.string.show_vpn),
                            )

                            SettingsSwitch(
                                setting = StatusBarSettingsStore.showMobileData,
                                title = stringResource(R.string.show_mobile_data),
                            )

                            SettingsSwitch(
                                setting = StatusBarSettingsStore.showHotspot,
                                title = stringResource(R.string.show_hotspot),
                            )

                            Text(
                                text = stringResource(R.string.date_time_formats),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                            )

                            // Date Format Selector
                            ActionSelectorRow(
                                options = DateFormat.entries,
                                selected = dateFormat,
                                label = "Date Format",
                                optionLabel = { it.displayName },
                                onSelected = { selectedFormat ->
                                    selectedFormat?.let { format ->
                                        scope.launch {
                                            StatusBarSettingsStore.dateFormat.set(ctx, format)
                                        }
                                    }
                                }
                            )

                            // Time Format Selector  
                            ActionSelectorRow(
                                options = TimeFormat.entries,
                                selected = timeFormat,
                                label = "Time Format",
                                optionLabel = { it.displayName },
                                onSelected = { selectedFormat ->
                                    selectedFormat?.let { format ->
                                        scope.launch {
                                            StatusBarSettingsStore.timeFormat.set(ctx, format)
                                        }
                                    }
                                }
                            )

                            // Custom formats when selected
                            val dateFormat by StatusBarSettingsStore.dateFormat.asState()
                            val timeFormat by StatusBarSettingsStore.timeFormat.asState()
                            val customDateFormat by StatusBarSettingsStore.customDateFormat.asState()
                            val customTimeFormat by StatusBarSettingsStore.customTimeFormat.asState()

                            if (dateFormat == DateFormat.CUSTOM) {
                                var dateFormatText by remember(customDateFormat) { mutableStateOf(customDateFormat) }
                                OutlinedTextField(
                                    value = dateFormatText,
                                    onValueChange = { dateFormatText = it },
                                    label = { Text(stringResource(R.string.custom_date_format)) },
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    singleLine = true
                                )
                                LaunchedEffect(dateFormatText) {
                                    if (dateFormatText != customDateFormat) {
                                        StatusBarSettingsStore.customDateFormat.set(ctx, dateFormatText)
                                    }
                                }
                            }

                            if (timeFormat == TimeFormat.CUSTOM) {
                                var timeFormatText by remember(customTimeFormat) { mutableStateOf(customTimeFormat) }
                                OutlinedTextField(
                                    value = timeFormatText,
                                    onValueChange = { timeFormatText = it },
                                    label = { Text(stringResource(R.string.custom_time_format)) },
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    singleLine = true
                                )
                                LaunchedEffect(timeFormatText) {
                                    if (timeFormatText != customTimeFormat) {
                                        StatusBarSettingsStore.customTimeFormat.set(ctx, timeFormatText)
                                    }
                                }
                            }

                            Text(
                                text = stringResource(R.string.bandwidth),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                            )

                            SettingsSwitch(
                                setting = StatusBarSettingsStore.mergeBandwidth,
                                title = stringResource(R.string.merge_bandwidth),
                            )
                        }
                    }
                }
            }
        )
    }
}
