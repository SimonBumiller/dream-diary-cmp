package me.bumiller.ddcmp.ui.common

import io.github.vinceglb.filekit.PlatformFile
import org.koin.core.context.GlobalContext

import android.content.Context
import android.content.Intent
import android.provider.DocumentsContract
import androidx.core.content.FileProvider
import io.github.vinceglb.filekit.AndroidFile
import me.bumiller.ddcmp.common.captureException

actual fun viewDirectory(file: PlatformFile): Boolean {
    val context = GlobalContext.get().get<Context>()

    val fileUri = when (file.androidFile) {
        is AndroidFile.FileWrapper -> {
            val file = (file.androidFile as AndroidFile.FileWrapper).file
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file.parentFile)
        }
        is AndroidFile.UriWrapper -> (file.androidFile as AndroidFile.UriWrapper).uri
    }

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(
            fileUri,
            DocumentsContract.Document.MIME_TYPE_DIR
        )
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    return try {
        context.startActivity(intent)
        true
    } catch(e: Exception) {
        e.printStackTrace()
        captureException(e)
        false
    }
}