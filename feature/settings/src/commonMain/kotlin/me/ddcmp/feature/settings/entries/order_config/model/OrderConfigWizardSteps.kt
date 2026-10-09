package me.ddcmp.feature.settings.entries.order_config.model

import dream_diary_cmp.feature.settings.generated.resources.Res
import dream_diary_cmp.feature.settings.generated.resources.order_config_options_label
import dream_diary_cmp.feature.settings.generated.resources.order_config_schedule_label
import dream_diary_cmp.feature.settings.generated.resources.order_config_storage_label
import me.bumiller.ddcmp.ui.common.component.WizardStep
import org.jetbrains.compose.resources.StringResource

internal enum class OrderConfigWizardSteps(
    override val nameRes: StringResource
): WizardStep {

    Storage(Res.string.order_config_storage_label),

    Schedule(Res.string.order_config_schedule_label),

    Options(Res.string.order_config_options_label)

}