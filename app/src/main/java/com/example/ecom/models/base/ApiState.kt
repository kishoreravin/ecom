package com.example.ecom.models.base

sealed class ApiState<out R> {
    data class Success<out T>(val result: T, val id: Int = 0) : ApiState<T>()
    data class Error(val exception: Exception, val id: Int? = 0) : ApiState<Nothing>()
    data class Failed<out T>(val result: T, val id: Int = 0) : ApiState<T>()
    object NoNetwork : ApiState<Nothing>()
}