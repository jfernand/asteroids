import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.color.presets.MEDIUM_SLATE_BLUE
import kotlin.math.PI
import kotlin.random.Random

class Ship(private val radius: Double, speed: Double = 200.0, heading: Double = 45.0) : Id, Renderer, IParticle by Particle(heading = heading, speed = speed) {
    override val id = Random.nextInt()

    override fun hashCode() = id

    fun fire(): Missile = Missile(realPosition, heading, speed + 500)

    override fun update(elapsed: Double, deltaTime: Double, inputs: ShipInputs) {
        inputs.process(deltaTime)
        realPosition = (realPosition + velocity * deltaTime).wrap()
    }

    private fun ShipInputs.process(deltaTime: Double) {
        if (turningLeft) heading -= 8 * deltaTime // 1deg/sec
        if (turningRight) heading += 8 * deltaTime
        if (accelerating) speed += 100
        if (decelerating) speed = (speed - 100).coerceAtLeast(0.0) // do not roll backwards
    }

    override fun render(drawer: Drawer, state: State) {
        drawer.isolated {
            drawHull()
            if (state.shipInputs.accelerating) drawExhaust()
            if (state.shipInputs.accelerating) drawSecondaryExhaust()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ship

        if (id != other.id) return false

        return true
    }

    private fun Drawer.drawHull() {
        fill = ColorRGBa.MEDIUM_SLATE_BLUE
        val worldScale = height / 10000.0
        translate(realPosition * worldScale)
        rotate(heading * 180 / PI)
        stroke = ColorRGBa.WHITE
        lineLoop(
            listOf(
                v(-radius / 4, 0.0),
                v(-radius / 2, radius / 2),
                v(radius / 2, 0.0),
                v(-radius / 2, -radius / 2),
            )
        )
    }

    private fun Drawer.drawExhaust() {
        stroke = ColorRGBa.RED
        lineLoop(
            listOf(
                v(-radius / 4, 0.0),
                v(-radius / 2, -radius / 4),
                v(-radius * 3 / 4, 0.0),
                v(-radius / 2, radius / 4),
            )
        )
    }

    private fun Drawer.drawSecondaryExhaust() {
        stroke = ColorRGBa.RED
        lineLoop(
            listOf(
                v(-radius / 4, 0.0),
                v(-radius / 2, -radius / 4),
                v(-radius, 0.0),
                v(-radius / 2, radius / 4),
            )
        )
    }

    override fun toString(): String = "S: r = ${realPosition.format("% 8.2f")} v= ${velocity.format("% 8.2f")}"
}
