package com.github.andiim.plantscan.uitesthiltmanifest

import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * A [ComponentActivity] annotated with [AndroidEntryPoint] for use in tests.
 */
@AndroidEntryPoint
class HiltComponentActivity : ComponentActivity()
