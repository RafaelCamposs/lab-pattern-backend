package com.example.application.evaluation.repository

import com.example.application.evaluation.model.EvaluationModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EvaluationRepository : JpaRepository<EvaluationModel, UUID> {}