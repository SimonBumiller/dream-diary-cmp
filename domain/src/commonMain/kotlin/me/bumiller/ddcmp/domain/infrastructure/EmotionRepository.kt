package me.bumiller.ddcmp.domain.infrastructure

import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.domain.model.meta.SortMode

/**
 * Typed emotion repository.
 */
interface EmotionRepository : Repository<Emotion>