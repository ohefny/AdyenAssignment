package com.adyen.android.assignment.venues.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> emitFlow(suspendBlock: suspend () -> T) : Flow<T> {
    return flow {
        emit(suspendBlock())
    }
}