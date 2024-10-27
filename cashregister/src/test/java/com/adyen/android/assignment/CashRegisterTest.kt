package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import com.adyen.android.assignment.money.ImmutableChange.Companion.toImmutable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class CashRegisterTest {

    @Test
    fun `test CashRegister initialization with change`() {
        val initialChange = Change().apply {
            add(Bill.TWENTY_EURO, 5)
            add(Coin.FIFTY_CENT, 10)
        }
        val cashRegister = CashRegister(initialChange)

        assertEquals(initialChange.toImmutable(), cashRegister.checkChange())
    }

    @Test
    fun `test successful transaction with change returned`() {
        // Initialize the register with specific change amounts
        val initialChange = Change().apply {
            add(Bill.TWENTY_EURO, 5)
            add(Coin.FIFTY_CENT, 10)
            add(Coin.TWO_EURO,2)
        }
        val cashRegister = CashRegister(initialChange)

        // Define the amount paid and the price
        val amountPaid = Change().apply {
            add(Bill.TWENTY_EURO, 1)
        }
        val priceInCents = 1500L  // Price is 15 euros

        // Define the expected change that should be given back to the shopper
        val expectedChange = Change().apply {
            add(Coin.TWO_EURO, 2)
            add(Coin.FIFTY_CENT, 2)
        }

        // Perform the transaction and retrieve the change given back
        val changeGivenBack = cashRegister.performTransaction(priceInCents, amountPaid)

        // Verify that the returned change matches the expected change
        assertEquals(expectedChange, changeGivenBack)

        // Adjust the counts of the expected register change manually
        val finalRegisterChange = Change().apply {
            add(Bill.TWENTY_EURO, 6)  // 5 original + 1 from amount paid
            add(Coin.FIFTY_CENT, 8)   // 10 original - 2 given back
            add(Bill.TEN_EURO, 0)     // 2 given back
        }

        // Verify that the CashRegister’s change reflects the transaction correctly
        assertEquals(finalRegisterChange.toImmutable(), cashRegister.checkChange())
    }


    @Test
    fun `test exact payment transaction returns no change`() {
        val initialChange = Change().apply {
            add(Bill.TEN_EURO, 5)
            add(Coin.TWO_CENT, 10)
        }
        val cashRegister = CashRegister(initialChange)
        val amountPaid = Change().apply {
            add(Bill.TEN_EURO, 1)
        }
        val priceInCents = 1000L  // 10 euros

        val changeGivenBack = cashRegister.performTransaction(priceInCents, amountPaid)

        assertTrue(changeGivenBack == Change.none())

        // Check that the register's change reflects the transaction
        val expectedRegisterChange = Change().apply {
            add(Bill.TEN_EURO, 6) //1 added from amount paid
            add(Coin.TWO_CENT, 10)
        }
        assertEquals(expectedRegisterChange.toImmutable(), cashRegister.checkChange())
    }

    @Test
    fun `test register change is updated correctly after transaction`() {
        val initialChange = Change().apply {
            add(Bill.TEN_EURO, 5)
            add(Coin.FIFTY_CENT, 10)
            add(Bill.FIVE_EURO,2)
        }
        val cashRegister = CashRegister(initialChange)
        val amountPaid = Change().apply {
            add(Bill.TEN_EURO, 1)
        }
        val priceInCents = 500L  // 5 euros

        cashRegister.performTransaction(priceInCents, amountPaid)

        // Check that the register's change reflects the transaction with paid change added and given change subtracted
        val expectedRegisterChange = Change().apply {
            add(Bill.TEN_EURO, 6) // 1 added from amount paid
            add(Coin.FIFTY_CENT, 10)
            add(Bill.FIVE_EURO,1) // 1 subtracted as change
        }
        assertEquals(expectedRegisterChange.toImmutable(), cashRegister.checkChange())
    }

    @Test
    fun `test register change is updated correctly after multiple transactions`(){
        val initialChange = Change().apply {
            add(Bill.TEN_EURO, 5)
            add(Coin.FIFTY_CENT, 10)
            add(Bill.FIVE_EURO,2)
        }
        val cashRegister = CashRegister(initialChange)

        val amountPaid1 = Change().apply {
            add(Bill.TEN_EURO, 1)
        }
        val priceInCents1 = 500L  // 5 euros
        cashRegister.performTransaction(priceInCents1, amountPaid1)
        val expectedRegisterChange1 = Change().apply {
            add(Bill.TEN_EURO, 6) // 1 added from amount paid
            add(Coin.FIFTY_CENT, 10)
            add(Bill.FIVE_EURO,1) // 1 subtracted as change
        }
        assertEquals(expectedRegisterChange1.toImmutable(), cashRegister.checkChange())

        val amountPaid2 = Change().apply {
            add(Bill.TEN_EURO, 1)
        }
        val priceInCents2 = 1000L  // 10 euros
        cashRegister.performTransaction(priceInCents2, amountPaid2)

        // Check that the register's change reflects the transactions with paid change added and given change subtracted
        val expectedRegisterChange2 = Change().apply {
            add(Bill.TEN_EURO, 7) // 2 added from amount paid
            add(Coin.FIFTY_CENT, 10)
            add(Bill.FIVE_EURO,1) // 1 subtracted as change
        }
        assertEquals(expectedRegisterChange2.toImmutable(), cashRegister.checkChange())

        //add another transaction
        val amountPaid3 = Change().apply {
            add(Bill.TEN_EURO, 2)
        }
        val priceInCents3 = 1500L  // 15 euros
        cashRegister.performTransaction(priceInCents3, amountPaid3)
        val expectedRegisterChange3 = Change().apply {
            add(Bill.TEN_EURO, 9) // 2 added from amount paid
            add(Coin.FIFTY_CENT, 10)
            add(Bill.FIVE_EURO,0) // 1 subtracted as change
        }

    }

    @Test
    fun `test transaction failure due to insufficient payment`() {
        val initialChange = Change().apply {
            add(Bill.TEN_EURO, 5)
            add(Coin.FIVE_CENT, 10)
        }
        val cashRegister = CashRegister(initialChange)
        val amountPaid = Change().apply {
            add(Coin.TEN_CENT, 1)
        }
        val priceInCents = 500L  // 5 euros

        assertThrows(CashRegister.TransactionException::class.java) {
            cashRegister.performTransaction(priceInCents, amountPaid)
        }
    }



    @Test
    fun `test transaction failure due to insufficient change in register`() {
        val initialChange = Change().apply {
            add(Coin.TWO_CENT, 19)
            add(Coin.ONE_CENT,10)
        }
        val cashRegister = CashRegister(initialChange)
        val amountPaid = Change().apply {
            add(Bill.TWENTY_EURO, 1)
        }
        val priceInCents = 1950L  // 19.5 euros

        assertThrows(CashRegister.TransactionException::class.java) {
            cashRegister.performTransaction(priceInCents, amountPaid)
        }
    }

    @Test
    fun `test minimal change returned when possible`() {
        val initialChange = Change().apply {
            add(Bill.FIFTY_EURO, 1)
            add(Coin.TWENTY_CENT, 10)
            add(Coin.TEN_CENT, 10)
            add(Coin.ONE_CENT, 50)
        }
        val cashRegister = CashRegister(initialChange)
        val amountPaid = Change().apply {
            add(Bill.FIFTY_EURO, 1)
        }
        val priceInCents = 4990L  // 49.9 euros

        val expectedChange = Change().apply {
            add(Coin.TEN_CENT, 1)
        }

        val changeGivenBack = cashRegister.performTransaction(priceInCents, amountPaid)

        assertEquals(expectedChange, changeGivenBack)

        // Check that the register's change reflects the transaction with paid change added and given change subtracted
        val expectedRegisterChange = Change().apply {
            add(Bill.FIFTY_EURO, 2)
            add(Coin.TWENTY_CENT, 10)
            add(Coin.TEN_CENT, 9)
            add(Coin.ONE_CENT, 50)
        }
        assertEquals(expectedRegisterChange.toImmutable(), cashRegister.checkChange())
    }
}

