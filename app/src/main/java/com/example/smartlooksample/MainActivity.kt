package com.example.smartlooksample

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.smartlooksample.ui.theme.SmartlookSampleTheme
import com.smartlook.android.core.api.Smartlook

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setupSmartlook()

		setContent {
			val context = LocalContext.current

			var grantedPermission by remember { mutableStateOf(false) }
			var showVideo by remember { mutableStateOf(false) }

			val requestPermission = rememberLauncherForActivityResult(
				contract = ActivityResultContracts.RequestPermission(),
				onResult = { grantedPermission = it }
			)

			SmartlookSampleTheme {
				LaunchedEffect(Unit) {
					if (hasCameraPermission(context)) {
						grantedPermission = true
					} else {
						requestPermission.launch(Manifest.permission.CAMERA)
					}
				}

				if (showVideo) {
					VideoDialog(
						onDismiss = { showVideo = false }
					)
				}

				if (grantedPermission) {
					Box {
						Camera()
						Button(
							onClick = { showVideo = true },
							modifier = Modifier.align(Alignment.BottomEnd)
						) {
							Text(text = "Show video")
						}
					}
				}
			}
		}
	}

	private fun setupSmartlook() {
		Smartlook.instance.preferences.projectKey = "put-your-project-key-here"
		Smartlook.instance.start()
		Smartlook.instance.user.identifier = "sample-123"
	}
}

private fun hasCameraPermission(context: Context) =
	ContextCompat.checkSelfPermission(
		context, Manifest.permission.CAMERA
	) == PackageManager.PERMISSION_GRANTED
