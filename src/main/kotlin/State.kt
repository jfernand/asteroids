import org.openrndr.KEY_ARROW_DOWN
import org.openrndr.KEY_ARROW_LEFT
import org.openrndr.KEY_ARROW_RIGHT
import org.openrndr.KEY_ARROW_UP
import org.openrndr.KEY_ESCAPE
import org.openrndr.KEY_SPACEBAR
import org.openrndr.KeyEvent
import org.openrndr.draw.Drawer
import kotlin.system.exitProcess

data class ShipInputs(
    val turningLeft: Boolean = false,
    val turningRight: Boolean = false,
    val accelerating: Boolean = false,
    val decelerating: Boolean = false,
    val firing: Boolean = false,
) {
    override fun toString(): String {
        var s = ""
        if (turningLeft) s += "L"
        if (turningRight) s += "R"
        if (accelerating) s += "U"
        if (decelerating) s += "D"
        if (firing) s += "S"
        return s
    }
}

interface Timer {
    fun getDeltaTime(): Double
    fun getLastTime(): Double
    fun getElapsedTime(): Double
    fun tick(elapsedTime: Double)
    fun tock(elapsedTime: Double)
}

data class TimeState(
    private var deltaTime: Double = 0.0,
    private var lastTime: Double = 0.0,
    private var elapsedTime: Double = 0.0,
) : Timer {

    override fun getDeltaTime(): Double {
        return deltaTime
    }

    override fun getLastTime(): Double {
        return lastTime
    }

    override fun getElapsedTime(): Double {
        return elapsedTime
    }

    override fun tick(elapsedTime: Double) {
        this.elapsedTime = elapsedTime
        deltaTime = elapsedTime - lastTime
    }

    override fun tock(elapsedTime: Double) {
        lastTime = elapsedTime
    }
}

data class State(val ship: Ship) : Timer by TimeState() {
    var shipInputs: ShipInputs = ShipInputs()
    val objs: MutableMap<Int, Any> = mutableMapOf()

    init {
        add(ship)
    }

    fun add(obj: Id) {
        objs[obj.id] = obj
    }

    fun addAll(objs: Iterable<Id>) {
        objs.forEach { add(it) }
    }

    fun update() {
        objs.forEach { (_, obj) ->
            when (obj) {
                is Updateable -> obj.update(getElapsedTime(), getDeltaTime(), shipInputs)
            }
        }
    }

    fun purge() {
        filter<Particle> { !it.alive }
            .forEach {
                println("Removing ${it.id}")
                objs.remove(it.id)
            }
    }

    inline fun <reified T> forEach(action: (T) -> Unit) = objs.values.filter { it is T }.map { it as T }.forEach(action)
    inline fun <reified T> filter(predicate: (T) -> Boolean) = objs.values.filter { it is T }.map { it as T }.filter(predicate)

    fun keyDown(keyEvent: KeyEvent) {
        shipInputs = when (keyEvent.key) {
            KEY_ARROW_DOWN -> shipInputs.copy(decelerating = true)
            KEY_ARROW_LEFT -> shipInputs.copy(turningLeft = true)
            KEY_ARROW_RIGHT -> shipInputs.copy(turningRight = true)
            KEY_ARROW_UP -> shipInputs.copy(accelerating = true)
            KEY_ESCAPE -> exitProcess(0)
            KEY_SPACEBAR -> shipInputs.copy(firing = true)
            else -> ShipInputs().copy(firing = shipInputs.firing) // do not reset until missile fired
        }
    }

    fun keyUp() {
        shipInputs = ShipInputs().copy(firing = shipInputs.firing) // do not reset until missile fired
    }

    fun fireMissiles() {
        if (shipInputs.firing) {
            add(ship.fire())
            shipInputs = shipInputs.copy(firing = false)
        }
    }

    fun render(drawer: Drawer) = forEach<Renderer> { it.render(drawer, this) }
}
