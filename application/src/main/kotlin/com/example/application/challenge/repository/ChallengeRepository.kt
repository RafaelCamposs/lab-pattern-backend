package com.example.application.challenge.repository

import com.example.application.challenge.model.ChallengeModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChallengeRepository : JpaRepository<ChallengeModel, UUID> {

    @Query(
        """ 
            SELECT c.* FROM challenge c
            LEFT JOIN submission s on s.challenge_id = c.id
            WHERE s.user_id = :id
            GROUP BY c.id
        """,
        nativeQuery = true
    )
    fun findByUserIdWithSubmission(id: UUID) : List<ChallengeModel>

    @Query(
        """
            SELECT * FROM challenge c
            WHERE c.is_daily = true
            ORDER BY c.created_at DESC
            LIMIT 1
        """,
        nativeQuery = true
    )
    fun findLatestDailyChallenge(): ChallengeModel
}