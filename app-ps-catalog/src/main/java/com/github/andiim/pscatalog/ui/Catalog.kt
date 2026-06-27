package com.github.andiim.pscatalog.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.andiim.plantscan.core.designsystem.component.PsButton
import com.github.andiim.plantscan.core.designsystem.component.PsOutlinedButton
import com.github.andiim.plantscan.core.designsystem.component.PsTextButton
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons
import com.github.andiim.plantscan.core.designsystem.theme.PsTheme

@OptIn(ExperimentalLayoutApi::class)
@Suppress("LongMethod")
@Composable
fun PsCatalog(modifier: Modifier = Modifier) {
    PsTheme {
        Surface(modifier = modifier.fillMaxSize()) {
            val contentPadding = WindowInsets
                .systemBars
                .add(WindowInsets(16.dp))
                .asPaddingValues()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    Text(
                        text = "Plantscan Catalog",
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
                item { Text("Buttons", Modifier.padding(top = 16.dp)) }
                item {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PsButton(onClick = { /* no-op */ }) {
                            Text(text = "Enabled")
                        }
                        PsOutlinedButton(onClick = { /* no-op */ }) {
                            Text(text = "Enabled")
                        }
                        PsTextButton(onClick = { /*no-op*/ }) {
                            Text(text = "Enabled")
                        }
                    }
                }
                item { Text("Disabled Buttons", Modifier.padding(top = 16.dp)) }
                item {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PsButton(
                            onClick = { /* no-op */ },
                            enabled = false,
                        ) {
                            Text(text = "Enabled")
                        }
                        PsOutlinedButton(
                            onClick = { /* no-op */ },
                            enabled = false,
                        ) {
                            Text(text = "Enabled")
                        }
                        PsTextButton(
                            onClick = { /* no-op */ },
                            enabled = false,
                        ) {
                            Text(text = "Enabled")
                        }
                    }
                }
                item { Text("Buttons with leading icons", Modifier.padding(top = 16.dp)) }
                item {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PsButton(
                            onClick = { /* no-op */ },
                            text = { Text(text = "Home") },
                            leadingIcon = {
                                Icon(imageVector = PsIcons.Home, contentDescription = null)
                            },
                        )
                        PsOutlinedButton(
                            onClick = { /* no-op */ },
                            text = { Text(text = "Home") },
                            leadingIcon = {
                                Icon(imageVector = PsIcons.Home, contentDescription = null)
                            },
                        )
                        PsTextButton(
                            onClick = { /* no-op */ },
                            text = { Text(text = "Home") },
                            leadingIcon = {
                                Icon(imageVector = PsIcons.Home, contentDescription = null)
                            }
                        )
                    }
                }
                item { Text("Disabled Buttons with leading icons", Modifier.padding(top = 16.dp)) }
                item {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        PsButton(
                            onClick = { /* no-op */ },
                            enabled = false,
                            text = { Text(text = "Home") },
                            leadingIcon = {
                                Icon(imageVector = PsIcons.Home, contentDescription = null)
                            },
                        )
                        PsOutlinedButton(
                            onClick = { /* no-op */ },
                            enabled = false,
                            text = { Text(text = "Home") },
                            leadingIcon = {
                                Icon(imageVector = PsIcons.Home, contentDescription = null)
                            },
                        )
                        PsTextButton(
                            onClick = { /* no-op */ },
                            enabled = false,
                            text = { Text(text = "Home") },
                            leadingIcon = {
                                Icon(imageVector = PsIcons.Home, contentDescription = null)
                            }
                        )
                    }
                }
            }
        }
    }
}
