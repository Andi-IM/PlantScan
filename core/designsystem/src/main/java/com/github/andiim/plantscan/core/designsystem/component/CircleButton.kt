package com.github.andiim.plantscan.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons
import com.github.andiim.plantscan.core.designsystem.theme.PsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        shape = CircleShape,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
    ) {
        Icon(
            imageVector,
            contentDescription = contentDescription,
            modifier = modifier
                .padding(30.dp)
                .shadow(8.dp, shape = CircleShape),
        )
    }
}

@ThemePreviews
@Composable
fun ButtonPreview() {
    PsTheme {
        Surface() {
            Box(modifier = Modifier.padding(16.dp)) {
                RoundIconButton(
                    imageVector = PsIcons.Camera,
                    contentDescription = "Testing",
                    onClick = {},
                )
            }
        }
    }
}
