package org.elnix.dragonlauncher.enumsui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.ui.graphics.vector.ImageVector
import org.elnix.dragonlauncher.common.R

enum class WorkspaceViewMode(
    override val resId: Int?,
    override val iconEnabled: ImageVector,
    override val iconDisabled: ImageVector? = null
) : ToggleButtonOption {
        DEFAULTS(R.string.workspace_defaults, Icons.Default.Apps),
    ADDED(R.string.workspace_added, Icons.Default.AddBox),
    REMOVED(R.string.workspace_removed, Icons.Default.RemoveCircle)
}
