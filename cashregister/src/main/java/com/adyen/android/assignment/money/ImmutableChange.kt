package com.adyen.android.assignment.money

public class ImmutableChange(private val change: Change) {
    val total: Long = change.total
    val elements: Set<MonetaryElement> = change.getElements()

    fun getCount(element: MonetaryElement): Int {
        return change.getCount(element)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImmutableChange) return false
        return change == other.change
    }

    override fun hashCode(): Int {
        return change.hashCode()
    }

    override fun toString(): String {
        return change.toString()
    }

    companion object {
        fun Change.toImmutable(): ImmutableChange {
            return ImmutableChange(this)
        }
    }
}