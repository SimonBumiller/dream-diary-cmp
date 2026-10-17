package me.bumiller.ddcmp.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

/**
 * Fixes the issue that when a list of flows that is empty is combined, the combine method waits forever.
 *
 * Now, if the flows list is empty and the [transform] lambda also returns a list, a flow of an empty list is returned
 */
inline fun <reified T, R> combineSafe(
    flows: Iterable<Flow<T>>,
    crossinline transform: suspend (List<T>) -> List<R>
): Flow<List<R>> = if (flows.none()) flowOf(emptyList())
else combine(flows) {
    transform(it.toList())
}