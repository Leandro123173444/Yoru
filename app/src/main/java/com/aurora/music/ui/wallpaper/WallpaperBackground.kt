package com.aurora.music.ui.wallpaper

import android.net.Uri
import android.view.TextureView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.aurora.music.data.UiPrefs
import com.aurora.music.data.WallpaperMode

/**
 * Custom player wallpaper: still image or looping video, with a dim overlay so UI stays readable.
 */
@Composable
fun WallpaperBackground(
    prefs: UiPrefs,
    modifier: Modifier = Modifier,
) {
    when (prefs.wallpaperMode) {
        WallpaperMode.IMAGE -> if (prefs.wallpaperImage.isNotBlank()) {
            ImageWallpaper(prefs.wallpaperImage, prefs.wallpaperDim, modifier)
        }
        WallpaperMode.VIDEO -> if (prefs.wallpaperVideo.isNotBlank()) {
            VideoWallpaper(prefs.wallpaperVideo, prefs.wallpaperDim, modifier)
        }
        else -> Unit
    }
}

@Composable
private fun ImageWallpaper(uri: String, dim: Float, modifier: Modifier) {
    Box(modifier.fillMaxSize()) {
        AsyncImage(
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        DimOverlay(dim)
    }
}

@Composable
private fun VideoWallpaper(uri: String, dim: Float, modifier: Modifier) {
    val context = LocalContext.current
    val owner = LocalLifecycleOwner.current
    val player = remember(uri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(uri)))
            repeatMode = Player.REPEAT_MODE_ONE
            volume = 0f
            prepare()
            playWhenReady = true
        }
    }
    val texture = remember(context) { TextureView(context) }

    DisposableEffect(player, texture, owner) {
        fun attach() {
            player.setVideoTextureView(texture)
            if (owner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                player.playWhenReady = true
            }
        }
        fun detach() {
            player.clearVideoTextureView(texture)
            player.playWhenReady = false
        }
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> attach()
                Lifecycle.Event.ON_STOP -> detach()
                else -> Unit
            }
        }
        owner.lifecycle.addObserver(observer)
        if (owner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) attach()
        onDispose {
            owner.lifecycle.removeObserver(observer)
            detach()
            player.release()
        }
    }

    Box(modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(factory = { texture }, modifier = Modifier.fillMaxSize())
        DimOverlay(dim)
    }
}

@Composable
private fun DimOverlay(dim: Float) {
    val alpha = dim.coerceIn(0f, 0.9f)
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black.copy(alpha = alpha * 0.85f),
                        Color.Black.copy(alpha = alpha * 0.55f),
                        Color.Black.copy(alpha = alpha * 0.9f),
                    )
                )
            )
    )
}

fun UiPrefs.hasCustomWallpaper(): Boolean = when (wallpaperMode) {
    WallpaperMode.IMAGE -> wallpaperImage.isNotBlank()
    WallpaperMode.VIDEO -> wallpaperVideo.isNotBlank()
    else -> false
}
