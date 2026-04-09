package org.elnix.dragonlauncher.logging

data class LogAlert(
    val level: Int,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)