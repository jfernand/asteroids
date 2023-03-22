
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.noise.random
import org.openrndr.math.Vector2
import org.openrndr.shape.contour
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class Asteroid(radius: Double) : Id, Renderer, IParticle by Particle(v(0.0, 0.0), random(0.0, 360.0), random(100.0, 500.0)) {
    private val outline = generate(radius)

    override val id = Random.nextInt()

    override fun hashCode() = id

    override fun render(drawer: Drawer, state: State) {
        drawer.isolated {
            val worldScale = height / 10000.0
            stroke = ColorRGBa.WHITE
            fill = ColorRGBa.BLACK
            translate(realPosition * worldScale)
            val c = contour {
                moveTo(outline.first())
                outline.drop(1).forEach {
                    lineTo(it)
                }
                close()
            }
            contour(c)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Asteroid

        if (id != other.id) return false

        return true
    }

    override fun toString(): String = "A: r = ${realPosition.format("% 8.2f")} v= ${velocity.format("% 8.2f")}"

}

//class AsteroidRenderer(private val drawer: Drawer) {
//    private val worldScale = drawer.height / 10000.0
//
//    fun draw(asteroid: Asteroid, state: State) =
//        drawer.isolated {
//            stroke = ColorRGBa.WHITE
//            fill = ColorRGBa.BLACK
//            drawer.translate(asteroid.realPosition * worldScale)
//            val c = contour {
//                moveTo(asteroid.outline.first())
//                asteroid.outline.drop(1).forEach {
//                    lineTo(it)
//                }
//                close()
//            }
//            drawer.contour(c)
//        }
//}

fun main() {
    println(generate(10.0))
}

fun generate(radius: Double): List<Vector2> {
    val segments = listOf(4, 5, 6, 7, 8, 9, 10, 11, 12).shuffled().first()
    val sector = 2.0 * PI / segments
    return (0 until segments).map {
        val angle = sector * it
        val t = random(radius / 2, radius)
        v(t * cos(angle), t * sin(angle)).round(2)
    }.toList()
}
