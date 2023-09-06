package com.github.andiim.plantscan.app.ui.common.composables

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.andiim.plantscan.app.core.analytics.LocalAnalyticsHelper
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.ui.logPlantResourceOpened
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import com.github.andiim.plantscan.app.utils.PlantPreviewParameterProvider
import com.github.andiim.plantscan.app.R.drawable as ImageDrawable

@Composable
fun PlantItem(
    plant: Plant,
    onClick: (String) -> Unit = {},
) {
    val analyticsHelper = LocalAnalyticsHelper.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clickable {
                analyticsHelper.logPlantResourceOpened(
                    plantResourceId = plant.id
                )
                onClick(plant.id)
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            AsyncImage(
                model =
                ImageRequest.Builder(LocalContext.current)
                    .data(plant.thumbnail)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(ImageDrawable.ic_error),
                contentDescription = plant.name,
                contentScale = ContentScale.Crop,
                modifier =
                Modifier
                    .clip(CircleShape)
                    .size(64.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column(modifier = Modifier.weight(2f)) {
                Text(
                    modifier = Modifier.testTag("Plant Name"),
                    text = plant.name,
                    style = (MaterialTheme.typography).titleSmall
                )
                Text(
                    modifier = Modifier.testTag("Plant Known Names"),
                    text = plant.species,
                    maxLines = 2,
                    overflow = TextOverflow.Clip,
                    style = (MaterialTheme.typography).bodyMedium
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Icon(Icons.Default.ArrowForwardIos, contentDescription = "click")
        }
    }
}

@Preview(
    name = "Night Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Day Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun Preview(
    @PreviewParameter(PlantPreviewParameterProvider::class) plant: Plant
) {
    PlantScanTheme { PlantItem(plant) }
}
