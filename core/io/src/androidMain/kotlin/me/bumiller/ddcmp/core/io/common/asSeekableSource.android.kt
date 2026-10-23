package me.bumiller.ddcmp.core.io.common

import android.content.Context
import androidx.core.net.toUri
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.path
import no.synth.kmpzip.io.SeekableSource
import no.synth.kmpzip.io.fileSeekableSource
import org.koin.core.context.GlobalContext
import java.io.File
import kotlin.use

private class TempFileSeekableSource(
    private val file: File,
    private val delegate: SeekableSource = fileSeekableSource(file.path)
) : SeekableSource by delegate {

    override fun close() {
        delegate.close()
        file.delete()
    }
}

// A PlatformFile on android can be a content file.
// We create a temporary file that we get by making a copy,
// and then return a seekable source to that temporary file.
actual fun PlatformFile.asSeekableSource(): SeekableSource {
    val context = GlobalContext.get().get<Context>()

    // Temporary file
    val tempFile = File(context.cacheDir, name)

    val uri = path.toUri()

    // Writing original content to the temporary file
    context.contentResolver.openInputStream(uri).use { source ->
        tempFile.outputStream().use { target ->
            source?.copyTo(target)
        }
    }

    return TempFileSeekableSource(tempFile)
}