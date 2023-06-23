package com.example.smartlooksample

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.RawResourceDataSource


@Composable
fun VideoDialog(
	onDismiss: () -> Unit,
) {
	Dialog(
		onDismissRequest = onDismiss,
	) {
		VideoView()
	}
}

@Composable
private fun VideoView() {
	val context = LocalContext.current

	val exoPlayer = ExoPlayer.Builder(context)
		.build()
		.also { exoPlayer ->
			val mediaItem = MediaItem.Builder()
				.setUri(RawResourceDataSource.buildRawResourceUri(R.raw.rainy_day))
				.build()

			exoPlayer.setMediaItem(mediaItem)
			exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
			exoPlayer.prepare()
		}

	DisposableEffect(
		AndroidView(
			factory = { context ->
				StyledPlayerView(context).apply {
					player = exoPlayer
				}
			},
			modifier = Modifier.aspectRatio(0.764f)
		)
	) {
		onDispose { exoPlayer.release() }
	}
}
