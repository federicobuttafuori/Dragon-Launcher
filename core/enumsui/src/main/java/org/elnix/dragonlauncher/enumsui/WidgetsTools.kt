package org.elnix.dragonlauncher.enumsui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.FormatClear
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.GridOff
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.MoveDown
import androidx.compose.material.icons.filled.MoveUp
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Restore
import androidx.compose.ui.graphics.vector.ImageVector
import org.elnix.dragonlauncher.common.R

enum class WidgetsToolsAddRemove(
    override val resId: Int?,
    override val iconEnabled: ImageVector,
    override val iconDisabled: ImageVector?
) : ToggleButtonOption {
    Add(R.string.add_widget, Icons.Default.Add, null),
    Remove(R.string.delete_widget, Icons.Default.RemoveCircle, null)
}


enum class WidgetsToolsCenterReset(
    override val resId: Int?,
    override val iconEnabled: ImageVector,
    override val iconDisabled: ImageVector?
) : ToggleButtonOption {
    Center(R.string.center_selected_widget, Icons.Default.CenterFocusStrong, null),
    Reset(R.string.reset_widget, Icons.Default.Restore, null)
}

enum class WidgetsToolsUpDown(
    override val resId: Int?,
    override val iconEnabled: ImageVector,
    override val iconDisabled: ImageVector?
) : ToggleButtonOption {
    Up(R.string.select_previous_widget, Icons.Default.ArrowUpward, null),
    Down(R.string.select_next_widget, Icons.Default.ArrowDownward, null)
}



enum class WidgetsToolsMoveUpDown(
    override val resId: Int?,
    override val iconEnabled: ImageVector,
    override val iconDisabled: ImageVector?
) : ToggleButtonOption {
    MoveUp(R.string.move_selected_widget_up, Icons.Default.MoveUp, null),
    MoveDown(R.string.move_selected_widget_down, Icons.Default.MoveDown, null)
}

enum class WidgetsToolsGridScaleNest(
    override val resId: Int?,
    override val iconEnabled: ImageVector,
    override val iconDisabled: ImageVector?
) : ToggleButtonOption {
    Grid(R.string.enable_grid, Icons.Default.GridOn, Icons.Default.GridOff),
    Scale(R.string.enable_scale_snap, Icons.Default.FormatSize, Icons.Default.FormatClear),
    Nests(R.string.pick_a_nest, Icons.Default.AccountCircle, null)
}