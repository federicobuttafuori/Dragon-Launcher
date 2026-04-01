package org.elnix.dragonlauncher.enumsui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.RecentActors
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import org.elnix.dragonlauncher.common.R

enum class DrawerToolbar(
    val resId: Int,
    val icon: ImageVector,
    val height: Int
) {
    Spacer(R.string.spacer, Icons.Default.Height, 0),
    RecentlyUsed(R.string.recently_used_apps, Icons.Default.RecentActors, 80),
    SearchBar(R.string.search_bar, Icons.Default.Search, 60)
}

