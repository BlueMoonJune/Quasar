package bluemoonjune.quasar.space

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.joml.Quaterniond
import org.joml.Vector3f

class Atmosphere(val layers: Int, val height: Double, val color: Vector3f) {
    companion object {
        fun parseJSON(obj: JsonObject) = Atmosphere(
            obj.get("layers").asInt,
            obj.get("height").asDouble,
            obj.getAsJsonArray("color").map(JsonElement::getAsFloat).let { c ->
                Vector3f(c[0], c[1], c[2])
            }
        )
    }

    interface Contains {
        val atmosphere: Atmosphere
    }
}