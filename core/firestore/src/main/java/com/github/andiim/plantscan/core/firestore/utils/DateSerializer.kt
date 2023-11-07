package com.github.andiim.plantscan.core.firestore.utils

import kotlinx.serialization.KSerializer
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
    override fun deserialize(decoder: Decoder): Date =
        Date.from(Instant.parse(decoder.decodeString()))
    override fun serialize(encoder: Encoder, value: Date) =
        encoder.encodeString(value.toInstant().toString())
}
