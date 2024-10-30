package com.adyen.android.assignment.core.presentation.ui
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Define the main color tokens
val PrimaryColor = Color(0xFF6BC8C8)          // Bright teal
val OnPrimaryColor = Color.White
val PrimaryContainer = Color(0xFFB2EBEB)      // Light teal for containers
val OnPrimaryContainer = Color(0xFF003333)

val SecondaryColor = Color(0xFFF8B400)        // Bright yellow
val OnSecondaryColor = Color.Black
val SecondaryContainer = Color(0xFFFFE082)    // Lighter yellow for containers
val OnSecondaryContainer = Color(0xFF332100)

val BackgroundColor = Color(0xFFF9F7F7)       // Light grey
val OnBackgroundColor = Color(0xFF333333)
val SurfaceColor = Color.White
val OnSurfaceColor = Color(0xFF333333)

val ErrorColor = Color(0xFFE57373)
val OnErrorColor = Color.White
val OutlineColor = Color(0xFFB0BEC5)          // Grey for outlines

// Light color scheme
val LightColors = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryColor,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = SecondaryColor,
    onSecondary = OnSecondaryColor,
    surfaceContainer = PrimaryContainer,
    surfaceVariant = PrimaryContainer,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    background = BackgroundColor,
    onBackground = OnBackgroundColor,
    surface = SurfaceColor,
    onSurface = OnSurfaceColor,
    error = ErrorColor,
    onError = OnErrorColor,
    outline = OutlineColor,
)

val DarkColors = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = OnPrimaryColor,
    primaryContainer = Color(0xFF005151), // Deeper teal for contrast
    onPrimaryContainer = Color(0xFFB2EBEB),
    secondary = SecondaryColor,
    onSecondary = OnSecondaryColor,
    secondaryContainer = Color(0xFF4A3E00), // Darkened yellow
    onSecondaryContainer = SecondaryContainer,
    background = Color(0xFF121212), // Dark gray background
    onBackground = Color(0xFFE0E0E0), // Light gray for readability
    surface = Color(0xFF1E1E1E), // Slightly lighter surface than background
    onSurface = Color(0xFFD0D0D0), // Light gray on dark for readability

    error = ErrorColor,
    onError = OnErrorColor,
    outline = Color(0xFF78909C) // Slightly lighter outline for subtle contrast
)
