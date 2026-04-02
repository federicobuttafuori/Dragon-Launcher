package org.elnix.dragonlauncher.enumsui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.elnix.dragonlauncher.common.R
import org.elnix.dragonlauncher.enumsui.DefaultThemes.AMOLED
import org.elnix.dragonlauncher.enumsui.DefaultThemes.CUSTOM
import org.elnix.dragonlauncher.enumsui.DefaultThemes.DARK
import org.elnix.dragonlauncher.enumsui.DefaultThemes.LIGHT
import org.elnix.dragonlauncher.enumsui.DefaultThemes.SYSTEM

enum class DefaultThemes { LIGHT, DARK, AMOLED, SYSTEM, CUSTOM }

@Composable
fun defaultThemeName(mode: DefaultThemes) = when (mode) {
    LIGHT -> stringResource(R.string.light_theme)
    DARK -> stringResource(R.string.dark_theme)
    AMOLED -> stringResource(R.string.amoled_theme)
    SYSTEM -> stringResource(R.string.system_theme)
    CUSTOM -> stringResource(R.string.custom_theme)
}


enum class ColorPickerButtonAction { RANDOM, RESET, COPY, PASTE }

@Composable
fun colorPickerButtonIcon(mode: ColorPickerButtonAction) = when (mode) {
    ColorPickerButtonAction.RANDOM -> Icons.Default.Shuffle
    ColorPickerButtonAction.RESET ->  Icons.Default.Restore
    ColorPickerButtonAction.COPY -> Icons.Default.ContentCopy
    ColorPickerButtonAction.PASTE ->  Icons.Default.ContentPaste
}


