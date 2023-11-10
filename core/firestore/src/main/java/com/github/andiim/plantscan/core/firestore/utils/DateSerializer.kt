package com.github.andiim.plantscan.core.firestore.utils

import android.util.Log
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.util.Date

object DateSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("java.util.Date", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): Date {
        val decode = decoder.decodeString()
        Log.d("TAG", "deserialize: $decode")
        return Date.from(Instant.parse(decode))
    }
    override fun serialize(encoder: Encoder, value: Date) =
        encoder.encodeString(value.toInstant().toString())
}

@Serializable
class DateJson(
    val seconds: Long,
    val nanoseconds: Long,
)
