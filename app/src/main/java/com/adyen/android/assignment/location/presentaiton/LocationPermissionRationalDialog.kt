package com.adyen.android.assignment.location.presentaiton

import androidx.compose.runtime.Composable
import com.adyen.android.assignment.R
import com.adyen.android.assignment.core.presentation.ui.components.OneActionDialog

@Composable
fun LocationPermissionRationalDialog(
    onDismiss: () -> Unit,
    onAllowClicked: () -> Unit,
    canRequestPermission: Boolean = false
) {
    OneActionDialog(
        onDismiss = onDismiss,
        onConfirm = onAllowClicked,
        title = R.string.msg_location_permission,
        message = R.string.lbl_enable_location,
        confirmText = if (canRequestPermission) {
            R.string.lbl_enable_location
        } else {
            R.string.lbl_go_to_settings
        }
    )
}


