package com.adyen.android.assignment.venues.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.adyen.android.assignment.R
import com.adyen.android.assignment.core.presentation.ui.shimmerEffect
import com.adyen.android.assignment.venues.domain.Category
import com.adyen.android.assignment.venues.domain.Place
import kotlin.math.roundToInt

@Composable
fun PlaceItem(place: Place) {
    val category = place.categories.firstOrNull()
    Card(
        //colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .padding(8.dp)
            .height(328.dp)
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
            if (category != null)
                CategoryItem(category, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun PlaceInfoSection(place: Place) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        // Place name
        Text(
            text = place.name,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 16.sp,
            modifier = Modifier.height(32.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        // Location
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                tint = Color(0xFF1E88E5),
                contentDescription = "Location Icon",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = place.location?.address ?: "No address available",
                fontSize = 12.sp,
                lineHeight = 14.sp,
                maxLines = 1,
            )
        }

        // Rating
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Star,
                tint = Color(0xFFFFC107),
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
                tint = Color(0xFF4CAF50),
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
                tint = Color(0xFF009688),
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
                tint = Color(0xFF757575),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${place.distance.toFloat().div(1000).roundToInt()} KM",
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
        }
    }
}


@Composable
fun CategoryItem(category: Category, modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(bottom = 4.dp), contentAlignment = Alignment.BottomCenter) {
        Row {
            AsyncImage(
                category.icons.small,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                contentDescription = null
            )
            Text(
                text = category.name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
fun PlaceItemPlaceHolder() {
    Column(Modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .width(170.dp)
                .height(320.dp)
                .clip(RoundedCornerShape(10.dp))
                .shimmerEffect()
        )
    }
}