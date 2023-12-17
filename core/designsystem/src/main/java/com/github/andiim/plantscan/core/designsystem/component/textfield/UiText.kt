package com.github.andiim.plantscan.core.designsystem.component.textfield

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any,
    ) : UiText()

//    @Composable
//    fun asString(): String {
//        return when (this) {
//            is DynamicString -> value
//            is StringResource -> stringResource(id = resId, args)
//        }
//    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, args)
        }
    }
}
