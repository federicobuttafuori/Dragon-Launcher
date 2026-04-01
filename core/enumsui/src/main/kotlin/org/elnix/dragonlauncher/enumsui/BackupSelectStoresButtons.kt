package org.elnix.dragonlauncher.enumsui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.ui.graphics.vector.ImageVector
import org.elnix.dragonlauncher.common.R

enum class BackupSelectStoresButtons(
    override val resId: Int?,
    override val iconEnabled: ImageVector,
    override val iconDisabled: ImageVector? = null
) : ToggleButtonOption {
    DESELECT_ALL(R.string.deselect_all, Icons.Default.Deselect),
    SELECT_ALL(R.string.select_all, Icons.Default.SelectAll),
    INVERT(R.string.invert, Icons.Default.ChangeCircle)
}
