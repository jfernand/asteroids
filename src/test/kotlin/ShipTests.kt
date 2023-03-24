import org.junit.Assert.assertEquals
import org.junit.Test

class ShipTest {

    @Test
    fun `Ship Moves`() {
        val ship = Ship(100.0)
        ship.updatePhaseState(1.0 / 60.0, 0.0, KeyStateDelegate())
        assertEquals(v(2.0, 2.0), ship.position)
    }
}
