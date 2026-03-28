package org.elnix.dragonlauncher.ui.components.generic

import android.view.ViewConfiguration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.elnix.dragonlauncher.enumsui.ToggleButtonOption
import org.elnix.dragonlauncher.ui.colors.AppObjectsColors
import org.elnix.dragonlauncher.ui.components.internals.connectedBottomButtonShapes
import org.elnix.dragonlauncher.ui.components.internals.connectedTopButtonShapes
import org.elnix.dragonlauncher.ui.modifiers.buttonGroup

/**
 * A horizontally connected multi-select toggle button group built on Material3 Expressive's
 * [ButtonGroupDefaults] connected shape system.
 *
 * Each button independently toggles without affecting the others, making this suitable
 * for multi-select filter rows, editor toolbars, or any set of orthogonal on/off options.
 *
 * Connected shapes are applied automatically based on position:
 * - First entry → [ButtonGroupDefaults.connectedLeadingButtonShapes]
 * - Last entry  → [ButtonGroupDefaults.connectedTrailingButtonShapes]
 * - Middle entries → [ButtonGroupDefaults.connectedMiddleButtonShapes]
 *
 * @param T Any type implementing [ToggleButtonOption], typically an enum.
 * @param entries The ordered list of options to display as toggle buttons.
 * @param isChecked Predicate returning the current checked state for a given entry.
 * @param onCheck Called when the user taps a button, both on check and uncheck.
 * @param showLabel Whether to show the text label alongside the icon. Defaults to `true`.
 * @param hapticFeedback Whether to emit a [HapticFeedbackType.KeyboardTap] on every tap,
 *   regardless of the resulting checked state. Defaults to `true`.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun <T : ToggleButtonOption> MultiSelectConnectedButtonColumn(
    entries: List<T>,

    // Optional parameters
    showLabel: Boolean = true,
    hapticFeedback: Boolean = true,

    isEnabled: (T) -> Boolean = { true },
    isChecked: (T) -> Boolean,
    onCheck: (T) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Column(
        Modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
    ) {
        entries.forEachIndexed { index, entry ->

            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()

            var showHelp by remember { mutableStateOf(false) }
            var longPressConsumed by remember { mutableStateOf(false) }

            LaunchedEffect(isPressed) {
                if (isPressed) {
                    longPressConsumed = false
                    delay(ViewConfiguration.getLongPressTimeout().toLong())
                    showHelp = true
                    longPressConsumed = true
                }
            }
            // No idea why, but using a not here feels more natural for the displayed entries
            val checked = !isChecked(entry)

            Box {
                ToggleButton(
                    checked = checked,
                    enabled = isEnabled(entry),
                    onCheckedChange = {
                        if (!longPressConsumed) {
                            onCheck(entry)

                            if (hapticFeedback) {
                                haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                            }
                        }
                        longPressConsumed = false
                    },
                    colors = AppObjectsColors.toggleButtonColors(),
                    // Custom shapes
                    shapes =
                        when (index) {
                            0 -> connectedTopButtonShapes()
                            entries.lastIndex -> connectedBottomButtonShapes()
                            else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                        },
                    interactionSource = interactionSource
                ) {
                    Crossfade(!checked) { notChecked ->
                        Icon(
                            entry.iconDisabled.takeIf { notChecked && it != null } ?: entry.iconEnabled,
                            contentDescription = null
                        )
                    }
                }

                DropdownMenu(
                    expanded = showHelp,
                    onDismissRequest = {
                        showHelp = false
                        longPressConsumed = false
                    },
                    containerColor = Color.Transparent,
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp
                ) {
                    Text(
                        text = stringResource(entry.resId ?: -1),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.buttonGroup()
                      )
                }
            }
        }
    }
}