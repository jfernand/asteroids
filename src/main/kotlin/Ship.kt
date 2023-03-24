
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.math.Vector2
import org.openrndr.math.times
import org.openrndr.shape.Rectangle
import java.lang.String.format

class Ship(
    private val radius: Double,
    phaseState: PhaseState = PhaseState(v(5000.0, 5000.0), v(0.0, 1.0)),
) : Renderable, HasExtent, Particle<Int> by ParticleDelegate(phaseState) {

    override val extent = Rectangle(-radius / 2, -radius / 2, radius, radius)

    var deltaV: Vector2 = zero
    fun fire(): Missile = Missile(phaseState.bumpSpeed(1000.0))

    override fun update(state:State) {
        state.process(state.getDeltaT())
        updatePhaseState(state.getDeltaT())
    }

    private fun State.process(deltaTime: Double) {
        if (turningLeft) {
            java.awt.Toolkit.getDefaultToolkit().beep()
            rotateAttitude(-180.0 * deltaTime)
        }
        if (turningRight) {
            rotateAttitude(180.0 * deltaTime)
        }
        if (accelerating) {
            deltaV = accelerate(1000.0, deltaTime)
        }
//        if (decelerating) speed = (speed - 100.0).coerceAtLeast(0.0) // do not roll backwards
    }

    override fun render(drawer: Drawer, state: State) {
        drawer.isolated {
            drawHull()
            if (state.accelerating) drawExhaust()
            if (state.accelerating) drawSecondaryExhaust()
            strokeWeight = 0.1
            rectangle(extent)
        }
    }

    private fun Drawer.drawHull() {
        fill = ColorRGBa.TRANSPARENT
        stroke = ColorRGBa.WHITE
        val worldScale = height / 10000.0
        translate(position * worldScale)
        lineSegment(0.0, 0.0, 10 * headingVector.x, 10 * headingVector.y)
        lineSegment(zero, 20.0 * deltaV)
        rotate(attitude)
        lineSegment(0.0, 0.0, 10.0, 0.0)
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

    override fun hashCode() = id
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ship

        if (id != other.id) return false

        return true
    }

    override fun toString(): String = "S: r = ${position.format("% 8.2f")} v = ${velocity.format("% 8.2f")} h = ${headingVector.format("% 5.3f")} a = ${attitudeVector.format("% 5.3f")} (${
        format(
            "% 5.1f",
            attitude
        )
    }) (${format("% 5.1f", direction)})"
}
