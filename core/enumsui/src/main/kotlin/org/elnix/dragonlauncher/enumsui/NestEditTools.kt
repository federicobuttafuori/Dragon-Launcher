package org.elnix.dragonlauncher.enumsui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.ui.graphics.vector.ImageVector
import org.elnix.dragonlauncher.common.R

enum class NestEditTools(
    override val resId: Int?,
    override val iconEnabled: ImageVector,
    override val iconDisabled: ImageVector?
) : ToggleButtonOption {
    NestManagement(R.string.edit_nests, Icons.Default.AccountCircle, null),
    GoParentNest(R.string.go_parent_nest, Icons.Default.Fullscreen, null),
    EnterNest(R.string.open_nest_circle, Icons.Default.FullscreenExit, null)
}
