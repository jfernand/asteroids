
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.shape.Rectangle

class Missile(
    phaseState: PhaseState,
    override val range: Double = 20000.0,
) : Renderable, HasExtent, Ranged, Particle<Int> by ParticleDelegate(phaseState) {
    override val extent: Rectangle = Rectangle(-5.0, -5.0, 10.0, 10.0)
    override fun hashCode() = id

    override fun render(drawer: Drawer, state: State) {

        drawer.isolated {
            val worldScale = height / 10000.0
            translate(phaseState.position * worldScale)
            stroke = ColorRGBa.RED
            fill = ColorRGBa.BLACK
            rotate(direction)
            circle(v(0.0, 0.0), 5.0)
            circle(10.0, 0.0, 3.0)
            strokeWeight = 0.1
            if (state.showBoundaries)
                rectangle(extent)

        }
    }

    override fun update(state:State ) {
        updatePhaseState(state.getDeltaT())
        if (odometer > range) alive = false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Missile

        if (id != other.id) return false

        return true
    }

    override fun toString(): String = "M: r = ${position.format("% 8.2f")} v= ${velocity.format("% 8.2f")} d = ${odometer} ${if (alive) "alive" else "dead"}"
}
