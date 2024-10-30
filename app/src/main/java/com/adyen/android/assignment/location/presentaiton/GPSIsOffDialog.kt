package com.adyen.android.assignment.location.presentaiton
import androidx.compose.runtime.Composable
import com.adyen.android.assignment.R
import com.adyen.android.assignment.core.presentation.ui.components.OneActionDialog

@Composable
fun GPSIsOffDialog(
    onDismiss: () -> Unit,
    onAllowClicked: () -> Unit
) {
    OneActionDialog(
        onDismiss = onDismiss,
        onConfirm = onAllowClicked,
        title = R.string.title_gps_is_off,
        message = R.string.msg_gps_is_off,
        confirmText = R.string.lbl_go_to_settings
    )
}