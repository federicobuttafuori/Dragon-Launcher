package org.elnix.dragonlauncher.enumsui

import org.elnix.dragonlauncher.common.R

/**
 * Available methods for locking the settings screen.
 */
enum class LockMethod(
    val resId: Int
) {
    /** No lock — settings are freely accessible */
    NONE(R.string.lock_none),

    /** Require a user-defined PIN code */
    PIN(R.string.lock_pin),

    /** Use native Android device unlock (biometric + device credentials fallback) */
    DEVICE_UNLOCK(R.string.lock_device_unlock)
}
