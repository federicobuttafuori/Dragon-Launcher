@file:Suppress("AssignedValueIsNeverRead")

package org.elnix.dragonlauncher.ui.settings.customization

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.elnix.dragonlauncher.base.ktx.toDp
import org.elnix.dragonlauncher.common.R
import org.elnix.dragonlauncher.common.logging.logD
import org.elnix.dragonlauncher.common.serializables.FloatingAppObject
import org.elnix.dragonlauncher.common.serializables.FloatingAppsJson
import org.elnix.dragonlauncher.common.serializables.IconShape
import org.elnix.dragonlauncher.common.serializables.SwipeActionSerializable
import org.elnix.dragonlauncher.common.undoredo.UndoRedoManager
import org.elnix.dragonlauncher.common.utils.Constants.Logging.WIDGET_TAG
import org.elnix.dragonlauncher.common.utils.UiConstants.DragonShape
import org.elnix.dragonlauncher.enumsui.UndRedoEditTools
import org.elnix.dragonlauncher.enumsui.WidgetsToolsAddRemove
import org.elnix.dragonlauncher.enumsui.WidgetsToolsCenterReset
import org.elnix.dragonlauncher.enumsui.WidgetsToolsGridScaleNest
import org.elnix.dragonlauncher.enumsui.WidgetsToolsMoveUpDown
import org.elnix.dragonlauncher.enumsui.WidgetsToolsUpDown
import org.elnix.dragonlauncher.models.FloatingAppsViewModel
import org.elnix.dragonlauncher.settings.stores.DebugSettingsStore
import org.elnix.dragonlauncher.settings.stores.WidgetsSettingsStore
import org.elnix.dragonlauncher.ui.components.FloatingAppsHostView
import org.elnix.dragonlauncher.ui.components.dragon.DragonIconButton
import org.elnix.dragonlauncher.ui.components.generic.MultiSelectConnectedButtonColumn
import org.elnix.dragonlauncher.ui.components.generic.MultiSelectConnectedButtonRow
import org.elnix.dragonlauncher.ui.components.settings.asState
import org.elnix.dragonlauncher.ui.dialogs.AddPointDialog
import org.elnix.dragonlauncher.ui.dialogs.NestManagementDialog
import org.elnix.dragonlauncher.ui.dialogs.ShapePickerDialog
import org.elnix.dragonlauncher.ui.helpers.SliderWithLabel
import org.elnix.dragonlauncher.ui.helpers.SmallShapeRow
import org.elnix.dragonlauncher.ui.helpers.settings.SettingsLazyHeader
import org.elnix.dragonlauncher.ui.modifiers.settingsGroup
import org.elnix.dragonlauncher.ui.remembers.LocalFloatingAppsViewModel
import org.elnix.dragonlauncher.ui.statusbar.StatusBar
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FloatingAppsTab(
    onBack: () -> Unit,
    onLaunchSystemWidgetPicker: (nestId: Int) -> Unit,
    onResetWidgetSize: (id: Int, widgetId: Int) -> Unit,
    onRemoveWidget: (FloatingAppObject) -> Unit,
    initialNestId: Int = 0
) {
    val ctx = LocalContext.current

    val floatingAppsViewModel = LocalFloatingAppsViewModel.current
    val cellSizePx = floatingAppsViewModel.cellSizePx
    val scope = rememberCoroutineScope()

    val widgetsDebugInfos by DebugSettingsStore.widgetsDebugInfo.asState()

    val floatingApps by floatingAppsViewModel.floatingApps.collectAsState()

    var selected by remember { mutableStateOf<FloatingAppObject?>(null) }
    val aWidgetIsSelected = selected != null

    var snapMove by remember { mutableStateOf(true) }
    var snapResize by remember { mutableStateOf(true) }

    var showScaleDropdown by remember { mutableStateOf(false) }
    var widgetsScale by remember { mutableFloatStateOf(0.80f) }

    var showAddDialog by remember { mutableStateOf(false) }
    var showNestPickerDialog by remember { mutableStateOf(false) }
    var nestId by remember { mutableIntStateOf(initialNestId) }
    var isPrecisionModeActive by remember { mutableStateOf(false) }



    /* ───────────────────────────────────────────────────────────────── */

    fun snapshotWidgets(): List<FloatingAppObject> = floatingApps.map { it.copy() }


    val undoRedo = remember { UndoRedoManager() }

    LaunchedEffect(Unit) {
        undoRedo.register(
            key = "floatingApps",
            snapshot = { snapshotWidgets() },
            restore = {
                floatingAppsViewModel.restoreFloatingApps(it)
                selected = floatingApps.find { p -> p.id == (selected?.id ?: "") }
            }
        )
    }

    fun save() {
        scope.launch {
            WidgetsSettingsStore.jsonSetting.set(ctx, FloatingAppsJson.encodeFloatingApps(snapshotWidgets()))
        }
    }

    fun applyChange(mutator: () -> Unit) {
        undoRedo.applyChange(mutator)
        save()
    }

    fun undo() {
        undoRedo.undo()
        save()
    }

    fun redo() {
        undoRedo.redo()
        save()
    }

    fun undoAll() {
        undoRedo.undoAll()
        save()
    }

    fun redoAll() {
        undoRedo.redoAll()
        save()
    }


    fun removeWidget(floatingApp: FloatingAppObject) {
        onRemoveWidget(floatingApp)
        if (selected == floatingApp) selected = null
    }


    /**
     * The widgets and the grid, displayed first, to keep access to the buttons
     *
     * The pointerInput is used to disable any widgets on click outside
     */
    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    selected = null
                }
            }
            .scale(widgetsScale)
            .border(1.dp, MaterialTheme.colorScheme.primary, DragonShape)
    ) {

        /**
         * Draw the grid of snapping that fills the entire screen
         */
        if (snapMove) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val lineWidth = 1f
                val color = Color.White.copy(alpha = 0.25f)

                // Vertical lines
                var x = 0f
                while (x <= size.width) {
                    drawLine(
                        color = color,
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = lineWidth
                    )
                    x += cellSizePx
                }

                // Horizontal lines
                var y = 0f
                while (y <= size.height) {
                    drawLine(
                        color = color,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = lineWidth
                    )
                    y += cellSizePx
                }
            }
        }

        /* ──────────────── Widget canvas ──────────────── */
        floatingApps
            .filter { it.nestId == nestId }
            // Dont sort them, because when you press the reorder buttons, it feels strange
//            .sortedBy { it.id == selected?.id } // Selected is always displayed first for easier click access
            .forEach { floatingApp ->
                key(floatingApp.id, nestId) {
                    DraggableFloatingApp(
                        floatingAppsViewModel = floatingAppsViewModel,
                        app = floatingApp,
                        selected = floatingApp.id == selected?.id,
                        onSelect = { selected = floatingApp },
                        onPrecisionModeChange = { isPrecisionModeActive = it },
                        onMove = { dx, dy ->
                            // I don't apply to stack on every movement, and so don't save
                            floatingAppsViewModel.moveFloatingApp(floatingApp.id, dx, dy, false)
                        },
                        onRotateEnd = {
                            applyChange {
                                floatingAppsViewModel.rotateFloatingApp(floatingApp.id, it, true)
                            }
                        },
                        onMoveEnd = {
                            // Only save on move end to avoid I/O overhead
                            applyChange {
                                floatingAppsViewModel.moveFloatingApp(floatingApp.id, 0f, 0f, snapMove)
                            }
                        },
                        onResize = { corner, dx, dy ->
                            // Same thing here, don't apply change until fully resized
                            floatingAppsViewModel.resizeFloatingApp(floatingApp.id, corner, dx, dy, false)
                        },
                        onResizeEnd = { corner ->
                            applyChange {
                                floatingAppsViewModel.resizeFloatingApp(floatingApp.id, corner, 0f, 0f, snapResize)
                            }
                        },
                        onEdit = {
                            applyChange {
                                floatingAppsViewModel.editFloatingApp(it)
                            }
                        }
                    )
                }
            }


        AnimatedVisibility(
            visible = isPrecisionModeActive,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp),
            enter = fadeIn() + slideInVertically { -it },
            exit = fadeOut() + slideOutVertically { -it }
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                contentColor = MaterialTheme.colorScheme.surface,
                shape = CircleShape
            ) {
                Text(
                    text = stringResource(R.string.precision_mode_active),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }


    /**
     * The settings scaffold, with on top the optional status bar
     */
    Column(
        Modifier.fillMaxSize()
    ) {

        StatusBar(null)

        SettingsLazyHeader(
            title = stringResource(R.string.widgets),
            onBack = onBack,
            helpText = stringResource(R.string.floating_apps_tab_help),
            onReset = {
                scope.launch {
                    applyChange {
                        floatingAppsViewModel.resetAllFloatingApps()
                    }
                }
            },
            otherIcons = arrayOf(
                ({
                    showScaleDropdown = !showScaleDropdown
                } to Icons.Default.LinearScale)
            ),
            bottomContent = {
                /* ───────────── Bottom controls ───────────── */

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    MultiSelectConnectedButtonRow(
                        entries = WidgetsToolsGridScaleNest.entries,
                        showLabel = false,
                        isChecked = {
                            when (it) {
                                WidgetsToolsGridScaleNest.Grid -> snapMove
                                WidgetsToolsGridScaleNest.Scale -> snapResize
                                WidgetsToolsGridScaleNest.Nests -> true
                            }
                        }
                    ) { entry ->
                        scope.launch {
                            when (entry) {
                                WidgetsToolsGridScaleNest.Grid -> {
                                    snapMove = !snapMove
                                }

                                WidgetsToolsGridScaleNest.Scale -> {
                                    snapResize = !snapResize
                                }

                                WidgetsToolsGridScaleNest.Nests -> {
                                    showNestPickerDialog = true
                                }
                            }
                        }
                    }

                    // Undo/Redo bar
                    val undoButtonEnabled = undoRedo.canUndo
                    val redoButtonEnabled = undoRedo.canRedo

                    MultiSelectConnectedButtonRow(
                        entries = UndRedoEditTools.entries,
                        showLabel = false,

                        isEnabled = {
                            when (it) {
                                UndRedoEditTools.UndoAll -> undoButtonEnabled
                                UndRedoEditTools.Undo -> undoButtonEnabled
                                UndRedoEditTools.Redo -> redoButtonEnabled
                                UndRedoEditTools.RedoAll -> redoButtonEnabled
                            }
                        }
                    ) { entry ->
                        scope.launch {
                            when (entry) {
                                UndRedoEditTools.UndoAll -> undoAll()
                                UndRedoEditTools.Undo -> undo()
                                UndRedoEditTools.Redo -> redo()
                                UndRedoEditTools.RedoAll -> redoAll()
                            }
                        }
                    }
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MultiSelectConnectedButtonRow(
                        entries = WidgetsToolsAddRemove.entries,
                        showLabel = false,
                        isChecked = {
                            when (it) {
                                WidgetsToolsAddRemove.Add -> true
                                WidgetsToolsAddRemove.Remove -> aWidgetIsSelected
                            }
                        },
                        isEnabled = {
                            when (it) {
                                WidgetsToolsAddRemove.Add -> true
                                WidgetsToolsAddRemove.Remove -> aWidgetIsSelected
                            }
                        }
                    ) { entry ->
                        scope.launch {
                            when (entry) {
                                WidgetsToolsAddRemove.Add -> {
                                    showAddDialog = true
                                }

                                WidgetsToolsAddRemove.Remove -> {
                                    selected?.let {
                                        applyChange {
                                            removeWidget(it)
                                        }
                                    }
                                }
                            }
                        }
                    }


                    MultiSelectConnectedButtonColumn(
                        entries = WidgetsToolsCenterReset.entries,
                        showLabel = false,
                        isChecked = { true },
                        isEnabled = { aWidgetIsSelected }
                    ) { entry ->
                        scope.launch {
                            when (entry) {
                                WidgetsToolsCenterReset.Center -> {
                                    selected?.let {
                                        applyChange {
                                            floatingAppsViewModel.centerFloatingApp(it.id)
                                        }
                                    }
                                }

                                WidgetsToolsCenterReset.Reset -> {
                                    selected?.let {
                                        applyChange {
                                            if (it.action is SwipeActionSerializable.OpenWidget) {
                                                onResetWidgetSize(it.id, (it.action as SwipeActionSerializable.OpenWidget).widgetId)
                                            } else {
                                                floatingAppsViewModel.resetFloatingAppSize(it.id)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    MultiSelectConnectedButtonColumn(
                        entries = WidgetsToolsUpDown.entries,
                        showLabel = false,
                        isChecked = { true }
                    ) { entry ->
                        scope.launch {
                            when (entry) {
                                WidgetsToolsUpDown.Up -> {
                                    if (floatingApps.isNotEmpty()) {
                                        val idx = floatingApps.indexOfFirst { it == selected }
                                        val next = if (idx <= 0) floatingApps.last() else floatingApps[idx - 1]
                                        selected = next
                                    }
                                }

                                WidgetsToolsUpDown.Down -> {
                                    if (floatingApps.isNotEmpty()) {
                                        val idx = floatingApps.indexOfFirst { it == selected }
                                        val next = if (idx == -1 || idx == floatingApps.lastIndex) floatingApps.first() else floatingApps[idx + 1]
                                        selected = next
                                    }
                                }
                            }
                        }
                    }

                    val upDownEnabled = aWidgetIsSelected && floatingApps.size > 1

                    MultiSelectConnectedButtonColumn(
                        entries = WidgetsToolsMoveUpDown.entries,
                        showLabel = false,
                        isEnabled = { upDownEnabled },
                        isChecked = { upDownEnabled }
                    ) { entry ->
                        scope.launch {
                            when (entry) {
                                WidgetsToolsMoveUpDown.MoveUp -> {
                                    selected?.let {
                                        applyChange {
                                            floatingAppsViewModel.moveFloatingAppDown(it.id)
                                        }
                                    }
                                }

                                WidgetsToolsMoveUpDown.MoveDown -> {
                                    selected?.let {
                                        applyChange {
                                            floatingAppsViewModel.moveFloatingAppUp(it.id)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            content = {
                Column(Modifier.fillMaxSize()) {
                    AnimatedVisibility(showScaleDropdown) {
                        Column(
                            modifier = Modifier.settingsGroup()
                        ) {

                            Text("${stringResource(R.string.widget_number)}: ${floatingApps.size}")
                            Text("${stringResource(R.string.current_nest)}: $nestId")

                            HorizontalDivider()

                            SliderWithLabel(
                                label = stringResource(R.string.scale),
                                value = widgetsScale,
                                valueRange = 0.5f..1f,
                                onReset = { widgetsScale = 0.85f }
                            ) { widgetsScale = it }
                        }
                    }
                }
            }
        )
    }


    // ─── Dialogs ───────────────────────────

    if (widgetsDebugInfos) {
        Box(
            modifier = Modifier
                .background(Color.DarkGray.copy(0.5f))
                .padding(5.dp)
        ) {
            Column {
                floatingApps.forEach {
                    Text(it.toString())
                }
            }
        }
    }

    if (showAddDialog) {
        AddPointDialog(
            onDismiss = { showAddDialog = false },
            actions = listOf(
                SwipeActionSerializable.OpenWidget(0, "", ""),
                SwipeActionSerializable.OpenCircleNest(0),
                SwipeActionSerializable.GoParentNest,
                SwipeActionSerializable.LaunchShortcut("", ""),
                SwipeActionSerializable.LaunchApp("", false, 0),
                SwipeActionSerializable.OpenUrl(""),
                SwipeActionSerializable.OpenFile(""),
                SwipeActionSerializable.NotificationShade,
                SwipeActionSerializable.ControlPanel,
                SwipeActionSerializable.OpenAppDrawer(),
                SwipeActionSerializable.Lock,
                SwipeActionSerializable.ReloadApps,
                SwipeActionSerializable.OpenRecentApps,
                SwipeActionSerializable.OpenDragonLauncherSettings()
            ),
            onActionSelected = { action ->
                when (action) {
                    is SwipeActionSerializable.OpenWidget -> onLaunchSystemWidgetPicker(nestId)
                    else -> floatingAppsViewModel.addFloatingApp(action, nestId = nestId)
                }
                showAddDialog = false
            }
        )
    }

    if (showNestPickerDialog) {
        NestManagementDialog(
            onDismissRequest = { showNestPickerDialog = false },
            title = stringResource(R.string.pick_a_nest),
            onDelete = null,
            onNewNest = null,
            onNameChange = null
        ) {
            logD(WIDGET_TAG) { it.toString() }
            nestId = it.id
            selected = null
            logD(WIDGET_TAG) { nestId.toString() }

            showNestPickerDialog = false
        }
    }
}


/**
 * Handles all widget interactions: drag to move, resize handles, tap/long-press.
 * Resize handles provide visual-only resize feedback by compensating position changes internally.
 *
 * @param floatingAppsViewModel ViewModel for widget state management
 * @param app Current widget data
 * @param selected True if this widget is currently selected
 * @param onSelect Callback when widget is tapped/selected
 * @param onMove Callback for position drag deltas (dx, dy in pixels)
 * @param onResize Callback for resize drag (corner, dx, dy in pixels)
 */
@SuppressLint("LocalContextResourcesRead")
@Composable
private fun DraggableFloatingApp(
    floatingAppsViewModel: FloatingAppsViewModel,
    app: FloatingAppObject,
    selected: Boolean,
    onPrecisionModeChange: (Boolean) -> Unit,
    onSelect: () -> Unit,
    onMove: (Float, Float) -> Unit,
    onRotateEnd: (Float) -> Unit,
    onMoveEnd: (Boolean) -> Unit,
    onResize: (FloatingAppsViewModel.ResizeCorner, Float, Float) -> Unit,
    onResizeEnd: (FloatingAppsViewModel.ResizeCorner) -> Unit,
    onEdit: (FloatingAppObject) -> Unit
) {
    val ctx = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent

    val cellSizePx = floatingAppsViewModel.cellSizePx

    val dm = ctx.resources.displayMetrics

    val widthPixels = dm.widthPixels
    val heightPixels = dm.heightPixels

    val x = (app.x * widthPixels).toInt()
    val y = (app.y * heightPixels).toInt()

    val width = app.spanX * cellSizePx
    val height = app.spanY * cellSizePx


    var widgetCenter by remember { mutableStateOf(Offset.Zero) }
    var handleCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    var widgetAngle by remember(app.angle) { mutableFloatStateOf(app.angle) }
    var isPrecisionMode by remember { mutableStateOf(false) }
    var showEditPopup by remember { mutableStateOf(false) }
    var showShapeEditor by remember { mutableStateOf(false) }

    LaunchedEffect(isPrecisionMode) {
        onPrecisionModeChange(isPrecisionMode)
    }

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = x,
                    y = y
                )
            }
            .size(
                width = width.toDp,
                height = height.toDp
            )
            // Used to compute the widget position for rotation computing
            .onGloballyPositioned { coordinates ->
                val rect = coordinates.boundsInRoot()
                widgetCenter = Offset(
                    rect.left + rect.width / 2f,
                    rect.top + rect.height / 2f
                )
            }
            .graphicsLayer {
                rotationZ = widgetAngle
                transformOrigin = TransformOrigin.Center
            }
            .border(
                width = if (selected) 2.dp else 0.dp,
                color = borderColor,
                shape = DragonShape
            )
    ) {

        // Widget / App content (touch blocked during editing)
        FloatingAppsHostView(
            floatingAppObject = app,
            blockTouches = true,
            cellSizePx = cellSizePx
        ) { }


        // Main interaction overlay (move + tap)
        Box(
            modifier = Modifier
                .matchParentSize()
                .pointerInput(app.id) {
                    detectTapGestures(
                        onPress = {
                            isPrecisionMode = false
                            onSelect()
                            try {
                                withTimeout(viewConfiguration.longPressTimeoutMillis) {
                                    tryAwaitRelease()
                                }
                            } catch (_: TimeoutCancellationException) {
                                isPrecisionMode = true
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        }
                    )
                }
                .pointerInput(app.id) {
                    detectDragGestures(
                        onDragStart = { onSelect() },
                        onDrag = { change, dragAmount ->

                            val angleRad = Math.toRadians(widgetAngle.toDouble())

                            val cos = cos(angleRad)
                            val sin = sin(angleRad)

                            val amountX = if (isPrecisionMode) dragAmount.x / 2f else dragAmount.x
                            val amountY = if (isPrecisionMode) dragAmount.y / 2f else dragAmount.y

                            val worldDx = (amountX * cos - amountY * sin).toFloat()
                            val worldDy = (amountX * sin + amountY * cos).toFloat()

                            change.consume()
                            onMove(worldDx, worldDy)
                        },
                        onDragEnd = {
                            onMoveEnd(isPrecisionMode)
                            isPrecisionMode = false
                        },
                        onDragCancel = {
                            isPrecisionMode = false
                        }
                    )
                }
        )

        if (selected) {

            // Rotate drag handle
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-40).dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .onGloballyPositioned {
                        handleCoordinates = it
                    }
                    .pointerInput(app.id) {

                        var dragStartFingerAngle: Float? = null
                        var dragStartWidgetAngle = 0f

                        detectDragGestures(

                            onDragStart = { offset ->

                                val rootPos = handleCoordinates
                                    ?.localToRoot(offset)
                                    ?: return@detectDragGestures

                                dragStartFingerAngle = Math.toDegrees(
                                    atan2(
                                        (rootPos.y - widgetCenter.y).toDouble(),
                                        (rootPos.x - widgetCenter.x).toDouble()
                                    )
                                ).toFloat()

                                dragStartWidgetAngle = widgetAngle
                            },

                            onDragEnd = {
                                dragStartFingerAngle = null
                                onRotateEnd(widgetAngle)
                            },

                            onDragCancel = {
                                dragStartFingerAngle = null
                            }

                        ) { change, _ ->

                            val rootPos = handleCoordinates
                                ?.localToRoot(change.position)
                                ?: return@detectDragGestures

                            val currentFingerAngle = Math.toDegrees(
                                atan2(
                                    (rootPos.y - widgetCenter.y).toDouble(),
                                    (rootPos.x - widgetCenter.x).toDouble()
                                )
                            ).toFloat()

                            dragStartFingerAngle?.let { startAngle ->

                                var delta = currentFingerAngle - startAngle

                                if (delta > 180f) delta -= 360f
                                if (delta < -180f) delta += 360f

                                widgetAngle = dragStartWidgetAngle + delta
                            }

                            change.consume()
                        }
                    }
            )


            // ──────────────────────────────────────────
            // Resize handles - only visible when selected
            // ──────────────────────────────────────────

            val dotSize = 12.dp
            val hitboxPadding = 20.dp

            // Top handle
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = -((dotSize.value / 2 + hitboxPadding.value).dp))
                    .size(dotSize + hitboxPadding * 2)
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .pointerInput(FloatingAppsViewModel.ResizeCorner.Top) {
                        detectDragGestures(
                            onDragEnd = {
                                onResizeEnd(FloatingAppsViewModel.ResizeCorner.Top)
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            onResize(FloatingAppsViewModel.ResizeCorner.Top, 0f, dragAmount.y)
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .align(Alignment.Center)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
            }

            // Bottom handle
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = ((dotSize.value / 2 + hitboxPadding.value).dp))
                    .size(dotSize + hitboxPadding * 2)
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .pointerInput(FloatingAppsViewModel.ResizeCorner.Bottom) {
                        detectDragGestures(
                            onDragEnd = {
                                onResizeEnd(FloatingAppsViewModel.ResizeCorner.Bottom)
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            onResize(FloatingAppsViewModel.ResizeCorner.Bottom, 0f, dragAmount.y)
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .align(Alignment.Center)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
            }

            // Left handle
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = -((dotSize.value / 2 + hitboxPadding.value).dp))
                    .size(dotSize + hitboxPadding * 2)
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .pointerInput(FloatingAppsViewModel.ResizeCorner.Left) {
                        detectDragGestures(
                            onDragEnd = {
                                onResizeEnd(FloatingAppsViewModel.ResizeCorner.Left)
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            onResize(FloatingAppsViewModel.ResizeCorner.Left, dragAmount.x, 0f)
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .align(Alignment.Center)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
            }

            // Right handle
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = ((dotSize.value / 2 + hitboxPadding.value).dp))
                    .size(dotSize + hitboxPadding * 2)
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .pointerInput(FloatingAppsViewModel.ResizeCorner.Right) {
                        detectDragGestures(
                            onDragEnd = {
                                onResizeEnd(FloatingAppsViewModel.ResizeCorner.Right)
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            onResize(FloatingAppsViewModel.ResizeCorner.Right, dragAmount.x, 0f)
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .align(Alignment.Center)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                )
            }


            // Edit button
            DragonIconButton(
                onClick = { showEditPopup = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(Color.Transparent)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit)
                )
            }
        }


        // ──────────────────────────────────────────
        // Ghost Toggle, to prevent clicks
        // ──────────────────────────────────────────

        DropdownMenu(
            expanded = showEditPopup,
            onDismissRequest = { showEditPopup = false },
            containerColor = Color.Transparent,
            shadowElevation = 0.dp,
            tonalElevation = 0.dp
        ) {
            Column(
                modifier = Modifier.settingsGroup()
            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = app.ghosted == true,
                        onCheckedChange = {
                            onEdit(app.copy(ghosted = it))
                        }
                    )

                    Text(
                        text = stringResource(R.string.ghosted),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 12.sp
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = app.foreground == true,
                        onCheckedChange = {
                            onEdit(app.copy(foreground = it))
                        }
                    )

                    Text(
                        text = stringResource(R.string.foreground),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 12.sp
                    )
                }

                SmallShapeRow(
                    selected = app.shape ?: IconShape.Square,
                    onReset = {
                        onEdit(app.copy(shape = null))
                    }
                ) { showShapeEditor = true }
            }
        }
    }

    if (showShapeEditor) {
        ShapePickerDialog(
            selected = app.shape ?: IconShape.Square,
            onDismiss = { showShapeEditor = false }
        ) {
            showShapeEditor = false
            onEdit(app.copy(shape = it))
        }
    }

}
