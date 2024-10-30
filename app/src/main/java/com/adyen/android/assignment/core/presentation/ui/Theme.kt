import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.adyen.android.assignment.core.presentation.ui.DarkColors
import com.adyen.android.assignment.core.presentation.ui.LightColors

@Composable
fun VenueDiscoveryTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

