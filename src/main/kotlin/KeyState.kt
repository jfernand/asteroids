import org.openrndr.CharacterEvent
import org.openrndr.KEY_ARROW_DOWN
import org.openrndr.KEY_ARROW_LEFT
import org.openrndr.KEY_ARROW_RIGHT
import org.openrndr.KEY_ARROW_UP
import org.openrndr.KEY_ESCAPE
import org.openrndr.KEY_SPACEBAR
import org.openrndr.KeyEvent
import kotlin.system.exitProcess

interface KeyState {
    val turningLeft: Boolean
    val turningRight: Boolean
    val accelerating: Boolean
    val decelerating: Boolean
    val showBoundaries: Boolean
    val showData: Boolean
    val firing: Boolean
    fun keyDown(keyEvent: KeyEvent)
    fun keyUp(keyEvent: KeyEvent)
    fun character(characterEvent: CharacterEvent)
    fun doneFiring()
}

data class KeyStateDelegate(
    override var turningLeft: Boolean = false,
    override var turningRight: Boolean = false,
    override var accelerating: Boolean = false,
    override var decelerating: Boolean = false,
    override var showBoundaries:Boolean = false,
    override var showData:Boolean = false,
    override var firing: Boolean = false,
) : KeyState {

    override fun doneFiring() {
        firing = false
    }

    override fun keyDown(keyEvent: KeyEvent) {
        when (keyEvent.key) {
            KEY_ARROW_DOWN -> decelerating = true
            KEY_ARROW_LEFT -> turningLeft = true
            KEY_ARROW_RIGHT -> turningRight = true
            KEY_ARROW_UP -> accelerating = true
            KEY_SPACEBAR -> firing = true
            KEY_ESCAPE -> exitProcess(0)
            else -> Unit
        }
    }

    override fun keyUp(keyEvent: KeyEvent) {
        when (keyEvent.key) {
            KEY_ARROW_DOWN -> decelerating = false
            KEY_ARROW_LEFT -> turningLeft = false
            KEY_ARROW_RIGHT -> turningRight = false
            KEY_ARROW_UP -> accelerating = false
            else -> Unit
        }
    }

    override fun character(characterEvent: CharacterEvent) {
        when (characterEvent.character) {
            'b' -> showBoundaries = !showBoundaries
            'd' -> showData = !showData
            else -> Unit
        }
    }
    override fun toString(): String {
        var s = ""
        if (turningLeft) s += "L"
        if (turningRight) s += "R"
        if (accelerating) s += "U"
        if (decelerating) s += "D"
        if (showBoundaries) s += "B"
        if (firing) s += "S"
        return s
    }
}
