import org.junit.Assert.assertEquals
import org.junit.Test

class ShipTest {

    @Test
    fun `Ship Moves`() {
        val ship = Ship(100.0)
        ship.speed =120.0
        ship.heading = 45.0
        ship.update(1.0 / 60.0, 0.0, ShipInputs())
        assertEquals(v(2.0, 2.0), ship.realPosition)
    }
}
