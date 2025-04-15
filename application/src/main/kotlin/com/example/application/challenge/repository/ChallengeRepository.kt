package com.example.application.challenge.repository

import com.example.application.challenge.model.ChallengeModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChallengeRepository : JpaRepository<ChallengeModel, UUID> {}