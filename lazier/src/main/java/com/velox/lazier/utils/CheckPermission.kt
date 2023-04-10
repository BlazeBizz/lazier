package com.velox.lazier.utils

import android.content.Context
import android.content.pm.PackageManager


fun String.checkForPermission(context: Context): Boolean = try {
    context.packageManager.checkPermission(
        this, context.packageName
    ) == PackageManager.PERMISSION_GRANTED
} catch (e: Exception) {
    false
}


/*
fun ComposablePermissionRequest(
    permission: String,
    onGranted: () -> Unit = {},
    onNotGranted: () -> Unit = {},
    onAlreadyGranted: () -> Unit = {},
) {
    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onGranted()
        } else {
            // permission not granted, handle the error
            onNotGranted()
        }
    }

//    val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE // replace with the permission you want to request

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context, permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // permission already granted, perform the action
            onAlreadyGranted()
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }
}*/
