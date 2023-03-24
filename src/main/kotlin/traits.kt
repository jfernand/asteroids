import org.openrndr.draw.Drawer
import org.openrndr.shape.Rectangle

interface HasId<T> {
    val id: T
}

interface Ranged {
    val range: Double
}

interface Renderable {
    fun render(drawer: Drawer, state: State)
}

interface HasExtent {
    val extent: Rectangle
}

interface Updateable {
    fun update(state:State)
}
