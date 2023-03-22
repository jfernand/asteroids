import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.draw.tint
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.shape.Rectangle
import org.openrndr.writer
import kotlin.math.cos
import kotlin.math.sin

/**
 *  This is a template for a live program.
 *
 *  It uses oliveProgram {} instead of program {}. All code inside the
 *  oliveProgram {} can be changed while the program is running.
 */

fun main() = application {
    configure {
        width = 768
        height = 576
    }
    oliveProgram {
        val image = loadImage("data/images/pm5544.png")
        val font = loadFont("data/fonts/default.otf", 45.0)

        extend {
            drawer.drawStyle.colorMatrix = tint(ColorRGBa.WHITE.shade(0.6))
            drawer.image(image)

//            drawer.drawStyle.fill = ColorRGBa.TRANSPARENT
//            drawer.drawStyle.stroke = ColorRGBa.PINK
//            drawer.rectangle(300.0, 57.0, 170.0, -42.0)

            drawer.fill = ColorRGBa.RED.mix(ColorRGBa.TRANSPARENT, 0.5)
            drawer.circle(cos(seconds) * width / 2.0 + width / 2.0, sin(0.5 * seconds) * height / 2.0 + height / 2.0, 140.0)

            drawer.fontMap = font
            drawer.fill = ColorRGBa.WHITE
            writer {
                box = Rectangle(300.0, 90.0, 170.0, 42.0)
                text("OPENRNDR")

            }
        }
    }
}
