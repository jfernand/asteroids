import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.draw.writer
import org.openrndr.panel.elements.round
import org.openrndr.shape.Rectangle
import kotlin.random.Random

object Instruments : HasId<Int>, Renderable {

    override val id: Int = Random.nextInt()

    override fun render(drawer: Drawer, state: State) {
        drawer.isolated {
            writer {
                box = Rectangle(height.toDouble(), 64.0, width.toDouble(), height.toDouble())
                text("${state.getDeltaT().round(3)}\n")
                text("${state.size} objects\n")
                text("\nObjects:\n")
                state.forEach { (id, obj) ->
                    text("$obj\n")
                }
                text("\n${state}")
            }
            val ship = state.ship
            if (ship != null) {
                text("v = ${ship.velocity.format("% 8.2f")}", height.toDouble(), height.toDouble() - 10)
                text("r = ${ship.position.format("% 7.2f")}", height.toDouble(), height.toDouble() - 64)
            }
        }
    }

    override fun toString():String = ""
}
