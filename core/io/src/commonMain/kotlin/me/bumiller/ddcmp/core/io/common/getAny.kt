package me.bumiller.ddcmp.core.io.common

fun <K, V> Map<K, V>.getAny(keys: List<K>): V? {
    keys.forEach { key ->
        get(key)?.let { return it }
    }

    return null
}

internal fun Map<String, ByteArray>.getFileContent(name: String, vararg extensions: String): ByteArray?
= getAny(extensions.map { "$name.it" })