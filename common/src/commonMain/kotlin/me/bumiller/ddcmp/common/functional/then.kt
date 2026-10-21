package me.bumiller.ddcmp.common.functional

infix fun <T1, T2> (() -> T1).then(next: (T1) -> T2): () -> T2 =
    { next(invoke()) }