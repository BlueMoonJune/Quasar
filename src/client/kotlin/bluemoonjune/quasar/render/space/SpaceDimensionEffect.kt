package bluemoonjune.quasar.render.space

import net.minecraft.client.render.DimensionEffects
import net.minecraft.util.math.Vec3d

class SpaceDimensionEffect(
    cloudsHeight: Float,
    alternateSkyColor: Boolean,
    skyType: SkyType,
    brightenLighting: Boolean,
    darkened: Boolean
) : DimensionEffects(cloudsHeight, alternateSkyColor, skyType, brightenLighting, darkened) {
    override fun adjustFogColor(
        color: Vec3d?,
        sunHeight: Float
    ): Vec3d? = color

    override fun useThickFog(camX: Int, camY: Int): Boolean = false
}