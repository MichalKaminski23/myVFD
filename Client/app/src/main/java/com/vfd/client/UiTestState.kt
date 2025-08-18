package com.vfd.client

sealed interface UiTestState<out T> {
    object Loading : UiTestState<Nothing>
    data class Success<T>(val data: T) : UiTestState<T>
    data class Error(val message: String) : UiTestState<Nothing>
}