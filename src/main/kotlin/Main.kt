import org.openrndr.Fullscreen
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.draw.tint
import org.openrndr.extra.noise.random
import org.openrndr.math.Vector2
import org.openrndr.panel.elements.round
import java.lang.String.format
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun main() = application {
    configure {
        fullscreen = Fullscreen.CURRENT_DISPLAY_MODE
    }

    program {
        val state = State()
        state.addShip(Ship(width / 128.0))

        state.addAll((0..10).map { Asteroid(random(10.0, 100.0)) }.toList())
        state.add(Instruments)

        keyboard.keyDown.listen { state.keyDown(it) }
        keyboard.keyUp.listen { state.keyUp(it) }
        keyboard.character.listen { state.character(it) }

        drawer.drawStyle.colorMatrix = tint(ColorRGBa.WHITE.shade(0.2))

        drawer.fill = ColorRGBa.WHITE
        drawer.fontMap = loadFont("data/fonts/VT323-Regular.ttf", 64.0)

        extend {
            state.tick(seconds)
            state.fireMissiles()
            state.update()
            state.render(drawer)
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
fun randomVec(min: Double, max: Double): Vector2 {
    val speed = random(min, max)
    val heading = random(00.0, 360.0) * PI / 180.0
    return v(speed * cos(heading), speed * sin(heading))
}

val zero = v(0.0, 0.0)
