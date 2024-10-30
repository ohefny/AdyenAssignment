package com.adyen.android.assignment.core.presentation.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//generic alert dialog for showing error messages
@Composable
fun OneActionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    @StringRes title: Int,
    @StringRes message: Int,
    @StringRes confirmText: Int
) {
    var openDialog by remember { mutableStateOf(true) }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
                onDismiss()
            },
            title = {
                Text(
                    text = stringResource(title),
                    fontSize = 20.sp
                )
            },
            text = {
                Text(stringResource(message))
            },
            confirmButton = {
                TextButton(onClick = {
                    openDialog = false
                    onConfirm()
                }) {
                    Text(stringResource(confirmText))
                }
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}