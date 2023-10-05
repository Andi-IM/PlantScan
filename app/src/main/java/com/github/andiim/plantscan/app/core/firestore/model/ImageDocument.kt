package com.github.andiim.plantscan.app.core.firestore.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ImageDocument(
    val url: String = "",
    @ServerTimestamp val date: Date? = null,
    val attribution: String? = "",
    @Exclude val id: Long? = null,
) {
    @get:PropertyName("desc")
    @set:PropertyName("desc")
    var description: String = ""
}
