package org.elnix.dragonlauncher.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RowScope.Spacer() {
    Spacer(Modifier.weight(1f))
}

@Composable
fun ColumnScope.Spacer() {
    Spacer(Modifier.weight(1f))
}