package com.example.application.evaluation.service

import com.example.application.evaluation.model.EvaluationModel
import com.example.application.evaluation.repository.EvaluationRepository
import com.example.domain.evaluation.entity.Evaluation
import com.example.domain.evaluation.entity.dto.StoreEvaluationDto
import com.example.domain.evaluation.gateway.StoreEvaluationGateway
import org.springframework.stereotype.Component

@Component
class StoreEvaluationService (
    private val evaluationRepository: EvaluationRepository
): StoreEvaluationGateway {
    override fun execute(storeEvaluationDto: StoreEvaluationDto): Result<Evaluation> {
        return runCatching {
            val evaluationModel = EvaluationModel.fromStoreEvaluationDto(storeEvaluationDto)
            evaluationRepository.save(evaluationModel).toDomain()
        }
    }
}