package org.elnix.dragonlauncher.settings.bases

interface DatastoreProvider {
    val value: String
    val backupKey: String
    val userBackup: Boolean
}