package com.example.mazadyapp.base

sealed class BaseResult<out T> {
    data class DataState<T : Any>(val items: T?) : BaseResult<T>()
    data class ErrorState(val errorMessage:String?) : BaseResult<Nothing>()
}