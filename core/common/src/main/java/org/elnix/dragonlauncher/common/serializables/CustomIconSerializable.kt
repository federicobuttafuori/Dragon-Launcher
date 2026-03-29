package org.elnix.dragonlauncher.common.serializables

import kotlinx.serialization.Serializable


/**
 * Describes a highly customizable icon that can be rendered from multiple sources
 * (vector, bitmap, text, or procedural shape) with advanced visual controls.
 *
 * This model is renderer-agnostic and supports extreme theming and animation use cases.
 */
@Serializable
data class CustomIconSerializable(

    /** Icon source type determining how `source` is interpreted. */
    val type: IconType? = null,
    /**
     * Icon source reference.
     * - BITMAP: base64-encoded image
     * - ICON_PACK: base64-encoded image
     * - TEXT: emoji or glyph
     * - SHAPE: renderer-defined primitive
     */
    val source: String? = null,

    /** Tint color (ARGB) applied after rendering. */
    val tint: Int? = null,

    /** Icon opacity multiplier (0.0 – 1.0). */
    val opacity: Float? = null,

    /** Per-corner radius override for icon clipping. */
    val shape: IconShape? = null,

    /** Stroke width (dp) around the icon shape. */
    val strokeWidth: Float? = null,

    /** Stroke color (ARGB) around the icon. */
    val strokeColor: Long? = null,

    /** Blur radius for icon shadow. */
    val shadowRadius: Float? = null,

    /** Shadow color (ARGB). */
    val shadowColor: Long? = null,

    /** Horizontal shadow offset (dp). */
    val shadowOffsetX: Float? = null,

    /** Vertical shadow offset (dp). */
    val shadowOffsetY: Float? = null,

    /** Rotation applied to the icon in degrees. */
    val rotationDeg: Float? = null,

    /** Horizontal scale multiplier. */
    val scaleX: Float? = null,

    /** Vertical scale multiplier. */
    val scaleY: Float? = null,

)

/**
 * Defines how a custom icon should be interpreted and rendered.
 */
@Serializable
enum class IconType {

    /** Icon sourced from an installed icon pack. */
    ICON_PACK,

    /** icon sourced from and image (PNG, JPG, WEBP). */
    BITMAP,

    /** Text-based icon (emoji, glyph, font icon). */
    TEXT,
    PLAIN_COLOR
}