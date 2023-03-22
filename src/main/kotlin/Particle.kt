import org.openrndr.math.Vector2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

interface IParticle : Updateable {
    var realPosition: Vector2
    var heading: Double
    var speed: Double
    val velocity: Vector2
    var alive: Boolean

    override fun update(elapsed: Double, deltaTime: Double, inputs: ShipInputs)
}

class Particle(
    override var realPosition: Vector2 = v(0.0, 0.0),
    heading: Double,
    speed: Double,
    override var alive:Boolean = true,
) : IParticle, Id {

    override val id = Random.nextInt()

    override var heading: Double = heading
        set(value) {
            field = value
            _velocity = v(speed * cos(field), speed * sin(field))
        }

    override var speed: Double = speed
        set(value) {
            field = value
            _velocity = v(field * cos(heading), field * sin(heading))
        }
    override val velocity: Vector2
        get() = _velocity

    private var _velocity = v(0.0, 0.0)

    init {
        _velocity = v(speed * cos(heading), speed * sin(heading))
    }

    override fun update(elapsed: Double, deltaTime: Double, inputs: ShipInputs) {
        realPosition = (realPosition + velocity * deltaTime).wrap()
    }
}
