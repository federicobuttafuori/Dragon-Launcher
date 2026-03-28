package org.elnix.dragonlauncher.ui.components.dragon

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.elnix.dragonlauncher.common.utils.UiConstants.DragonShape


@Composable
fun DragonTooltipInternal(
    text: String,
    content: @Composable (() -> Unit)
) {
    @OptIn(ExperimentalMaterial3Api::class)
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            positioning = TooltipAnchorPosition.Above
        ),
        tooltip = {
            PlainTooltip(
                shape = DragonShape,
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 5.dp,
                shadowElevation = 3.dp
            ) {
                Text(text)
            }
        },
        state = rememberTooltipState(),
        content = content
    )
}
@Composable
fun DragonTooltip(
    resId: Int,
    content: @Composable (() -> Unit)
) {
    DragonTooltipInternal(
        text = stringResource(resId),
        content = content
    )
}


@Composable
fun DragonTooltip(
    description: String,
    content: @Composable (() -> Unit)
) {
    DragonTooltipInternal(
        text = description,
        content = content
    )
}
