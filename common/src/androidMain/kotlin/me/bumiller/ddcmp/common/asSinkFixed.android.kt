package me.bumiller.ddcmp.common

import android.content.Context
import io.github.vinceglb.filekit.AndroidFile
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.sink
import kotlinx.io.RawSink
import kotlinx.io.asSink
import org.koin.core.context.GlobalContext
import java.io.FileOutputStream

/**
 * Android platform fix for the [sink] function.
 *
 * The normal [sink] function reopens a uri as a document file again, which causes problems with nextcloud content providers.
 * This fixed implementation directly resolves the uri in an output stream, which is then converted into a sink.
 */
actual fun PlatformFile.asSinkFixed(): RawSink = when (androidFile) {
    is AndroidFile.FileWrapper -> FileOutputStream((androidFile as AndroidFile.FileWrapper).file).asSink()
    is AndroidFile.UriWrapper -> {
        val context = GlobalContext.get().get<Context>()

        return context.contentResolver
            .openOutputStream((androidFile as AndroidFile.UriWrapper).uri, "wt")
            ?.asSink()
            ?: error("Cannot open output stream")

    }
}