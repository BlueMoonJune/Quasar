package bluemoonjune.quasar.space

import net.minecraft.util.Identifier

class Planet(override val orbit: Orbit, override val radius: Double, val gravity: Double, override val atmosphere: Atmosphere) : Satellite, Orbitable, Atmosphere.Contains {
    override val mass: Double
        get() = radius*radius*gravity/Orbit.G
}
