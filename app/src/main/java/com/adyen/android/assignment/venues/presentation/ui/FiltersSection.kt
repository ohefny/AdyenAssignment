package com.adyen.android.assignment.venues.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adyen.android.assignment.R


@Composable
fun FiltersSection(
    openNowChecked: Boolean,
    onOpenNowCheckedChange: (Boolean) -> Unit,
    verifiedChecked: Boolean,
    onVerifiedCheckedChange: (Boolean) -> Unit
) {
    Row(Modifier.padding(horizontal = 16.dp)) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_filter_24),
            contentDescription = "Filter Icon",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = "Filter By:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FilterCheckbox(
            checked = openNowChecked,
            onCheckedChange = onOpenNowCheckedChange,
            label = "Open Now"
        )
        FilterCheckbox(
            checked = verifiedChecked,
            onCheckedChange = onVerifiedCheckedChange,
            label = "Verified"
        )
    }
}


@Composable
fun FilterCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String
) {
    Row(
        modifier = Modifier
            .toggleable(
                value = checked,
                onValueChange = { onCheckedChange(!checked) },
                role = Role.Checkbox
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,  // Set to null to prevent double handling of clicks
            colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.primary)
        )
        Text(text = label, fontSize = 14.sp)
    }
}
