
import org.openrndr.math.Vector2
import kotlin.random.Random

interface Particle<T> : IPhaseState, Updateable, HasId<T> {
    var phaseState: PhaseState
    var alive: Boolean
}

class ParticleDelegate(
    override var phaseState: PhaseState = PhaseState(zero, zero, v(0.0, 1.0)),
    override var alive: Boolean = true,
) : Particle<Int>, IPhaseState {

    override val id = Random.nextInt()

    override fun update(state: State) {
        phaseState = phaseState.update(state.getDeltaT())
    }

    override val position: Vector2
        get() = phaseState.position
    override val velocity: Vector2
        get() = phaseState.velocity
    override val attitudeVector: Vector2
        get() = phaseState.attitudeVector
    override val headingVector: Vector2
        get() = phaseState.headingVector
    override val attitude: Double
        get() = phaseState.attitude
    override val direction: Double
        get() = phaseState.direction
    override val odometer: Double
        get() = phaseState.odometer

    override fun accelerate(a: Double, deltaT: Double): Vector2 {
        val ret = phaseState.accelerate(a, deltaT)
        phaseState = ret.first
        return ret.second
    }

    override fun bumpSpeed(deltaSpeed: Double) {
        phaseState = phaseState.bumpSpeed(deltaSpeed)
    }

    override fun updatePhaseState(deltaT: Double) {
        phaseState = phaseState.update(deltaT)
    }

    override fun rotateAttitude(deg: Double) {
        phaseState = phaseState.rotateAttitude(deg)
    }
}
