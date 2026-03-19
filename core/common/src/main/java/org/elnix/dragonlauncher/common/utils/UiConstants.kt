package org.elnix.dragonlauncher.common.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.elnix.dragonlauncher.common.serializables.CustomGlow
import org.elnix.dragonlauncher.common.serializables.CustomObjectSerializable
import org.elnix.dragonlauncher.common.serializables.IconShape

object UiConstants {

    val DRAGON_SHAPE_CORNER_DP = 12.dp

    val CIRCLE_SHAPE_CORNER_DP = 50.dp

    val DragonShape = RoundedCornerShape(DRAGON_SHAPE_CORNER_DP)

    val defaultLineCustomObject = CustomObjectSerializable(
        stroke = 2f,
        color = null, // RGB Color according to the angle
        glow = CustomGlow(
            radius = 10f
        ),

        /** Not used for the line as it goes from `start` to `end` */
        shape = null,
        size = null,
        rotation = null, // No rotation for line, (it's nullable, but I use nul here to indicate that the rotation isn't available)

        eraseBackground = false
    )


    val defaultAngleCustomObject = CustomObjectSerializable(
        stroke = 2f,
        color = null, // RGB Color according to the angle
        glow = CustomGlow(
            radius = 20f
        ),
        shape = IconShape.Circle,
        size = 50f,
        rotation = 90,
        eraseBackground = false
    )

    val defaultStartCustomObject = CustomObjectSerializable(
        stroke = 4f,
        color = null, // RGB Color according to the angle
        glow = CustomGlow(
            radius = 32f
        ),
        shape = IconShape.Circle,
        size = 30f,
        rotation = 0,
        eraseBackground = true
    )

    val defaultEndCustomObject = CustomObjectSerializable(
        stroke = 0f,
        color = null, // RGB Color according to the angle
        glow = CustomGlow(
            radius = 12f
        ),
        shape = IconShape.Circle,
        size = 8f,
        rotation = 0,
        eraseBackground = false
    )


    val defaultHoldCustomObject = CustomObjectSerializable(
        stroke = 10f,
        color = Color.Red,
        glow = CustomGlow(
            radius = 12f
        ),
        shape = IconShape.Circle,
        size = 70f,
        rotation = 0,
        eraseBackground = false
    )
}
