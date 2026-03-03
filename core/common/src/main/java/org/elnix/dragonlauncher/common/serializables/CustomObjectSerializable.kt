package org.elnix.dragonlauncher.common.serializables

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


data class CustomObjectSerializable(
    val stroke: Float? = null,
    val color: Color? = null,
    val glow: CustomGlow? = null,
    val radius: Float? = null
)

@Serializable
data class CustomGlow(
    val type: GlowType,
    val color: Color,
    val radius: Float
)

@Serializable
enum class GlowType {
    Radial, Linear
}

// Custom Color serializer (simplest approach - store as ARGB)
@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Color::class)
object ColorSerializer : KSerializer<Color> {
    override val descriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeInt(value.toArgb()) // ARGB as Long
    }

    override fun deserialize(decoder: Decoder): Color {
        return Color(decoder.decodeInt())
    }
}



//// In your Json config
//val json = Json {
//    serializersModule = SerializersModule {
//        contextual(Color::class, ColorSerializer)
//        contextual(Brush::class, BrushSerializer)
//    }
//}
//
//// Now it works!
//val obj = CustomObjectSerializable(
//    color = CustomColorSerializable.Plain(Color.Blue),
//    glow = CustomGlow(GlowType.Radial, CustomColorSerializable.Plain(Color.Red), 10f)
//)
//val jsonString = json.encodeToString(obj)
