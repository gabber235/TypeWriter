package me.gabber235.typewriter.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface Result<S, E> {
    data class Success<S, E>(val value: S) : Result<S, E>

    data class Failure<S, E>(val error: E) : Result<S, E>
}

fun <S, E> ok(value: S): Result<S, E> = Result.Success(value)
fun <S, E> failure(error: E): Result<S, E> = Result.Failure(error)


@OptIn(ExperimentalContracts::class)
fun <S, E> Result<S, E>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is Result.Success<S, E>)
        returns(false) implies (this@isSuccess is Result.Failure<S, E>)
    }
    return this is Result.Success
}

@OptIn(ExperimentalContracts::class)
fun <S, E> Result<S, E>.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure is Result.Failure<S, E>)
        returns(false) implies (this@isFailure is Result.Success<S, E>)
    }
    return this is Result.Failure
}

fun <S, E> Result<S, E>.unwrap(): S? {
    return when (this) {
        is Result.Success -> this.value
        is Result.Failure -> null
    }
}


fun <S, E> Result<S, E>.getFailure(): E? {
    return when (this) {
        is Result.Success -> null
        is Result.Failure -> this.error
    }
}

fun <T> Result<T, T>.unwrapBoth(): T {
    return when (this) {
        is Result.Success -> this.value
        is Result.Failure -> this.error
    }
}

inline fun <S, E, R> Result<S, E>.map(transform: (S) -> R): Result<R, E> {
    return when (this) {
        is Result.Success -> Result.Success(transform(this.value))
        is Result.Failure -> Result.Failure(this.error)
    }
}

inline operator fun <O, N, E> Result<O, E>.invoke(onFailure: (Result.Failure<N, E>) -> Nothing): O {
    if (isFailure()) {
        onFailure(this as Result.Failure<N, E>)
    }
    return this.value
}

inline infix fun <O, N, E> Result<O, E>.onFail(onFailure: (Result.Failure<N, E>) -> Nothing): O = invoke(onFailure)