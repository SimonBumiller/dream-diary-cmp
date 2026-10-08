package me.bumiller.ddcomp.core.database.repository

import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.infrastructure.EmotionRepository
import me.bumiller.ddcmp.domain.model.emotion.Emotion
import me.bumiller.ddcomp.core.database.Mapping
import me.bumiller.ddcomp.core.database.dao.DreamDao
import me.bumiller.ddcomp.core.database.dao.EmotionDao
import me.bumiller.ddcomp.core.database.entity.EmotionEntity
import me.bumiller.ddcomp.core.database.repository.base.SimpleBaseRepository

class EmotionRepository(
    emotionDao: EmotionDao,
    val dreamDao: DreamDao
) : SimpleBaseRepository<Emotion, EmotionEntity>(
    dao = emotionDao,
    map = Mapping::emotion,
    reverseMap = Mapping.Reverse::emotion
), EmotionRepository {

    override fun getForDream(dreamId: Long) = dreamDao
        .getByIdAggregate(dreamId)
        .map { dream -> dream?.emotions?.map(Mapping::emotion) }

}