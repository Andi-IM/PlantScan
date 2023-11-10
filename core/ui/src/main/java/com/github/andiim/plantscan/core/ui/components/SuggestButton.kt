package com.github.andiim.plantscan.core.ui.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.github.andiim.plantscan.core.ui.R.string as uiString

@Composable
fun SuggestButton(onClick: () -> Unit) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle()) { append(context.getString(uiString.suggestion_button_label)) }
        append(" ")
        withStyle(
            style =
            SpanStyle(color = (MaterialTheme.colorScheme).primary, fontWeight = FontWeight.Bold),
        ) {
            append(context.getString(uiString.suggestion_action_label))
        }
    }

    ClickableText(
        text = annotatedText,
        onClick = { onClick() },
    )
}
