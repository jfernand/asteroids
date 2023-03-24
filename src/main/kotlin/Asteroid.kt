import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.noise.random
import org.openrndr.math.Vector2
import org.openrndr.shape.bounds
import org.openrndr.shape.contour
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class Asteroid(
    private val radius: Double,
) : Renderable, HasExtent, Particle<Int> by ParticleDelegate(PhaseState(zero, randomVec(100.0, 500.0))) {
    private val outline = generate(radius)
    override val extent = outline.bounds

    override val id = Random.nextInt()

    override fun hashCode() = id

    override fun render(drawer: Drawer, state: State) {
        drawer.isolated {
            val worldScale = height / 10000.0
            stroke = ColorRGBa.WHITE
            fill = ColorRGBa.BLACK
            translate(position * worldScale)
            pushModel()
            rotate(attitude)
            val c = contour {
                moveTo(outline.first())
                outline.drop(1)
                    .forEach {
                        lineTo(it)
                    }
                close()
            }
            contour(c)
            circle(v(0.0, 0.0), 5.0)
            strokeWeight = 0.1
            fill = ColorRGBa.TRANSPARENT
            circle(v(0.0, 0.0), radius)
            rectangle(extent)
            popModel()
            lineSegment(0.0, 0.0, 10.0 , 0.0 )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Asteroid

        if (id != other.id) return false

        return true
    }

    override fun toString(): String = "A: r = ${position.format("% 8.2f")} v= ${velocity.format("% 8.2f")} h = ${headingVector.format("% 5.2f")} a = ${attitudeVector.format("% 5.2f")}"
}

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
