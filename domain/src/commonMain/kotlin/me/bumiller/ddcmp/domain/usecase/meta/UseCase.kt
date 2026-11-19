package me.bumiller.ddcmp.domain.usecase.meta

import kotlinx.coroutines.flow.Flow

/**
 * Models an abstract usecase.
 */
public fun interface UseCase<in I, out O> {

    /**
     * Invokes the use-case for a given input.
     */
    public suspend operator fun invoke(i: I): O

}

/**
 * Use-case that produces a flow.
 */
public fun interface FlowUseCase<in I, out O> {

    /**
     * Invokes the use-case for a given input.
     */
    public operator fun invoke(i: I): Flow<O>

}