package bluemoonjune.quasar.space

class Star(override val radius: Double, val gravity: Double, override val atmosphere: Atmosphere) : Orbitable, Atmosphere.Contains {
    override val mass: Double
        get() = radius*radius*gravity/Orbit.G
}
