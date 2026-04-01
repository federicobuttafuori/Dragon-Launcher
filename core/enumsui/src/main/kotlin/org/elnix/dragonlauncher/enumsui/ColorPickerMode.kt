package org.elnix.dragonlauncher.enumsui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.ui.graphics.vector.ImageVector
import org.elnix.dragonlauncher.common.R

enum class ColorPickerMode(
    override val resId: Int?,
    override val iconEnabled: ImageVector,
    override val iconDisabled: ImageVector? = null
) : ToggleButtonOption {
    DEFAULTS(R.string.default_text, Icons.Default.ColorLens),
    SLIDERS(R.string.sliders, Icons.Default.LinearScale),
    GRADIENT(R.string.gradient, Icons.Default.Colorize)
}
