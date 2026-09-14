package com.example.khainow.utils

sealed class DataState<T>(
    val success: T? = null,
    val massage: String? = null
) {

    class Success<T>(suc_massage:T?): DataState<T>(suc_massage)

    class Error<T>(exception: String?): DataState<T>(massage = exception)

    class Loading<T>(): DataState<T>()

}