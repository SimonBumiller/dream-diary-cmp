package me.bumiller.ddcomp.core.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import me.bumiller.ddcmp.domain.model.dream.Tag
import me.bumiller.ddcomp.core.database.entity.TagEntity

/**
 * Database migration from version 9 to version 10.
 *
 * Version 10 added [TagEntity.target].
 *
 * This migration adds the field, and gives values to all existing tags.
 * If a tag was only added to persons, it gets [Tag.TagTarget.Person].
 * If a tag was only added to dreams, it gets [Tag.TagTarget.Dream].
 * If a tag was applied to both or neither, it gets [Tag.TagTarget.Person]. In this case, its removed from the other one.
 */
@Suppress("ConstPropertyName", "ClassName")
internal object Migration_9_10 : Migration(9, 10) {

    /**
     * Statement updates the table schema to include the 'target' column.
     */
    private const val AlterTableStatement = """
            ALTER TABLE tags
            ADD COLUMN target TEXT NOT NULL DEFAULT ''
        """

    /**
     * Assigns the target 'Person' to every tag that is not referenced by any dream or person.
     */
    private const val GivePersonTargetToNeitherReferencingStatement = """
        UPDATE tags
        SET target = 'Person'
        WHERE 
            NOT EXISTS (
                SELECT 1 FROM persons_tags_m2m
                WHERE tagId = tags.id
            )
            AND
            NOT EXISTS (
                SELECT 1 FROM dreams_tags_m2m
                WHERE tagId = tags.id
            )
    """

    /**
     * Assigns the target 'Person' to every tag that is referenced by both dreams and persons.
     */
    private const val GivePersonTargetToBothReferencingStatement = """
       UPDATE tags
       SET target = 'Person'
       WHERE 
           EXISTS (
               SELECT 1 FROM persons_tags_m2m
               WHERE tagId = tags.id
           )
           AND
           EXISTS (
               SELECT 1 FROM dreams_tags_m2m
               WHERE tagId = tags.id
           )
    """

    /**
     * Assigns the target 'Dream' to every tag that is referenced only by dreams.
     */
    private const val GiveDreamTargetToDreamReferencingStatement = """
        UPDATE tags
        SET target = 'Dream'
        WHERE 
            NOT EXISTS (
                SELECT 1 FROM persons_tags_m2m
                WHERE tagId = tags.id
            )
            AND
            EXISTS (
                SELECT 1 FROM dreams_tags_m2m
                WHERE tagId = tags.id
            )
    """

    /**
     * Assigns the target 'Person' to every tag that is referenced only by persons.
     */
    private const val GivePersonTargetToPersonReferencingStatement =
        """
        UPDATE tags
        SET target = 'Person'
        WHERE 
            EXISTS (
                SELECT 1 FROM persons_tags_m2m
                WHERE tagId = tags.id
            )
            AND
            NOT EXISTS (
                SELECT 1 FROM dreams_tags_m2m
                WHERE tagId = tags.id
            )
    """

    /**
     * Deletes all disallowed dream-references from tags that target persons.
     */
    private const val DeleteIllegalDreamReferencesStatement =
        """
            DELETE FROM dreams_tags_m2m
            WHERE tagId IN (
                    SELECT tagId FROM tags
                    WHERE target = 'Person'
                )
        """

    override fun migrate(connection: SQLiteConnection) = connection.run {
        // Change the table schema
        execSQL(AlterTableStatement)

        /*
         * Give target according to rules:
         *
         * If references only persons -> Person target
         * If references only dreams -> Dream target
         * If references neither -> Person target
         * If references both -> Person target
         */
        execSQL(GivePersonTargetToBothReferencingStatement)
        execSQL(GivePersonTargetToNeitherReferencingStatement)
        execSQL(GivePersonTargetToPersonReferencingStatement)
        execSQL(GiveDreamTargetToDreamReferencingStatement)

        /*
         * Now we have some tags with target 'Person' that reference dreams.
         * We simply remove those references.
         */
        execSQL(DeleteIllegalDreamReferencesStatement)
    }

}