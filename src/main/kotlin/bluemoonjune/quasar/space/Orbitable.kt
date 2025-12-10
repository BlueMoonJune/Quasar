package bluemoonjune.quasar.space

import net.minecraft.util.Identifier

//Satellites can orbit these
interface Orbitable {
    val mass : Double
    val radius: Double
}