package com.adyen.android.assignment.venues.presentation.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adyen.android.assignment.R
import com.adyen.android.assignment.core.miscs.Action
import com.adyen.android.assignment.venues.domain.SortOption


@Composable
fun SortOptions(
    selectedOption: SortOption? = null,
    onOptionSelected: (SortOption) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(32.dp).padding(horizontal = 16.dp)
    ) {
        //bold sortBy icon and text
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight()) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_sort_24),
                contentDescription = "Sort Icon",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Sort By:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
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
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        onClick = onOptionSelected,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(text = label)
    }
}
