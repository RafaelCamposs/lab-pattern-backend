package com.example.application.evaluation.repository

import com.example.application.evaluation.model.EvaluationModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface EvaluationRepository : JpaRepository<EvaluationModel, UUID> {

    @Query(
        """
            SELECT e.* FROM evaluation e
            WHERE e.submission_id IN (:ids)
        """,
        nativeQuery = true
    )
    fun findBySubmissionIds(ids: List<UUID>): List<EvaluationModel>
}