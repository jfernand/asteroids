import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.math.Vector2
import kotlin.random.Random





class Missile(private val startPosition: Vector2, heading: Double, speed: Double) : Id, Renderer, IParticle by Particle(startPosition, heading, speed) {
    override val id = Random.nextInt()

    override fun hashCode() = id

    override fun render(drawer: Drawer, state: State) {

        drawer.isolated {
            val worldScale = height / 10000.0
            translate(realPosition * worldScale)
            stroke = ColorRGBa.RED
            circle(v(0.0,0.0), 10.0)
        }
    }

    override fun update(elapsed: Double, deltaTime: Double, inputs: ShipInputs) {
        realPosition = (realPosition + velocity * deltaTime).wrap()
        if ((realPosition - startPosition).length > 1000.0)
            alive = false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Missile

        if (id != other.id) return false

        return true
    }

    override fun toString(): String = "M: r = ${realPosition.format("% 8.2f")} v= ${velocity.format("% 8.2f")} d = ${(realPosition-startPosition).length} ${if(alive) "alive" else "dead"}"
}
