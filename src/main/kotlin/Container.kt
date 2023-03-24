import org.openrndr.draw.Drawer

interface Container<T> : Updateable, Renderable, Map<T, Any> {
    fun add(obj: HasId<T>)
    fun addAll(objs: Iterable<HasId<T>>)
    fun purge()
}

class ContainerDelegate<T>(private val objs: MutableMap<T, Any> = mutableMapOf()) : Container<T>, Map<T, Any> by objs {

    override fun add(obj: HasId<T>) {
        objs[obj.id] = obj
    }

    override fun addAll(objs: Iterable<HasId<T>>) {
        objs.forEach { add(it) }
    }

    override fun update(state:State) {
        objs.forEach { (_, obj) ->
            when (obj) {
                is Updateable -> obj.update( state)
            }
        }
    }

    override fun render(drawer: Drawer, state:State) = forEach<Renderable> { it.render(drawer, state) }

    override fun purge() {
        filter<Particle<T>> { !it.alive }
            .forEach {
                println("Removing ${it.id}")
                objs.remove(it.id)
            }
    }

    private inline fun <reified T> forEach(action: (T) -> Unit) = objs.values.filter { it is T }.map { it as T }.forEach(action)
    private inline fun <reified T> filter(predicate: (T) -> Boolean) = objs.values.filter { it is T }.map { it as T }.filter(predicate)
}
