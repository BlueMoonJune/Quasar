package bluemoonjune.quasar.space

import com.google.gson.JsonObject
import org.joml.Quaterniond
import org.joml.Quaternionf
import org.joml.Vector3d
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

class Orbit(var eccentricity : Double, var semimajoraxis : Double, var rotation: Quaterniond, var epoch: Double, val body: Orbitable) {

    companion object {
        const val G = 6.67430E-11
        const val PERIOD_CONSTAT = 4*PI*PI/G
        const val DEFAULT_ITERS = 128

        fun parseJSON(obj: JsonObject, body: Orbitable) = Orbit(
            obj.get("ecc").asDouble,
            obj.get("sma").asDouble,
            Quaterniond()
                .rotateY(obj.get("argp").asDouble)
                .rotateX(obj.get("inc").asDouble)
                .rotateY(obj.get("lan").asDouble),
            obj.get("epoch").asDouble,
            body
        )
    }

    fun getPeriod() = sqrt(PERIOD_CONSTAT / body.mass * semimajoraxis * semimajoraxis * semimajoraxis)
    fun getMeanMotion() = Math.TAU/getPeriod()
    fun getTimeSincePeriapsis(time: Double) = (time - epoch) % getPeriod()
    fun getMeanAnomaly(time: Double) = getMeanMotion()*getTimeSincePeriapsis(time)
    fun computeEccentricAnomaly(time: Double, iters: Int = DEFAULT_ITERS) : Double {
        var M = getMeanAnomaly(time)
        var E = M
        for (i in 1..iters) {
            E = M + eccentricity * sin(E)
        }
        return E
    }
    fun computeTrueAnomaly(E: Double) : Double {
        val a = 2* atan(sqrt((1+eccentricity)*tan(E/2).pow(2)/(1-eccentricity)))
        return if (E < PI) a else Math.TAU - a
    }
    fun computeDistance(E: Double) : Double = semimajoraxis*(1-eccentricity * cos(E))

    fun computePlanarPosition(time: Double, iters: Int = DEFAULT_ITERS) : Vector3d {
        val E = computeEccentricAnomaly(time, iters)
        val r = computeDistance(E)
        val t = computeTrueAnomaly(E)
        return Vector3d(cos(t), 0.0, sin(t)).mul(r)
    }
    fun computeLocalPosition(time: Double, iters: Int = DEFAULT_ITERS) : Vector3d = computePlanarPosition(time, iters).rotate(rotation)

    fun computePosition(time: Double, iters: Int = Orbit.DEFAULT_ITERS): Vector3d {
        val body = body
        var pos = computeLocalPosition(time, iters)
        if (body is Satellite) {
            pos = pos.add(body.orbit.computePosition(time, iters))
        }
        return pos
    }
}