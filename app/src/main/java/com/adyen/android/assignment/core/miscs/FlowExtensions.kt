package com.adyen.android.assignment.core.miscs

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

fun <T> emitFlow(suspendBlock: suspend () -> T) : Flow<T> {
    return flow {
        emit(suspendBlock())
    }
}

fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
    var i = 0
    delay(initialDelay)
    while (true) {
        emit(i++)
        delay(period)
    }
}