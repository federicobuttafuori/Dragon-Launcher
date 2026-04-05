package org.elnix.dragonlauncher.enumsui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.ui.graphics.vector.ImageVector
import org.elnix.dragonlauncher.common.R

enum class SelectedUnselectedViewMode(
    override val resId: Int?,
    override val iconEnabled: ImageVector,
    override val iconDisabled: ImageVector? = null
) : ToggleButtonOption {
    Unselected(R.string.unselected, Icons.Default.CheckBoxOutlineBlank),
    Selected(R.string.selected_text, Icons.Default.CheckBox)
}
