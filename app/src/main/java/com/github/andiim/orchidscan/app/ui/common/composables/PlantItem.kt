package com.github.andiim.orchidscan.app.ui.common.composables

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.andiim.orchidscan.app.data.model.Image
import com.github.andiim.orchidscan.app.data.model.Plant
import com.github.andiim.orchidscan.app.ui.theme.PlantScanTheme

@Composable
fun PlantItem(plant: Plant, onClick: (Plant) -> Unit = {}) {
  Card(modifier = Modifier.fillMaxWidth().height(96.dp).clickable { onClick.invoke(plant) }) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround) {
//          AsyncImage(
//              model =
//                  ImageRequest.Builder(LocalContext.current)
//                      .data(plant.images[0].file)
//                      .crossfade(true)
//                      .build(),
//              placeholder = painterResource(ImageDrawable.ic_error),
//              contentDescription = plant.name,
//              contentScale = ContentScale.Crop,
//              modifier = Modifier.clip(CircleShape).size(64.dp).align(Alignment.CenterVertically))
          Spacer(modifier = Modifier.size(8.dp))
          PlantContent(modifier = Modifier.weight(2f), plant = plant)
          Spacer(modifier = Modifier.size(8.dp))
          Icon(Icons.Default.ArrowForwardIos, contentDescription = "click")
        }
  }
}

@Composable
private fun PlantContent(modifier: Modifier = Modifier, plant: Plant) {
  Column(modifier = modifier) {
    Text(text = plant.name, style = (MaterialTheme.typography).titleSmall)
    KnownNames(names = plant.commonName)
  }
}

@Composable
private fun KnownNames(names: List<String>) {
  val nameString = names.joinToString(", ")
  Text(
      text = nameString,
      maxLines = 2,
      overflow = TextOverflow.Clip,
      style = (MaterialTheme.typography).bodyMedium)
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
private fun Preview() {
  val plant =
      Plant(
          id = "1",
          name = "Bird of Paradise",
          species = "Strelizia reginae",
          type = "bird",
          images =
              listOf(
                  Image(
                      attribution = "Creative Common",
                      name = "Orchid",
                      file =
                          "https://upload.wikimedia.org/wikipedia/commons/3/30/Orchid_Phalaenopsis_hybrid.jpg")),
          commonName = listOf("Strelizia reginae", "Crane flower", "Bird of Paradise"),
      )
  PlantScanTheme { PlantItem(plant) }
}
