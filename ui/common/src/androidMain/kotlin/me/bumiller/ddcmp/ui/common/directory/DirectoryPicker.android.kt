package me.bumiller.ddcmp.ui.common.directory

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.github.vinceglb.filekit.PlatformFile

actual class DirectoryPickerResultLauncher(
    private val delegate: ManagedActivityResultLauncher<Uri?, Uri?>
) {
    actual fun launch() {
        delegate.launch(null)
    }
}

private class OpenDocumentTreePersistable: ActivityResultContracts.OpenDocumentTree() {

    override fun createIntent(context: Context, input: Uri?): Intent {
        val superIntent = super.createIntent(context, input)

        superIntent.addFlags(FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        superIntent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION)
        superIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION)

        return superIntent
    }

}

@Composable
actual fun rememberPersistableDirectoryPickerLauncher(
    onResult: (PlatformFile?) -> Unit
): DirectoryPickerResultLauncher {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = OpenDocumentTreePersistable()
    ) { uri ->
        val flags = FLAG_GRANT_READ_URI_PERMISSION or
                FLAG_GRANT_WRITE_URI_PERMISSION

        if(uri != null) {
            context.contentResolver.takePersistableUriPermission(uri, flags)
        }

        val file = uri?.let(::PlatformFile)
        onResult(file)
    }

    return DirectoryPickerResultLauncher(launcher)
}