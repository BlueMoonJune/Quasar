package bluemoonjune.quasar.render.space

import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.loader.impl.lib.sat4j.core.Vec
import net.minecraft.client.render.*
import net.minecraft.client.render.VertexFormat.DrawMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3i
import net.minecraft.util.math.random.Random
import org.joml.AxisAngle4f
import org.joml.Quaternionf
import org.joml.Vector3f


private val Vec3i.vector3f: Vector3f
    get() = Vector3f(x.toFloat(), y.toFloat(), z.toFloat())

class SpaceSkyRenderer : DimensionRenderingRegistry.SkyRenderer {
    val STAR_LAYER: RenderLayer = RenderLayer.of(
        "quasar:stars", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS,
        786432, RenderLayer.MultiPhaseParameters.builder()
            .program(RenderPhase.COLOR_PROGRAM)
            .transparency(RenderPhase.NO_TRANSPARENCY)
            .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
            .cull(RenderPhase.ENABLE_CULLING)
            .build(false)
    )

    val ATMOSPHERE_LAYER: RenderLayer = RenderLayer.of(
        "quasar:atmosphere", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS,
        786432, RenderLayer.MultiPhaseParameters.builder()
            .program(RenderPhase.COLOR_PROGRAM)
            .transparency(RenderPhase.ADDITIVE_TRANSPARENCY)
            .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
            .cull(RenderPhase.ENABLE_CULLING)
            .build(false)
    )

    override fun render(context: WorldRenderContext) {
        val bufs = context.consumers()!!
        var buf : VertexConsumer

        val mats = context.matrixStack() ?: MatrixStack()
        val time = context.world().time + context.tickCounter().getTickDelta(false)


        mats.push()
        buf = bufs.getBuffer(STAR_LAYER)

        val random = Random.create(10842L)
        val i = 1500
        val f = 500.0f


        //mats.multiply(Quaternionf(AxisAngle4f(time / 100f, 1f, 0f, 0f)))

        val mat = mats.peek()

        for (j in 0..<i) {
            val g = random.nextFloat() * 2.0f - 1.0f
            val h = random.nextFloat() * 2.0f - 1.0f
            val k = random.nextFloat() * 2.0f - 1.0f
            val l = 0.15f + random.nextFloat() * 0.1f
            val m = MathHelper.magnitude(g, h, k)
            if (!(m <= 0.010000001f) && !(m >= 1.0f)) {
                val vector3f = (Vector3f(-k, h, g)).normalize(f)
                val n = (random.nextDouble() * Math.PI.toFloat().toDouble() * 2.0).toFloat()
                val quaternionf = (Quaternionf()).rotateTo(Vector3f(0.0f, 0.0f, -1.0f), vector3f).rotateZ(n)
                drawSquare(mat, buf, quaternionf, l * 5, -f, 1f, 1f, 1f, 1f)
            }
        }
        mats.pop()

        bufs.end

        mats.push()
        mats.translate(50f, -30f, 50f)
        buf = bufs.getBuffer(ATMOSPHERE_LAYER)
        for (i in 1..5) {
            for (dir in Direction.entries) {
                drawSquare(
                    mats.peek(),
                    buf,
                    Quaternionf().rotateTo(Vector3f(0f, 0f, 1f), dir.vector.vector3f),
                    13-i.toFloat()/2,
                    i.toFloat()/2-13,
                    Math.clamp(0.04f, 0f, 1f),
                    Math.clamp(0.16f, 0f, 1f),
                    Math.clamp(0.4f, 0f, 1f),
                    1f
                )
            }
        }
        mats.pop()
    }

    fun drawSquare(
        mat: MatrixStack.Entry, buf: VertexConsumer, quaternionf: Quaternionf,
        l: Float, z: Float, r: Float, g: Float, b: Float, a: Float
    ) {
        buf.vertex(mat, (Vector3f(l, -l, z).rotate(quaternionf))).color(r, g, b, a)
        buf.vertex(mat, (Vector3f(l, l, z).rotate(quaternionf))).color(r, g, b, a)
        buf.vertex(mat, (Vector3f(-l, l, z).rotate(quaternionf))).color(r, g, b, a)
        buf.vertex(mat, (Vector3f(-l, -l, z).rotate(quaternionf))).color(r, g, b, a)
    }
}