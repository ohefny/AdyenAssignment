package com.adyen.android.assignment.venues.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.adyen.android.assignment.R
import com.adyen.android.assignment.venues.domain.Category
import com.adyen.android.assignment.venues.domain.Place

@Composable
fun PlaceItem(place: Place) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(320.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier) {
            //image of 64x64 for the place
            AsyncImage(
                model = place.photos.firstOrNull()?.small,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.size(180.dp)
            )
            PlaceInfoSection(place)
            //categories section contains a list of categories of image and name
            CategoriesSection(place.categories)
        }
    }
}

@Composable
private fun PlaceInfoSection(place: Place) {
    Column(modifier = Modifier.padding(8.dp)) {
        // Place name
        Text(
            text = place.name,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 16.sp
        )

        // Location
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = "Location Icon",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = place.location?.address ?: "No address available",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }

        // Rating
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Rating Icon",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = place.rating.toString(),
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }

        // Verified
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Verified Icon",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (place.verified == true) "Verified" else "Not Verified",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }

        // Price Level
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_outline_payments_24),
                contentDescription = "Price Icon",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = place.price?.name ?: "Price not available",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }

        // Distance
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_outline_directions_24),
                contentDescription = "Distance Icon",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${place.distance} Meters",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }
    }
}

//categories section contains a list of categories of image and name
@Composable
fun CategoriesSection(categories: List<Category>) {
    categories.take(1).forEach {
        CategoryItem(it)
    }
}

@Composable
fun CategoryItem(category: Category) {
    Row {
        AsyncImage(category.icons.small, contentDescription = null)
        Text(
            text = category.name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}