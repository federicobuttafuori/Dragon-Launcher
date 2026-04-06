package org.elnix.dragonlauncher.common.serializables

import kotlinx.serialization.Serializable

@Serializable
sealed class IconShape {

    @Serializable
    object PlatformDefault : IconShape()

    @Serializable
    object Circle : IconShape()

    @Serializable
    object Square : IconShape()

    @Serializable
    object RoundedSquare : IconShape()

    @Serializable
    object Triangle : IconShape()

    @Serializable
    object Squircle : IconShape()

    @Serializable
    object Hexagon : IconShape()

    @Serializable
    object Pentagon : IconShape()

    @Serializable
    object Teardrop : IconShape()

    @Serializable
    object Pebble : IconShape()

    @Serializable
    object EasterEgg : IconShape()

    @Serializable
    object Random : IconShape()

    @Serializable
    data class Custom(
        val shape: CustomIconShapeSerializable
    ) : IconShape()

    // Not used anymore, it's here for legacy purposes, because it makes the import fail if people has some
    @Serializable
    object Dragon : IconShape()
}


val allShapes = listOf(
    IconShape.PlatformDefault,
    IconShape.Circle,
    IconShape.Square,
    IconShape.RoundedSquare,
    IconShape.Triangle,
    IconShape.Squircle,
    IconShape.Hexagon,
    IconShape.Pentagon,
    IconShape.Teardrop,
    IconShape.Pebble,
    IconShape.EasterEgg,
    IconShape.Random,
)


val allShapesWithoutRandom = listOf(
    IconShape.PlatformDefault,
    IconShape.Circle,
    IconShape.Square,
    IconShape.RoundedSquare,
    IconShape.Triangle,
    IconShape.Squircle,
    IconShape.Hexagon,
    IconShape.Pentagon,
    IconShape.Teardrop,
    IconShape.Pebble,
    IconShape.EasterEgg,
)
