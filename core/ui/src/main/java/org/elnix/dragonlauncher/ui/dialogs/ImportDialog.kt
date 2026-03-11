package org.elnix.dragonlauncher.ui.dialogs

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.elnix.dragonlauncher.common.utils.UiConstants.DragonShape
import org.elnix.dragonlauncher.settings.DataStoreName
import org.elnix.dragonlauncher.settings.backupableStores
import org.elnix.dragonlauncher.settings.bases.BaseSettingsStore
import org.elnix.dragonlauncher.ui.colors.AppObjectsColors
import org.json.JSONObject

@Composable
fun ImportSettingsDialog(
    backupJson: JSONObject,
    onDismiss: () -> Unit,
    onConfirm: (selectedStores: Map<DataStoreName, BaseSettingsStore<*,*>>) -> Unit
) {

    // Filter stores that exist in backup JSON
    val availableStores = backupableStores.filter {
        backupJson.has(it.key.backupKey) ||
        backupJson.has("actions") // Old actions store, for legacy support
    }

    val selected = remember(availableStores) {
        mutableStateMapOf<DataStoreName, Boolean>().apply {
            availableStores.forEach { put(it.key, true) }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(availableStores.filter { selected[it.key] == true })
                },
                colors = AppObjectsColors.buttonColors()
            ) {
                Text("Import")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = AppObjectsColors.cancelButtonColors()
            ) { Text("Cancel") }
        },
        title = { Text("Select settings to import") },
        text = {
            LazyColumn(
                modifier = Modifier.heightIn(max = 600.dp)
            ) {
                selectedActionRow(selected)

                items(availableStores.entries.toList()) { entry ->
                    StoreItem(selected, entry.key, entry.value)
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        shape = DragonShape
    )
}
