package org.elnix.dragonlauncher.enumsui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.GridOff
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.ui.graphics.vector.ImageVector
import org.elnix.dragonlauncher.common.R

enum class PointsEditTools(
    override val resId: Int,
    override val iconEnabled: ImageVector,
    override val iconDisabled: ImageVector
) : ToggleButtonOption {
    SnapPoints(R.string.snap_points, Icons.Default.GridOn, Icons.Default.GridOff),
    AutoSeparate(R.string.auto_separate, Icons.Default.AutoMode, Icons.Default.LinearScale),
    FreeMove(R.string.free_move_dragged_point, Icons.Default.Link, Icons.Default.LinkOff)
}