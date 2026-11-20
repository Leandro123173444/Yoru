package com.aurora.music.ui.screens.settings

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.aurora.music.data.UiPrefs
import com.aurora.music.data.WallpaperMode
import com.aurora.music.data.settings.AppearanceSettings
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun WallpaperSettingsSection(prefs: UiPrefs, store: AppearanceSettings) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        runCatching {
            context.contentResolver.takePersistableUriPermission(
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        scope.launch {
            store.setWallpaperImage(uri.toString())
            store.setWallpaperMode(WallpaperMode.IMAGE)
        }
    }
    val pickVideo = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        runCatching {
            context.contentResolver.takePersistableUriPermission(
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        scope.launch {
            store.setWallpaperVideo(uri.toString())
            store.setWallpaperMode(WallpaperMode.VIDEO)
        }
    }

    SettingsSectionTitle("Fondo de pantalla")
    SettingsGroup {
        SegmentedRow(
            "Tipo",
            listOf("Ninguno", "Imagen", "Video"),
            prefs.wallpaperMode,
        ) { i ->
            scope.launch { store.setWallpaperMode(i) }
        }
    }

    when (prefs.wallpaperMode) {
        WallpaperMode.IMAGE -> {
            SettingsGroup {
                SettingsNavRow(
                    Icons.Outlined.Image,
                    "Elegir imagen",
                    value = if (prefs.wallpaperImage.isBlank()) "Sin imagen" else "Imagen seleccionada",
                ) {
                    pickImage.launch(arrayOf("image/*"))
                }
            }
        }
        WallpaperMode.VIDEO -> {
            SettingsGroup {
                SettingsNavRow(
                    Icons.Outlined.Videocam,
                    "Elegir video",
                    value = if (prefs.wallpaperVideo.isBlank()) "Sin video" else "Video seleccionado",
                ) {
                    pickVideo.launch(arrayOf("video/*"))
                }
            }
        }
    }

    if (prefs.wallpaperMode != WallpaperMode.NONE) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp)) {
            SettingsSliderRow(
                title = "Oscurecer fondo",
                valueLabel = "${(prefs.wallpaperDim * 100).roundToInt()}%",
                value = prefs.wallpaperDim,
                range = 0.1f..0.85f,
            ) { v ->
                scope.launch { store.setWallpaperDim(v) }
            }
            Text(
                "Se aplica en inicio, biblioteca, ajustes y el resto. El reproductor de música queda aparte, más transparente.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
            )
        }
        if ((prefs.wallpaperMode == WallpaperMode.IMAGE && prefs.wallpaperImage.isBlank()) ||
            (prefs.wallpaperMode == WallpaperMode.VIDEO && prefs.wallpaperVideo.isBlank())
        ) {
            Text(
                "Elegí un archivo para activar el fondo personalizado.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            )
        }
    }
}
