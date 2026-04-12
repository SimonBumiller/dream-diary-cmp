package me.bumiller.ddcmp.domain.usecase.meta

import me.bumiller.ddcmp.domain.infrastructure.meta.Repository
import me.bumiller.ddcmp.domain.model.meta.Unique

/**
 * Models a fixed set of use-cases that are used to perform simple crud-operations.
 */
interface CrudUseCases<T: Unique> {

    /**
     * Usecase to get all entities.
     */
    val getAll: FlowUseCase<Unit, List<T>>

    /**
     * Usecase to save an entity.
     */
    val save: UseCase<T, Long>

    /**
     * Usecase to get by id.
     */
    val byId: FlowUseCase<Long, T?>

    /**
     * Usecase to delete entity.
     */
    val delete: UseCase<Long, Boolean>

    companion object {

        /**
         * Creates a crud-usecase object that delegates to a specific repository.
         */
        fun <T: Unique>create(repo: Repository<T>): CrudUseCases<T> = object : CrudUseCases<T> {
            override val getAll: FlowUseCase<Unit, List<T>> = FlowUseCase { repo.all }
            override val save: UseCase<T, Long> = UseCase { repo.save(it) }
            override val byId: FlowUseCase<Long, T?> = FlowUseCase { repo.id(it) }
            override val delete: UseCase<Long, Boolean> = UseCase { repo.delete(it) }

        }

    }

}