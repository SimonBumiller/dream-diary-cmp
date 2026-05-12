package me.bumiller.ddcmp.domain.util

import me.bumiller.ddcmp.domain.model.meta.Searchable

fun <T : Searchable> List<T>.search(query: String): List<T> {
    val scored = map { element ->
        element.searchableFields.forEachIndexed { index, field ->
            if (field.lowercase().startsWith(query.lowercase())) return@map element to index
        }

        element.searchableFields.forEachIndexed { index, field ->
            if (field.lowercase()
                    .contains(query.lowercase())
            ) return@map element to index + element.searchableFields.size
        }

        return@map element to -1
    }

    return scored
        .filterNot { it.second == -1 }
        .sortedBy { it.second }
        .map { it.first }
}