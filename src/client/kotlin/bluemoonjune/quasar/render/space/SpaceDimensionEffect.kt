package bluemoonjune.quasar.render.space

import net.fabricmc.loader.impl.lib.sat4j.core.Vec
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
    ): Vec3d? = Vec3d.ZERO

    override fun useThickFog(camX: Int, camY: Int): Boolean = false

    override fun getFogColorOverride(skyAngle: Float, tickDelta: Float): FloatArray {
        return arrayOf( 0f, 0f, 0f, 0f ).toFloatArray()
    }
}