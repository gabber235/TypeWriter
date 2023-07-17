package me.gabber235.typewriter.utils


fun <T> ok(value: T): Result<T> = Result.success(value)
fun <T> failure(error: String): Result<T> = Result.failure(Exception(error))


inline operator fun <O, N> Result<O>.invoke(onFailure: (Result<N>) -> Nothing): O {
    if (isFailure) {
        onFailure(Result.failure(exceptionOrNull()!!))
    }
    return getOrThrow()
}

inline infix fun <O, N> Result<O>.onFail(onFailure: (Result<N>) -> Nothing): O = invoke(onFailure)