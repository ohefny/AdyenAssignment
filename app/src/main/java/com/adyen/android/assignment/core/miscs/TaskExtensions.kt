package com.adyen.android.assignment.core.miscs

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


fun <T> Task<T?>.asFlow(): Flow<T> = callbackFlow {
    addOnSuccessListener { value ->
        if (value != null) {
            trySend(value)
            close()
        } else
            close(IllegalStateException("Task completed successfully but returned null"))
    }
    addOnFailureListener { exception ->
        close(exception)
    }
    awaitClose { channel.close() }
}