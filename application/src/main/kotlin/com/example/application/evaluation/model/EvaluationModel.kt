package com.example.application.evaluation.model

import com.example.domain.evaluation.entity.Evaluation
import com.example.domain.evaluation.entity.dto.StoreEvaluationDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity(name = "evaluation")
data class EvaluationModel(

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    val id: UUID? = null,

    val submissionId: UUID,

    val detectedPatternId: UUID,

    val score: Int,

    val feedback: String,

    val evaluatedAt: LocalDateTime = LocalDateTime.now()

) {
    companion object {
        fun fromStoreEvaluationDto(storeEvaluationDto: StoreEvaluationDto) = EvaluationModel(
            submissionId = storeEvaluationDto.submissionId,
            detectedPatternId = storeEvaluationDto.patternId,
            score = storeEvaluationDto.aiEvaluationDto.score,
            feedback = storeEvaluationDto.aiEvaluationDto.feedback
        )
    }
    
    fun toDomain() = Evaluation(
        id = id,
        submissionId = submissionId,
        detectedPatternId = detectedPatternId,
        score = score,
        feedback = feedback,
        evaluatedAt = evaluatedAt
    )
}
