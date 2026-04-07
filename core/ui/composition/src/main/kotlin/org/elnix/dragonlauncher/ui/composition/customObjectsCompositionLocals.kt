package org.elnix.dragonlauncher.ui.composition

import androidx.compose.runtime.compositionLocalOf
import org.elnix.dragonlauncher.common.serializables.CustomObjectSerializable

val LocalLineObject = compositionLocalOf<CustomObjectSerializable> {
    error("No LocalLine provided")
}

val LocalAngleLineObject = compositionLocalOf<CustomObjectSerializable> {
    error("No LocalAngleLine provided")
}

val LocalStartLineObject = compositionLocalOf<CustomObjectSerializable> {
    error("No LocalStartLine provided")
}

val LocalEndLineObject = compositionLocalOf<CustomObjectSerializable> {
    error("No LocalEndLine provided")
}

val LocalHoldCustomObject = compositionLocalOf<CustomObjectSerializable> {
    error("No LocalHoldCustomObject provided")
}
