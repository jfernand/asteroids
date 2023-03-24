interface Time {
    fun getDeltaT(): Double
    fun getLastTime(): Double
    fun getElapsedTime(): Double
    fun tick(elapsedTime: Double)
    fun tock(elapsedTime: Double)
}

data class TimeDelegate(
    private var deltaTime: Double = 0.0,
    private var lastTime: Double = 0.0,
    private var elapsedTime: Double = 0.0,
) : Time {

    override fun getDeltaT(): Double {
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
