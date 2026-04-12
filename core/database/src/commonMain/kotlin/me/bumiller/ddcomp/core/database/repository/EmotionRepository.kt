package me.bumiller.ddcomp.core.database.repository

import me.bumiller.ddcmp.domain.model.Emotion
import me.bumiller.ddcmp.domain.model.Person
import me.bumiller.ddcomp.core.database.Mapping
import me.bumiller.ddcomp.core.database.dao.EmotionDao
import me.bumiller.ddcomp.core.database.dao.PersonDao
import me.bumiller.ddcomp.core.database.entity.EmotionEntity
import me.bumiller.ddcomp.core.database.entity.PersonEntity
import me.bumiller.ddcomp.core.database.repository.base.SimpleBaseRepository

class EmotionRepository(
    emotionDao: EmotionDao
): SimpleBaseRepository<Emotion, EmotionEntity>(
    dao = emotionDao,
    map = Mapping::emotion,
    reverseMap = Mapping.Reverse::emotion
)