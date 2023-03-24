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
}
