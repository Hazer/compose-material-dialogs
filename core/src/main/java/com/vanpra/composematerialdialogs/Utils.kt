package com.vanpra.composematerialdialogs

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.window.Dialog

@Composable
internal fun Context.getString(@StringRes res: Int? = null, default: String? = null): String {
    return if (res != null) {
        ContextAmbient.current.getString(res)
    } else default
        ?: throw IllegalArgumentException("Function must receive one non null string parameter")
}

@Composable
internal fun ThemedDialog(onCloseRequest: () -> Unit, children: @Composable () -> Unit) {
    val colors = MaterialTheme.colors
    val typography = MaterialTheme.typography

    Dialog(onDismissRequest = onCloseRequest) {
        MaterialTheme(colors = colors, typography = typography) {
            children()
        }
    }
}
