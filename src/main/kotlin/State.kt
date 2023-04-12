import org.openrndr.draw.Drawer

class State : Time by TimeDelegate(),
    KeyState by KeyStateDelegate(),
    Container<Int> by ContainerDelegate() {

    var ship: Ship? = null

    fun addShip(ship: Ship) {
        this.ship = ship
        add(ship)
    }

    fun update() {
        update(this)
        fireMissiles()
        detectCollisions()
        purge()
    }

    fun fireMissiles() {
        val s = ship
        if (s != null && firing) {
            add(s.fire())
            doneFiring()
        }
    }

    fun render(drawer: Drawer) = render(drawer, this)

    fun detectCollisions() {}
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
