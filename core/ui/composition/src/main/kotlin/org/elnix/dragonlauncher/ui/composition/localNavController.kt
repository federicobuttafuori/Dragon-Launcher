package org.elnix.dragonlauncher.ui.composition

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    error("No LocalNavController provided")
}