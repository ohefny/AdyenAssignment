package com.adyen.android.assignment.venues.presentation.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adyen.android.assignment.core.miscs.Action
import com.adyen.android.assignment.venues.domain.SortOption


@Composable
fun SortOptions(
    selectedOption: SortOption? = null,
    onOptionSelected: (SortOption) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        // Price Sort Option
        SortButton(
            onOptionSelected = {
                onOptionSelected(SortOption.PRICE)
            },
            isSelected = selectedOption == SortOption.PRICE,
            label = "Price"
        )
        SortButton(
            onOptionSelected = {
                onOptionSelected(SortOption.DISTANCE)
            },
            isSelected = selectedOption == SortOption.DISTANCE,
            label = "Distance"
        )
        SortButton(
            onOptionSelected = {
                onOptionSelected(SortOption.RATING)
            },
            isSelected = selectedOption == SortOption.RATING,
            label = "Rating"
        )
    }
}

@Composable
private fun SortButton(
    onOptionSelected: Action,
    isSelected: Boolean,
    label: String
) {
    OutlinedButton(
        onClick = onOptionSelected,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(text = label)
    }
}
