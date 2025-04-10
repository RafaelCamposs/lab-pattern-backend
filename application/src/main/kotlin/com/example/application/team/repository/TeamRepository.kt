package com.example.application.team.repository

import com.example.application.team.model.TeamModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamRepository : JpaRepository<TeamModel, Long> {}