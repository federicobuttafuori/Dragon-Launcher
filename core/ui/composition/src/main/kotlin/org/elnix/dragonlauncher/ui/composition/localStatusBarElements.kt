package org.elnix.dragonlauncher.ui.composition

import androidx.compose.runtime.compositionLocalOf
import org.elnix.dragonlauncher.common.serializables.StatusBarSerializable

val LocalStatusBarElements = compositionLocalOf<List<StatusBarSerializable>> {
    error("No status bar elements provided")
}