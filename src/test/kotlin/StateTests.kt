import org.junit.Assert.assertEquals
import org.junit.Test

class StateTests {

    @Test
    fun testPurge() {
        val phaseState = PhaseState(
            zero,
            v(1.0, 0.0),
            v(0.0, 1.0)
        )
        val state = State()
        val particle = Missile(phaseState)
        state.add(particle)
        particle.alive = false
        assertEquals("Particles added correctly",1, state.objs.size)
        state.purge()
        assertEquals("Dead things are purged",0, state.objs.size)
    }
}
