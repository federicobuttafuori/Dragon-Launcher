package org.elnix.dragonlauncher.ui.settings

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import org.elnix.dragonlauncher.common.R
import org.elnix.dragonlauncher.ui.helpers.SwitchRow
import org.elnix.dragonlauncher.ui.helpers.settings.SettingsLazyHeader

@Composable
fun PermissionsTab(onBack: () -> Unit) {
    val ctx = LocalContext.current
    val permissionStates = remember { mutableStateMapOf<String, Boolean>() }

    fun checkPermissions() {
        val perms = listOf(
            Manifest.permission.QUERY_ALL_PACKAGES,
            Manifest.permission.REQUEST_DELETE_PACKAGES,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.BIND_APPWIDGET,
        ).let { list ->
            var updatedList = list
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                updatedList = updatedList + Manifest.permission.POST_NOTIFICATIONS
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                updatedList = updatedList + Manifest.permission.SCHEDULE_EXACT_ALARM
            }
            updatedList
        }

        perms.forEach { permission ->
            permissionStates[permission] = ContextCompat.checkSelfPermission(
                ctx,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    LaunchedEffect(Unit) {
        checkPermissions()
    }

    Column {
        SettingsLazyHeader(
            title = stringResource(R.string.permissions),
            onBack = onBack,
            helpText = stringResource(R.string.permission_tab_help),
            onReset = null
        ) {
            item {
                Text(
                    text = stringResource(R.string.special_system_access),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                SwitchRow(
                    state = null,
                    onCheck = {
                        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                        ctx.startActivity(intent)
                    },
                    text = stringResource(R.string.notification_access),
                    subText = ""
                )
            }

            item {
                SwitchRow(
                    state = ctx.packageManager.canRequestPackageInstalls(),
                    onCheck = {
                        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                            data = "package:${ctx.packageName}".toUri()
                        }
                        ctx.startActivity(intent)
                    },
                    text = stringResource(R.string.install_from_unknown_source_permission),
                    subText = stringResource(R.string.install_from_unknown_source_permission_desc)
                )
            }

            item {
                SwitchRow(
                    state = null,
                    onCheck = {
                        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                        ctx.startActivity(intent)
                    },
                    text = stringResource(R.string.accessibility_service),
                    subText = stringResource(R.string.accessibility_service_desc)

                )
            }

            item {
                SwitchRow(
                    state = null,
                    enabled = false,
                    onCheck = {
                        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                        ctx.startActivity(intent)
                    },
                    text = stringResource(R.string.usage_access),
                    subText = stringResource(R.string.not_implemented)
//                    subText = stringResource(R.string.usage_access_desc)
                )
            }

            item {
                Text(
                    text = stringResource(R.string.android_permissions),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            val androidPermissions = listOf(
                Manifest.permission.QUERY_ALL_PACKAGES,
                Manifest.permission.BIND_APPWIDGET,
            ).let { list ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    list + Manifest.permission.POST_NOTIFICATIONS
                } else list
            }

            androidPermissions.forEach { permission ->
                item {
                    val isGranted = permissionStates[permission] ?: false
                    SwitchRow(
                        state = isGranted,
                        onCheck = {
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", ctx.packageName, null)
                                }
                            ctx.startActivity(intent)
                        },
                        text = permission.substringAfterLast("."),
                        subText = stringResource(R.string.manage_in_system_settings)
                    )
                }
            }
        }
    }
}
