package com.example.application.tournament.repository

import com.example.application.tournament.model.TournamentModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TournamentRepository  : JpaRepository<TournamentModel, Long> {

    @Query(
        """
            SELECT t.* FROM tournament t 
            WHERE t.created_by_id = :creatorId
            AND t.deleted_at IS NULL
        """,
        nativeQuery = true
    )
    fun findByCreatorId(creatorId: Long): List<TournamentModel>
}