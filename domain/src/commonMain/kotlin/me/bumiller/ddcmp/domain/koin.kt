package me.bumiller.ddcmp.domain

import me.bumiller.ddcmp.domain.stats.dream.global.DreamLengthFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.DreamPersonCountFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.DreamStreakStatistic
import me.bumiller.ddcmp.domain.stats.dream.global.DreamTimeFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.DreamWeekdayFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.DreamWeeklyFrequencyStatistic
import me.bumiller.ddcmp.domain.stats.dream.global.MarkedDreamLengthFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.dream.global.MarkedDreamPersonCountFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.person.PersonInfluencingEmotionsStatistic
import me.bumiller.ddcmp.domain.stats.person.PersonInfluencingPersonsStatistic
import me.bumiller.ddcmp.domain.stats.person.PersonOccurrenceStatistic
import me.bumiller.ddcmp.domain.stats.person.PersonTimeFrequencyHistogram
import me.bumiller.ddcmp.domain.stats.person.PersonsOccurrencesCountStatistic
import me.bumiller.ddcmp.domain.usecase.attachment.AddAttachmentUseCase
import me.bumiller.ddcmp.domain.usecase.attachment.DownloadAttachmentUseCase
import me.bumiller.ddcmp.domain.usecase.attachment.GetAttachmentsUseCase
import me.bumiller.ddcmp.domain.usecase.attachment.RemoveAttachmentUseCase
import me.bumiller.ddcmp.domain.usecase.backup.CancelBackupOrdersUseCase
import me.bumiller.ddcmp.domain.usecase.backup.GetBackupOrderUseCase
import me.bumiller.ddcmp.domain.usecase.backup.GetBackupsForOrderUseCase
import me.bumiller.ddcmp.domain.usecase.backup.PerformBackupOrderUseCase
import me.bumiller.ddcmp.domain.usecase.backup.RecordPeriodicBackupUseCase
import me.bumiller.ddcmp.domain.usecase.backup.SaveBackupOrderUseCase
import me.bumiller.ddcmp.domain.usecase.backup.ScheduleBackupOrderUseCase
import me.bumiller.ddcmp.domain.usecase.dream.AllDreamUseCase
import me.bumiller.ddcmp.domain.usecase.dream.DeleteDreamUseCase
import me.bumiller.ddcmp.domain.usecase.dream.DreamDatesUseCase
import me.bumiller.ddcmp.domain.usecase.dream.SaveDreamUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.AllEmotionsUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.DeleteEmotionUseCase
import me.bumiller.ddcmp.domain.usecase.emotion.SaveEmotionUseCase
import me.bumiller.ddcmp.domain.usecase.io.ExportUseCase
import me.bumiller.ddcmp.domain.usecase.io.GetExportLogUsecase
import me.bumiller.ddcmp.domain.usecase.io.GetImportLogUsecase
import me.bumiller.ddcmp.domain.usecase.io.ImportUseCase
import me.bumiller.ddcmp.domain.usecase.person.AllPersonUseCase
import me.bumiller.ddcmp.domain.usecase.person.DeletePersonUseCase
import me.bumiller.ddcmp.domain.usecase.person.SavePersonUseCase
import me.bumiller.ddcmp.domain.usecase.settings.GetSettingsUseCase
import me.bumiller.ddcmp.domain.usecase.settings.SetSettingsUseCase
import me.bumiller.ddcmp.domain.usecase.tag.AllTagUseCase
import me.bumiller.ddcmp.domain.usecase.tag.DeleteTagUseCase
import me.bumiller.ddcmp.domain.usecase.tag.SaveTagUseCase
import me.bumiller.ddcmp.domain.util.AppCoroutineScope
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * The Koin module for the domain component
 */
val domain = module {
    singleOf(::AppCoroutineScope)

    singleOf(::AllDreamUseCase)
    singleOf(::DreamDatesUseCase)
    singleOf(::SaveDreamUseCase)

    singleOf(::AddAttachmentUseCase)
    singleOf(::GetAttachmentsUseCase)
    singleOf(::RemoveAttachmentUseCase)
    singleOf(::DownloadAttachmentUseCase)

    singleOf(::AllEmotionsUseCase)
    singleOf(::SaveEmotionUseCase)

    singleOf(::AllPersonUseCase)
    singleOf(::SavePersonUseCase)

    singleOf(::GetSettingsUseCase)
    singleOf(::SetSettingsUseCase)

    singleOf(::ImportUseCase)
    singleOf(::ExportUseCase)
    singleOf(::GetImportLogUsecase)
    singleOf(::GetExportLogUsecase)

    singleOf(::DeleteTagUseCase)
    singleOf(::DeleteDreamUseCase)
    singleOf(::DeleteEmotionUseCase)
    singleOf(::DeletePersonUseCase)

    singleOf(::GetBackupOrderUseCase)
    singleOf(::GetBackupsForOrderUseCase)
    singleOf(::RecordPeriodicBackupUseCase)
    singleOf(::CancelBackupOrdersUseCase)
    singleOf(::SaveBackupOrderUseCase)
    singleOf(::ScheduleBackupOrderUseCase)
    singleOf(::PerformBackupOrderUseCase)

    singleOf(::AllTagUseCase)
    singleOf(::SaveTagUseCase)

    singleOf(::DreamTimeFrequencyHistogram)
    singleOf(::DreamWeekdayFrequencyHistogram)
    singleOf(::DreamStreakStatistic)
    singleOf(::DreamWeeklyFrequencyStatistic)
    singleOf(::DreamPersonCountFrequencyHistogram)
    singleOf(::MarkedDreamPersonCountFrequencyHistogram)
    singleOf(::DreamLengthFrequencyHistogram)
    singleOf(::MarkedDreamLengthFrequencyHistogram)
    singleOf(::PersonInfluencingEmotionsStatistic)
    singleOf(::PersonInfluencingPersonsStatistic)
    singleOf(::PersonOccurrenceStatistic)
    singleOf(::PersonsOccurrencesCountStatistic)
    singleOf(::PersonTimeFrequencyHistogram)
}