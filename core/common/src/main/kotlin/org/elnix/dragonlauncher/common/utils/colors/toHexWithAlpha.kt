package org.elnix.dragonlauncher.common.utils.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

// ───────────── Utility: convert Color → #AARRGGBB ─────────────
fun Color?.toHexWithAlpha(prefix: Boolean = true): String =
    "${if (prefix) "#" else ""}%08X".format(this?.toArgb())
