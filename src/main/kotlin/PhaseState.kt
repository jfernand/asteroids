
import org.openrndr.math.Vector2
import org.openrndr.math.asDegrees
import org.openrndr.math.times
import kotlin.math.atan

interface IPhaseState {
    val position: Vector2
    val velocity: Vector2
    val attitudeVector: Vector2
    val odometer: Double
    val headingVector: Vector2
    val attitude: Double
    val direction: Double
    fun accelerate(a: Double, deltaT: Double):Vector2
    fun bumpSpeed(deltaSpeed: Double)
    fun updatePhaseState(deltaT: Double)
    fun rotateAttitude(deg: Double)
}

// Using to rawBits() to detect a zero with a negative sign
fun arctan(v: Vector2): Double {
    val angle = atan(v.y / v.x).asDegrees
    return if (v.x < 0.0) (180.0 + angle)
    else if (v.x > 0.0 && v.y < 0.0) 360.0 + angle
    else if (v.x > 0.0 && v.y == 0.0 && v.y.toRawBits() ushr 63 == 1L) 360.0 + angle
    else if (v.x == 0.0 && v.x.toRawBits() ushr 63 == 1L) 180.0 + angle
    else angle
}

data class PhaseState(
    val position: Vector2,
    val velocity: Vector2,
    val attitudeVector: Vector2 = velocity / velocity.length,
    val odometer: Double = 0.0,
) {

    val headingVector: Vector2 = velocity / velocity.length
    val attitude = arctan(attitudeVector)

    val direction: Double = arctan(headingVector)

    fun accelerate(a: Double, deltaT: Double): Pair<PhaseState, Vector2> {
        val speedIncrease = a * deltaT
        val deltaV = speedIncrease * attitudeVector
        val finalV = velocity + deltaV
        return PhaseState(
            position = position,
            velocity = finalV,
            attitudeVector = attitudeVector
        ) to deltaV
    }

    fun bumpSpeed(deltaSpeed: Double) = copy(velocity = velocity + deltaSpeed * attitudeVector)

    fun update(deltaT: Double): PhaseState {
        val deltaR = velocity * deltaT
        return copy(
            position = (position + deltaR).wrap(),
            odometer = odometer + deltaR.length
        )
    }

    fun rotateAttitude(deg: Double) = copy(attitudeVector = attitudeVector.rotate(deg))

    override fun toString(): String {
        return "[position=$position, velocity=$velocity, attitudeVector=$attitudeVector (${attitudeVector.length}), attitude=$attitude, direction=$direction]"
    }
}
