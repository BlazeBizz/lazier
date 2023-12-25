package com.velox.lazeir.utils.installer

import android.content.Context
import android.net.Uri

interface InstallerInterface {

    fun installApk(context: Context, uri: Uri, onError: (String) -> Unit = {})

    fun installApk(context: Context, fileUri: String, packageName:String, onError: (String) -> Unit = {})

    fun unInstallApk(context: Context, packageName: String, onError: (String) -> Unit = {})

    fun isAppInstalled(context: Context, packageName: String): Boolean
}