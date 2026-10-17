package me.bumiller.ddcmp.common

import android.content.Context
import androidx.documentfile.provider.DocumentFile
import io.github.vinceglb.filekit.AndroidFile
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.div
import org.koin.core.context.GlobalContext
import java.io.File

/**
 * Android platform fix for the [div] function.
 *
 * The original one does weird things with nextcloud documents and appends the filename.
 * This fix changes the behavior for [AndroidFile.UriWrapper] by simply registering a new child document.
 */
actual suspend fun PlatformFile.createChildFile(
    name: String,
    mimeType: String
): PlatformFile = when (androidFile) {
    is AndroidFile.FileWrapper -> PlatformFile(
        File(
            (androidFile as AndroidFile.FileWrapper).file,
            name
        )
    )

    is AndroidFile.UriWrapper -> {
        val context = GlobalContext.get().get<Context>()
        val directoryUri = (androidFile as AndroidFile.UriWrapper).uri

        val directory = DocumentFile.fromTreeUri(context, directoryUri)
            ?: error("Invalid directory URI")

        val existing = directory.findFile(name)

        val child = existing
            ?: directory.createFile(mimeType, name)
            ?: error("Could not create file")

        val platformFile = PlatformFile(child.uri)

        return platformFile
    }
}