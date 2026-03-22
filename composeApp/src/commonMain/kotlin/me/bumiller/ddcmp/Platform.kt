package me.bumiller.ddcmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform