package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ChangeTest {
    @Test
    fun testEquals() {
        val expected = Change()
            .add(Coin.FIVE_CENT, 3)
            .add(Coin.TWO_CENT, 1)
            .add(Bill.FIFTY_EURO, 2)
        val actual = Change()
            .add(Bill.FIFTY_EURO, 2)
            .add(Coin.FIVE_CENT, 3)
            .add(Coin.TWO_CENT, 1)
        assertEquals(expected, actual)
    }

    @Test
    fun testElementsDiffer() {
        val expected = Change()
            .add(Coin.TWO_EURO, 4)
            .add(Bill.TEN_EURO, 1)
            .add(Coin.FIFTY_CENT, 3)
            .add(Coin.TWENTY_CENT, 2)
        val actual = Change()
            .add(Coin.TWO_EURO, 4)
            .add(Coin.TEN_CENT, 1)
            .add(Coin.FIFTY_CENT, 3)
            .add(Coin.TWENTY_CENT, 2)
        assertNotEquals(expected, actual)
    }

    @Test
    fun testCountsDiffer() {
        val expected = Change()
            .add(Coin.TWO_EURO, 4)
            .add(Bill.ONE_HUNDRED_EURO, 1)
            .add(Coin.FIFTY_CENT, 3)
            .add(Coin.TWENTY_CENT, 2)
        val actual = Change()
            .add(Coin.TWO_EURO, 3)
            .add(Coin.TWENTY_CENT, 1)
            .add(Coin.FIFTY_CENT, 2)
            .add(Bill.ONE_HUNDRED_EURO, 1)
        assertNotEquals(expected, actual)
    }

    @Test
    fun `test adding single element increases count and updates total`() {
        val change = Change()
        change.add(Bill.TEN_EURO, 2)

        assertEquals(2, change.getCount(Bill.TEN_EURO))
        assertEquals(20_00, change.total) // 2 x 10 Euro
    }

    @Test
    fun `test adding multiple elements updates total and counts correctly`() {
        val change = Change()
        change.add(Bill.FIFTY_EURO, 1)
        change.add(Coin.TWENTY_CENT, 5)

        assertEquals(1, change.getCount(Bill.FIFTY_EURO))
        assertEquals(5, change.getCount(Coin.TWENTY_CENT))
        assertEquals(50_00 + (5 * 20), change.total) // 50 Euro + 5 x 0.20 Euro
    }

    @Test
    fun `test removing elements decreases count and updates total`() {
        val change = Change()
        change.add(Coin.TWO_EURO, 3)
        change.remove(Coin.TWO_EURO, 1)

        assertEquals(2, change.getCount(Coin.TWO_EURO))
        assertEquals(4_00, change.total)
    }

    @Test
    fun `test removing all elements sets count to zero and updates total`() {
        val change = Change()
        change.add(Bill.FIVE_HUNDRED_EURO, 1)
        change.remove(Bill.FIVE_HUNDRED_EURO, 1)

        assertEquals(0, change.getCount(Bill.FIVE_HUNDRED_EURO))
        assertEquals(0, change.total)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test removing more elements than available throws exception`() {
        val change = Change()
        change.add(Coin.ONE_CENT, 2)
        change.remove(Coin.ONE_CENT, 3)
    }

    @Test
    fun `elements should be sorted from smallest to largest in Change`() {
        val change = Change()
        change.add(Bill.TWENTY_EURO, 1)
        change.add(Coin.FIVE_CENT, 3)
        change.add(Bill.FIFTY_EURO, 1)
        change.add(Coin.TWO_EURO, 2)

        val sortedElements = change.getElements().toList()
        val isSorted = sortedElements.zipWithNext { a, b ->
            a.minorValue <= b.minorValue
        }.all { it }

        assertTrue("Elements in Change should be sorted from smallest to largest",isSorted)
    }

    @Test
    fun `test clone returns a deep copy of the Change object`() {
        val change = Change()
            .add(Bill.TEN_EURO, 2)
            .add(Coin.FIFTY_CENT, 3)
        val clonedChange = change.clone()

        assertEquals(change, clonedChange)
        change.add(Bill.TEN_EURO, 1)
        assertNotEquals(change, clonedChange)
    }


}
