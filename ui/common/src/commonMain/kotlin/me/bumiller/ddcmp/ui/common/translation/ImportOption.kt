package me.bumiller.ddcmp.ui.common.translation

import androidx.compose.runtime.Composable
import dream_diary_cmp.ui.common.generated.resources.Res
import dream_diary_cmp.ui.common.generated.resources.importfiletype_json_name
import dream_diary_cmp.ui.common.generated.resources.importfiletype_xml_name
import dream_diary_cmp.ui.common.generated.resources.importfiletype_yaml_name
import me.bumiller.ddcmp.domain.model.io.IOFileType
import org.jetbrains.compose.resources.stringResource

/**
 * Name of the selected import file type.
 */
val IOFileType.translateName: String
    @Composable get() = when (this) {
        IOFileType.Json -> Res.string.importfiletype_json_name
        IOFileType.Xml -> Res.string.importfiletype_xml_name
        IOFileType.Yaml -> Res.string.importfiletype_yaml_name
    }.let { stringResource(it) }