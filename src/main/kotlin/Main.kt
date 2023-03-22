import org.openrndr.Fullscreen
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.draw.loadFont
import org.openrndr.draw.tint
import org.openrndr.draw.writer
import org.openrndr.extra.noise.random
import org.openrndr.math.Vector2
import org.openrndr.panel.elements.round
import org.openrndr.shape.Rectangle
import java.lang.String.format
import kotlin.random.Random

fun main() = application {
    configure {
        fullscreen = Fullscreen.CURRENT_DISPLAY_MODE
    }

    program {
        val state = State(Ship(width / 128.0))

        state.addAll((0..10).map { Asteroid(random(10.0, 100.0)) }.toList())
        state.add(Instruments)

        keyboard.keyDown.listen { state.keyDown(it) }
        keyboard.keyUp.listen { state.keyUp() }

        drawer.drawStyle.colorMatrix = tint(ColorRGBa.WHITE.shade(0.2))

        drawer.fill = ColorRGBa.WHITE
        drawer.fontMap = loadFont("data/fonts/VT323-Regular.ttf", 64.0)

        extend {
            state.tick(seconds)
            state.fireMissiles()
            state.update()
            state.render(drawer)
            state.purge()
            state.tock(seconds)
        }
    }
}

fun Double.wrap(): Double = (this + 10000.0) % 10000.0
fun Vector2.wrap() = Vector2(x.wrap(), y.wrap())
fun v(x: Double, y: Double) = Vector2(x, y)
fun Vector2.string() = "(${x.round(2)}, ${y.round(2)})"
fun Vector2.format(fmt: String): String = format("($fmt, $fmt)", x, y)
fun Vector2.round(i: Int) = v(x.round(i), y.round(i))

interface Renderer {
    fun render(drawer: Drawer, state: State)
}

interface Updateable {
    fun update(elapsed: Double, deltaTime: Double, inputs: ShipInputs)
}

interface Id {
    val id: Int
}

object Instruments : Id, Renderer {

    override val id: Int = Random.nextInt()

    override fun render(drawer: Drawer, state: State) {
        drawer.isolated {
            writer {
                box = Rectangle(height.toDouble(), 64.0, width.toDouble(), height.toDouble())
                text("${state.getDeltaTime().round(3)}\n")
                text("${state.objs.size} objects\n")
                text("\nObjects:\n")
                state.objs.forEach { (id, obj) ->
                    text("$obj\n")
                }
                text("\n${state.shipInputs}")
            }
            text("v = ${state.ship.velocity.format("% 8.2f")}", height.toDouble(), height.toDouble() - 10)
            text("r = ${state.ship.realPosition.format("% 7.2f")}", height.toDouble(), height.toDouble() - 64)
        }
    }

}
