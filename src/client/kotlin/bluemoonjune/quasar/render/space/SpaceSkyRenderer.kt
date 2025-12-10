package bluemoonjune.quasar.render.space

import bluemoonjune.quasar.PlanetReloadListener
import bluemoonjune.quasar.Quasar
import bluemoonjune.quasar.space.Atmosphere
import bluemoonjune.quasar.space.Orbit
import bluemoonjune.quasar.space.Planet
import bluemoonjune.quasar.space.Satellite
import bluemoonjune.quasar.space.Star
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.loader.impl.lib.sat4j.core.Vec
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.*
import net.minecraft.client.render.VertexFormat.DrawMode
import net.minecraft.client.render.block.BlockModelRenderer
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3i
import net.minecraft.util.math.random.Random
import net.minecraft.util.profiler.ProfilerSystem
import org.joml.AxisAngle4f
import org.joml.Quaterniond
import org.joml.Quaternionf
import org.joml.Vector3d
import org.joml.Vector3f
import kotlin.math.PI
import kotlin.math.log
import kotlin.math.log10


private val Vec3i.vector3f: Vector3f
    get() = Vector3f(x.toFloat(), y.toFloat(), z.toFloat())

class SpaceSkyRenderer : DimensionRenderingRegistry.SkyRenderer {
    companion object {
        val STAR_LAYER: RenderLayer = RenderLayer.of(
            "quasarstars", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS,
            786432, RenderLayer.MultiPhaseParameters.builder()
                .program(RenderPhase.COLOR_PROGRAM)
                .transparency(RenderPhase.NO_TRANSPARENCY)
                .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
                .cull(RenderPhase.ENABLE_CULLING)
                .build(false)
        )

        val ATMOSPHERE_LAYER: RenderLayer = RenderLayer.of(
            "quasaratmosphere", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS,
            786432, RenderLayer.MultiPhaseParameters.builder()
                .program(RenderPhase.COLOR_PROGRAM)
                .transparency(RenderPhase.ADDITIVE_TRANSPARENCY)
                .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
                .cull(RenderPhase.ENABLE_CULLING)
                .build(false)
        )
    }

    override fun render(context: WorldRenderContext) {

        val myOrbit = Orbit(
            0.0,
            1300000.0,
            Quaterniond().rotateX(PI/4),
            0.0,
            PlanetReloadListener.planets.get(Quasar.id("overworld")) ?: return
        )

        val time = context.world().timeOfDay + context.tickCounter().getTickDelta(false)
        val rot = Quaternionf(AxisAngle4f(0f, Vector3f(0f, 1f, 1f).normalize()))

        val bufs = context.consumers()!!
        var buf : VertexConsumer

        val mats = context.matrixStack()
            ?:
            MatrixStack()

        val pos = context.camera().pos
        //mats.translate(-pos.x, -pos.y, -pos.z)

        mats.push()

        mats.push()

        //mats.multiply(context.camera().rotation.invert()) //TODO: GET RID OF THIS!!!

        mats.multiply(rot)

        buf = bufs.getBuffer(ATMOSPHERE_LAYER)

        val random = Random.create(10842L)
        val rollRandom = Random.create(12314L)
        val i = 1500
        val f = 500.0f


        val mat = mats.peek()

        for (j in 0..<i) {
            val g = random.nextFloat() * 2.0f - 1.0f
            val h = random.nextFloat() * 2.0f - 1.0f
            val k = random.nextFloat() * 2.0f - 1.0f
            val l = 0.15f + random.nextFloat() * 0.1f
            val m = MathHelper.magnitude(g, h, k)
            if (!(m <= 0.010000001f) && !(m >= 1.0f)) {
                val vector3f = (Vector3f(-k, h, g)).normalize(f)
                val n = (random.nextDouble() * Math.PI.toFloat().toDouble() * 2.0+time/20.0*(rollRandom.nextDouble()-0.5)).toFloat()
                val quaternionf = (Quaternionf()).rotateTo(Vector3f(0.0f, 0.0f, -1.0f), vector3f).rotateZ(n)
                drawSquare(mat, buf, quaternionf, l * 5, -f, 1f, 1f, 1f, 1f)
            }
        }
        mats.pop()
        mats.push()
        val opos = myOrbit.computePosition(time/20.0)

        for (planet in PlanetReloadListener.planets.keys) {
            renderPlanet(mats, bufs, planet, time.toDouble()/20, opos)
        }

        mats.pop()
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

    fun renderPlanet(mats: MatrixStack, bufs: VertexConsumerProvider, id: Identifier, time: Double, camera: Vector3d) {
        mats.push()

        val planet = PlanetReloadListener.planets[id] ?: return
        val pos: Vector3d
        if (planet is Satellite) {
            pos = planet.orbit.computePosition(time)
        } else {
            pos = Vector3d(0.0, 0.0, 0.0)
        }
        pos.sub(camera)
        val len = (pos.length()).toFloat()
        val scale = 512*(1/len-1/len/len)
        mats.scale(scale, scale, scale)
        mats.translate(pos.x, pos.y, pos.z)
        if (planet is Planet) {
            mats.scale(planet.radius.toFloat(), planet.radius.toFloat(), planet.radius.toFloat())
        } else if (planet is Star) {
            mats.scale(planet.radius.toFloat(), planet.radius.toFloat(), planet.radius.toFloat())
        }
        //mats.multiply(Quaternionf(AxisAngle4f(1f, Vector3f(1f, 1f, 1f).normalize())))
        //mats.multiply(Quaternionf(AxisAngle4f(context.world().timeOfDay / 24000f * MathHelper.TAU, 0f, 1f, 0f)))

        if (planet is Atmosphere.Contains) {
            val atmo = planet.atmosphere
            val radius = (atmo.height / planet.radius + 1).toFloat()
            val gap = (atmo.height / atmo.layers / planet.radius).toFloat()
            val color = atmo.color
            val atmo_buf = bufs.getBuffer(ATMOSPHERE_LAYER)
            for (i in 0..<atmo.layers) {
                for (dir in Direction.entries) {
                    drawSquare(
                        mats.peek(),
                        atmo_buf,
                        Quaternionf().rotateTo(Vector3f(0f, 0f, 1f), dir.vector.vector3f),
                        radius - i * gap,
                        i * gap - radius,
                        color.x,
                        color.y,
                        color.z,
                        1f
                    )
                }
            }
        }

        mats.translate(-0.5f, -0.5f, -0.5f)
        val buf = bufs.getBuffer(RenderLayer.getEntityCutout(Identifier.ofVanilla("textures/atlas/blocks.png")))
        val a = MinecraftClient.getInstance().bakedModelManager.getModel(Identifier.of(id.namespace,
            PlanetReloadListener.expandId(id).path.replace(".json",""))) ?: return
        MinecraftClient.getInstance().blockRenderManager.modelRenderer.render(mats.peek(), buf, null, a, 1f, 1f, 1f,
            LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE,
            OverlayTexture.DEFAULT_UV)

        mats.pop()
    }
}