package com.example.application.tournament.repository

import com.example.application.tournament.model.TournamentModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TournamentRepository  : JpaRepository<TournamentModel, Long>