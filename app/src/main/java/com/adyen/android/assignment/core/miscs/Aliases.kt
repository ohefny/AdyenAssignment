package com.adyen.android.assignment.core.miscs

typealias Action = () -> Unit
typealias Consumer<T> = (T) -> Unit
typealias Producer<T> = () -> T