package org.elnix.dragonlauncher.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.elnix.dragonlauncher.enumsui.BackupSelectStoresButtons
import org.elnix.dragonlauncher.enumsui.BackupSelectStoresButtons.DESELECT_ALL
import org.elnix.dragonlauncher.enumsui.BackupSelectStoresButtons.INVERT
import org.elnix.dragonlauncher.enumsui.BackupSelectStoresButtons.SELECT_ALL
import org.elnix.dragonlauncher.settings.DataStoreName
import org.elnix.dragonlauncher.settings.backupableStores
import org.elnix.dragonlauncher.settings.bases.BaseSettingsStore
import org.elnix.dragonlauncher.ui.UiConstants.DragonShape
import org.elnix.dragonlauncher.ui.colors.AppObjectsColors
import org.elnix.dragonlauncher.ui.components.generic.MultiSelectConnectedButtonRow

@Composable
fun ExportSettingsDialog(
    onDismiss: () -> Unit,
    availableStores: Map<DataStoreName, BaseSettingsStore<*, *>> = backupableStores,
    defaultStores: Map<DataStoreName, BaseSettingsStore<*, *>> = backupableStores,
    onConfirm: (selectedStores: Map<DataStoreName, BaseSettingsStore<*, *>>) -> Unit
) {

    val selected = remember(availableStores) {
        mutableStateMapOf<DataStoreName, Boolean>().apply {
            availableStores.forEach { put(it.key, it.value in defaultStores.values) }
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
                Text("Export")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = AppObjectsColors.cancelButtonColors()
            ) { Text("Cancel") }
        },
        title = { Text("Select settings to export") },
        text = {
            LazyColumn(
                modifier = Modifier.heightIn(max = 600.dp)
            ) {
                selectedActionRow(selected, availableStores.size)

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

fun LazyListScope.selectedActionRow(
    selected: SnapshotStateMap<DataStoreName, Boolean>,
    totalStoresNumber: Int
) {
    item {
        MultiSelectConnectedButtonRow(
            entries = BackupSelectStoresButtons.entries,
            isEnabled = {
                when (it) {
                    DESELECT_ALL -> selected.isNotEmpty()
                    SELECT_ALL -> selected.size < totalStoresNumber
                    INVERT -> true
                }
            }
        ) {
            when (it) {
                DESELECT_ALL -> {
                    selected.forEach { (store, _) ->
                        selected[store] = false
                    }
                }
                SELECT_ALL -> {
                    selected.forEach { (store, _) ->
                        selected[store] = true
                    }
                }
                INVERT -> {
                    selected.forEach { (store, isSelected) ->
                        selected[store] = !isSelected
                    }
                }
            }
        }
    }
}


@Composable
fun StoreItem(
    selected: SnapshotStateMap<DataStoreName, Boolean>,
    dataStoreName: DataStoreName,
    settingsStore: BaseSettingsStore<*, *>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(DragonShape)
            .padding(vertical = 4.dp)
            .toggleable(
                value = selected[dataStoreName] ?: true,
            ) { selected[dataStoreName] = it },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(settingsStore.name)
        Checkbox(
            checked = selected[dataStoreName] ?: true,
            onCheckedChange = null
        )
    }
}