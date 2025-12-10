package bluemoonjune.quasar

import bluemoonjune.quasar.space.Atmosphere
import bluemoonjune.quasar.space.Orbit
import bluemoonjune.quasar.space.Orbitable
import bluemoonjune.quasar.space.Planet
import bluemoonjune.quasar.space.Star
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import java.util.HashMap
import kotlin.math.exp


class PlanetReloadListener : SimpleSynchronousResourceReloadListener {
    companion object {
        val planets = HashMap<Identifier, Orbitable>()

        fun trimId(id: Identifier) = Identifier.of(id.namespace, id.path.replace("planet/", "").replace(".json", ""))
        fun expandId(id: Identifier) = Identifier.of(id.namespace, "planet/"+id.path+".json")
    }

    override fun getFabricId(): Identifier = Quasar.id("planet")

    override fun reload(manager: ResourceManager) {
        planets.clear()
        for (id in manager.findResources(
            "planet",
            { path -> path.toString().endsWith(".json") },
            ).keys
        ) {
            loadPlanet(trimId(id), manager)
        }
        return
    }

    fun loadPlanet(id: Identifier, manager: ResourceManager): Orbitable? {
        if (planets.containsKey(id)) return planets[id]
        try {
            manager.getResource(expandId(id)).get().reader.use { stream ->
                val json = JsonHelper.deserialize(stream)
                if (json.has("parent")) {
                    val parent = loadPlanet(Identifier.of(json.get("parent").asString), manager)!!
                    planets[id] = Planet(Orbit.parseJSON(json.getAsJsonObject("orbit"), parent), json.get("radius").asDouble, json.get("gravity").asDouble, Atmosphere.parseJSON(json.getAsJsonObject("atmosphere")))
                } else {
                    planets[id] = Star(json.get("radius").asDouble, json.get("gravity").asDouble, Atmosphere.parseJSON(json.getAsJsonObject("atmosphere")))
                }
            }
        } catch (e: Exception) {
            Quasar.logger.error("Error occurred while loading resource json " + id.toString(), e)
        }
        return planets.getOrDefault(id, null)
    }
}