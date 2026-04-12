package me.bumiller.ddcmp.domain.usecase.meta

import kotlinx.coroutines.flow.Flow

/**
 * Models an abstract usecase.
 */
fun interface UseCase<in I, out O> {

    /**
     * Invokes the use-case for a given input.
     */
    suspend operator fun invoke(i: I): O

}

/**
 * Use-case that produces a flow.
 */
fun interface FlowUseCase<in I, out O> {

    /**
     * Invokes the use-case for a given input.
     */
    operator fun invoke(i: I): Flow<O>

}