
import org.junit.Assert.assertEquals
import org.junit.Test
import org.openrndr.math.asDegrees
import org.openrndr.math.asRadians
import org.openrndr.panel.elements.round
import java.lang.Long.toBinaryString
import java.lang.String.format
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sign
import kotlin.math.sin

class PhaseStateTests {

    @Test
    fun testOrientation() {
        val state = PhaseState(
            zero,
            v(1.0, 0.0),
            v(0.0, 1.0)
        )
        println(state)
        assertEquals("attitude is a unit vector", 1.0, state.attitudeVector.length, 0.001)
        assertEquals("Moving due North", 0.0, state.direction, 0.001)
        val (newState, deltaV) = state.accelerate(0.0, 1.0)
        println(newState)
        assertEquals("Still moving to the North", 0.0, newState.direction, 0.001)
        assertEquals("Pointing to the East", 90.0, newState.attitude, 0.001)

        println(newState.direction)
    }

    @Test
    fun testRotation() {
        val state = PhaseState(
            zero,
            v(1.0, 0.0),
            v(0.0, 1.0)
        )
        println(state)
        assertEquals(0.0, state.direction, 0.001)
        assertEquals(90.0, state.attitude, 0.001)
        val newState = state.rotateAttitude(8.0)
        println(newState)
        assertEquals(98.0, newState.attitude, 0.001)
    }

    @Test
    fun testArctan() {
        for (i in 0..360) {
            val theta = i.toDouble().asRadians
            val y = sin(theta).round(2)
            val x = cos(theta).round(2)
            val arctan = arctan(v(x, y)).round(2)
            val y1 = sin(arctan.asRadians).round(2)
            val x1 = cos(arctan.asRadians).round(2)
            val atan = atan(y / x).asDegrees.round(2)
            val y2 = sin(atan.asRadians).round(2)
            val x2 = cos(atan.asRadians).round(2)
            println(
                format(
                    "%3d % .20f % 4.2f | % 7.2f % 4.2f % 4.2f | % 7.2f | % 4.2f % 4.2f %s%s%s %s",
                    i,
                    x,
                    y,
                    arctan,
                    x1,
                    y1,
                    atan,
                    x2,
                    y2,
                    if (abs(i.toDouble() - arctan) > 1.0) "!" else " ",
                    if (x == +0.0) "+" else "",
                    if (x.sign == 0.0) "0" else "",
                    toBinaryString(x.toRawBits() ushr 63)
                )
            )
        }
    }
}
