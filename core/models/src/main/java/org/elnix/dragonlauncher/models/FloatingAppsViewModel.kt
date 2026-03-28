package org.elnix.dragonlauncher.models

import android.annotation.SuppressLint
import android.app.Application
import android.appwidget.AppWidgetProviderInfo
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.elnix.dragonlauncher.common.serializables.FloatingAppObject
import org.elnix.dragonlauncher.common.serializables.FloatingAppsJson
import org.elnix.dragonlauncher.common.serializables.SwipeActionSerializable
import org.elnix.dragonlauncher.settings.stores.LegacyFloatingAppsSettingsStore
import org.elnix.dragonlauncher.settings.stores.WidgetsSettingsStore
import kotlin.math.roundToInt
import kotlin.random.Random

class FloatingAppsViewModel(
    application: Application
) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val ctx = application.applicationContext

    private val _floatingApps = MutableStateFlow<List<FloatingAppObject>>(emptyList())
    val floatingApps = _floatingApps.asStateFlow()



    private val dm = ctx.resources.displayMetrics

    val cellSizePx = (30.dp * dm.density).value
    private val screenWidth = dm.widthPixels.toFloat()
    private val screenHeight = dm.heightPixels.toFloat()
    private val minSize = 1.5f

    init {
        loadFloatingApps()
    }

    /* ───────────────────────────── Public API ───────────────────────────── */

    fun addFloatingApp(action: SwipeActionSerializable, info: AppWidgetProviderInfo? = null, nestId: Int) {

        viewModelScope.launch {
            val appWidgetId = if (action is SwipeActionSerializable.OpenWidget) action.widgetId else null
            val app = FloatingAppObject(
                id = Random.nextInt(),
                appWidgetId = appWidgetId,
                nestId = nestId,
                action = action
            )

            _floatingApps.value += app

            centerFloatingApp(appId = app.id)
            resetFloatingAppSize(appId = app.id, info = info)
        }
    }


    fun removeFloatingApp(id: Int, onDeleteId: (Int) -> Unit) {
        viewModelScope.launch {
            _floatingApps.value = _floatingApps.value.filterNot { it.id == id }
            onDeleteId(id)
        }
    }


    fun moveFloatingApp(
        appId: Int,
        dxPx: Float,
        dyPx: Float,
        snap: Boolean,
        snapScale: Float = cellSizePx
    ) {

        updateApp(appId) { app ->
            var newX = app.x + dxPx / screenWidth
            var newY = app.y + dyPx / screenHeight

            if (snap) {
                val snapX = snapScale / screenWidth
                val snapY = snapScale / screenHeight
                newX = (newX / snapX).roundToInt() * snapX
                newY = (newY / snapY).roundToInt() * snapY
            }

            app.copy(
                x = newX,
                y = newY
            )
        }
    }


    fun rotateFloatingApp(id: Int, newAngle: Float, snap: Boolean) {
        updateApp(id) { app ->

            val snapped = if (snap) {
                (newAngle / 15f).roundToInt() * 15f
            } else newAngle

            app.copy(angle = snapped)
        }
    }

    fun moveFloatingAppUp(appId: Int) {
        val current = _floatingApps.value
        val index = current.indexOfFirst { it.id == appId }
        if (index <= 0) return

        val moved = current.toMutableList().apply {
            val floatingApp = removeAt(index)
            add(index - 1, floatingApp)
        }
        _floatingApps.value = moved
    }

    fun moveFloatingAppDown(appId: Int) {
        val current = _floatingApps.value
        val index = current.indexOfFirst { it.id == appId }
        if (index == -1 || index == current.lastIndex) return

        val moved = current.toMutableList().apply {
            val floatingApp = removeAt(index)
            add(index + 1, floatingApp)
        }
        _floatingApps.value = moved
    }


    fun centerFloatingApp(appId: Int) {

        updateApp(appId) { app ->
            val floatingAppWidthPx = app.spanX * cellSizePx
            val floatingAppHeightPx = app.spanY * cellSizePx

            val centerXPx = (screenWidth - floatingAppWidthPx) / 2f
            val centerYPx = (screenHeight - floatingAppHeightPx) / 2f

            app.copy(
                x = centerXPx / screenWidth,
                y = centerYPx / screenHeight
            )
        }
    }


    fun resetFloatingAppSize(appId: Int, info: AppWidgetProviderInfo? = null) {
        updateApp(appId) { app ->
            app.copy(
                spanX = calculateSpanX(info?.minWidth?.toFloat()),
                spanY = calculateSpanY(info?.minHeight?.toFloat()),
                angle = 0f
            )
        }
    }

    /**
     * Resizes a floatingApp while compensating position to maintain visual anchor point.
     * Left/Top resize moves position opposite to drag direction so visual edge stays fixed.
     * Optionally snaps the floatingApp's span to a given scale.
     *
     * @param appId ID of floatingApp to resize
     * @param corner Resize corner/handle being dragged
     * @param dxPx Horizontal drag delta in pixels
     * @param dyPx Vertical drag delta in pixels
     * @param snap If true, snap the floatingApp's width/height to multiples of snapScale
     * @param snapScale Scale in pixels for snapping (default 10px)
     */
    fun resizeFloatingApp(
        appId: Int,
        corner: ResizeCorner,
        dxPx: Float,
        dyPx: Float,
        snap: Boolean,
        snapScale: Float = cellSizePx
    ) {
        updateApp(appId) { app ->

            val deltaSpanX = dxPx / cellSizePx
            val deltaSpanY = dyPx / cellSizePx
            val deltaPosX = dxPx / screenWidth
            val deltaPosY = dyPx / screenHeight

            var newSpanX = app.spanX
            var newSpanY = app.spanY
            var posDeltaX = 0f
            var posDeltaY = 0f

            when (corner) {
                ResizeCorner.Left -> {
                    newSpanX = (app.spanX - deltaSpanX).coerceAtLeast(minSize)
                    posDeltaX = deltaPosX  // Compensate position to keep left edge fixed
                }

                ResizeCorner.Right -> {
                    newSpanX = (app.spanX + deltaSpanX).coerceAtLeast(minSize)
                    // Right edge extends naturally
                }

                ResizeCorner.Top -> {
                    newSpanY = (app.spanY - deltaSpanY).coerceAtLeast(minSize)
                    posDeltaY = deltaPosY  // Compensate position to keep top edge fixed
                }

                ResizeCorner.Bottom -> {
                    newSpanY = (app.spanY + deltaSpanY).coerceAtLeast(minSize)
                    // Bottom edge extends naturally
                }
            }

            if (snap) {
                val snapX = snapScale / cellSizePx
                val snapY = snapScale / cellSizePx
                newSpanX = (newSpanX / snapX).roundToInt() * snapX
                newSpanY = (newSpanY / snapY).roundToInt() * snapY
            }

            app.copy(
                spanX = newSpanX,
                spanY = newSpanY,
                x = app.x + posDeltaX,
                y = app.y + posDeltaY
            )
        }
    }


    fun editFloatingApp(app: FloatingAppObject) {
        val updated = _floatingApps.value.map { floatingApp ->
            if (floatingApp.id == app.id) app
            else floatingApp
        }

        _floatingApps.value = updated
    }


    enum class ResizeCorner {
        Top, Right, Left, Bottom
    }


    fun restoreFloatingApps(snapshot: List<FloatingAppObject>) {
        _floatingApps.value = snapshot.map { it.copy() }
    }

    fun resetAllFloatingApps() {
        _floatingApps.value = emptyList()

        viewModelScope.launch {
            WidgetsSettingsStore.resetAll(ctx)
        }
    }


    /* ───────────────────────────── Internal ───────────────────────────── */

    private fun updateApp(
        appId: Int,
        block: (FloatingAppObject) -> FloatingAppObject
    ) {
        val current = _floatingApps.value

        val updatedList = current.map { app ->
            if (app.id == appId) {
                block(app)
            } else {
                app
            }
        }

        _floatingApps.value = updatedList
    }

    private fun loadFloatingApps() {
        viewModelScope.launch {
            val floatingAppsJsonString = WidgetsSettingsStore.jsonSetting.get(ctx)
            _floatingApps.value = FloatingAppsJson.decodeFloatingApps(floatingAppsJsonString)
                // If null try to load legacy floating apps
                ?: LegacyFloatingAppsSettingsStore.legacyLoadFloatingApps(ctx)
        }
    }

    private fun calculateSpanX(minWidthDp: Float?): Float {
        val cellWidthDp = 100
        return ((minWidthDp ?: minSize) / cellWidthDp).coerceAtLeast(minSize)
    }

    private fun calculateSpanY(minHeightDp: Float?): Float {
        val cellHeightDp = 100
        return ((minHeightDp ?: minSize) / cellHeightDp).coerceAtLeast(minSize)
    }
}
