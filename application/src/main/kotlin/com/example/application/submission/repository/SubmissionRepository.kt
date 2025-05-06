package com.example.application.submission.repository

import com.example.application.submission.model.SubmissionModel
import com.example.application.submission.repository.projections.SubmissionWithEvaluationDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface SubmissionRepository : JpaRepository<SubmissionModel, UUID> {

    @Query(
        """
            SELECT s.* FROM submission s
            WHERE s.user_id = :userId
            AND s.challenge_id = :challengeId
        """,
        nativeQuery = true
    )
    fun findByUserIdAndChallengeId(
        userId: UUID,
        challengeId: UUID
    ): List<SubmissionModel>

    @Query(
        """
            SELECT 
                s as submissionModel,
                e as evaluationModel
            FROM submission s
            join evaluation e on e.submissionId = s.id
            WHERE s.userId = :userId
        """,
        nativeQuery = false
    )
    fun findAllByUserWithEvaluation(userId: UUID): List<SubmissionWithEvaluationDto>
}