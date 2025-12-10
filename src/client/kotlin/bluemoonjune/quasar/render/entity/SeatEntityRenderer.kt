package bluemoonjune.quasar.render.entity

import bluemoonjune.quasar.entity.SeatEntity
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory

class SeatEntityRenderer(ctx: EntityRendererFactory.Context) : EntityRenderer<SeatEntity>(ctx) {
    override fun getTexture(entity: SeatEntity?) = null

    override fun shouldRender(entity: SeatEntity?, frustum: Frustum?, x: Double, y: Double, z: Double) = true
}
