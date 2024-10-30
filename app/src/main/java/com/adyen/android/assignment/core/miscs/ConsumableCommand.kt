package com.adyen.android.assignment.core.miscs

data class ConsumableCommand<T>(
    private var data: T,
    private val onConsumed: Consumer<Long>? = null
) {
    var isConsumed = false
    val updateTime: Long = System.currentTimeMillis()
    fun consume(consumer: (T) -> Unit) {
        if (isConsumed) return
        consumer(data)
        onConsumed?.invoke(updateTime)
        isConsumed = true
    }

    fun consume(): T? {
        if (isConsumed) return null
        val result = data
        onConsumed?.invoke(updateTime)
        isConsumed = true
        return result
    }

    override fun equals(other: Any?): Boolean {
        return other?.takeIf { it is ConsumableCommand<*> && it.updateTime == updateTime }
            ?.let { true } ?: false
    }

    override fun hashCode(): Int {
        var result = data?.hashCode() ?: 0
        result = 31 * result + updateTime.hashCode()
        return result
    }

    companion object {
        fun unit(onConsumed: Consumer<Long>? = null): ConsumableCommand<Unit> =
            ConsumableCommand<Unit>(Unit, onConsumed)
    }
}