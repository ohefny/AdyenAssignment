package com.adyen.android.assignment.venues.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.adyen.android.assignment.core.miscs.Consumer
import com.adyen.android.assignment.venues.domain.Category
import com.adyen.android.assignment.venues.presentation.model.CategoryUIState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryFilter(
    categories: List<CategoryUIState>,
    onCategorySelected: Consumer<CategoryUIState>,
) {
    val listState = rememberLazyListState()
    val selectedCategoriesSize = remember(categories) {
        mutableIntStateOf(categories.count { it.isSelected })
    }
    LaunchedEffect(selectedCategoriesSize) {
        listState.animateScrollToItem(0) // Scroll to the first item
    }
    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories, key = {it.name}) { category ->
            //val animatedModifier = Modifier.animateItemPlacement(animationSpec = tween(durationMillis = 300)) // Animation duration

            CategoryChip(
                category = category,
                onSelected = {
                    onCategorySelected(category)
                    },
                isSelected = category.isSelected,
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
fun CategoryChip(
    category: CategoryUIState,
    onSelected: (Category) -> Unit,
    isSelected: Boolean ,
    modifier: Modifier = Modifier
) {
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val textColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .clickable { onSelected(category.category) }
            .padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            AsyncImage(
                model = category.category.icons.small,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )
            Text(
                text = category.category.name + " (${category.count})",
                color = textColor,
            )
        }

    }
}
