package org.elnix.dragonlauncher.ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import org.elnix.dragonlauncher.settings.stores.BehaviorSettingsStore
import org.elnix.dragonlauncher.ui.components.settings.asState

@Composable
fun withHaptic(
    type: HapticFeedbackType = HapticFeedbackType.ContextClick,
    block: () -> Unit
): () -> Unit {
    val haptic = LocalHapticFeedback.current
    val disableHapticGlobally by BehaviorSettingsStore.disableHapticFeedbackGlobally.asState()
    val latestBlock = rememberUpdatedState(block)

    return retain(type, haptic, disableHapticGlobally) {
        {
            if (!disableHapticGlobally) haptic.performHapticFeedback(type)
            latestBlock.value()
        }
    }
}