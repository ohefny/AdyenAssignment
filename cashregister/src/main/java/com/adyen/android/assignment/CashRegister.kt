package com.adyen.android.assignment

import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.ImmutableChange
import com.adyen.android.assignment.money.ImmutableChange.Companion.toImmutable

/**
 * The CashRegister class holds the logic for performing transactions.
 *
 * @param registerChange The change that the CashRegister is holding.
 */
class CashRegister(initialChange: Change) {

    private val registerChange: Change = initialChange.clone()
    /**
     * Performs a transaction for a product/products with a certain price and a given amount.
     *
     * @param price The price of the product(s).
     * @param amountPaid The amount paid by the shopper.
     *
     * @return The change for the transaction.
     *
     * @throws TransactionException If the transaction cannot be performed.
     */
    fun performTransaction(priceInCents: Long, amountPaid: Change): Change {
        if(amountPaid.total < priceInCents) {
            throw TransactionException("Amount paid is less than the price by ${amountPaid.total} < $priceInCents")
        }
        addPaidChangeToRegister(amountPaid)
        if(amountPaid.total == priceInCents) {
            return Change.none()
        }
        val changeGivenBack = calculateChangeToReturnFromRegister(amountPaid = amountPaid, priceInCents = priceInCents)
        val canGiveBackRemainingChange = amountPaid.total - priceInCents == changeGivenBack.total
        if (!canGiveBackRemainingChange) {
            takeChangeOutFromRegister(change = amountPaid) //revert register to it's previous state
            throw TransactionException("Cannot provide exact change")
        }
        takeChangeOutFromRegister(change = changeGivenBack)
        return changeGivenBack
    }

    fun checkChange(): ImmutableChange {
        return registerChange.toImmutable()
    }

    private fun addPaidChangeToRegister(amountPaid: Change) {
        val elements = amountPaid.getElements()
        for (element in elements) {
            val count = amountPaid.getCount(element)
            registerChange.add(element, count)
        }
    }

    private fun calculateChangeToReturnFromRegister(
        amountPaid: Change,
        priceInCents: Long
    ): Change {
        var remainingCents = amountPaid.total - priceInCents
        val giveBackChange = Change()
        val elements = registerChange.getElements()
            .reversed() //it's guaranteed that bills/coins are sorted from smallest to largest in Change contract
        for (element in elements) {
            val countInRegister = registerChange.getCount(element)
            val neededCount = remainingCents / element.minorValue
            val usedCount: Int = minOf(neededCount.toInt(), countInRegister)
            giveBackChange.add(element = element, count = usedCount)
            remainingCents -= usedCount * element.minorValue.toLong()
        }
        return giveBackChange
    }

    private fun takeChangeOutFromRegister(change: Change) {
        val elements = change.getElements()
        for (element in elements) {
            val count = change.getCount(element)
            registerChange.remove(element, count)
        }
    }


    class TransactionException(message: String, cause: Throwable? = null) : Exception(message, cause)
}
