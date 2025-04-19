package com.example.application.evaluation.service

import com.example.application.evaluation.repository.EvaluationRepository
import com.example.domain.evaluation.entity.Evaluation
import com.example.domain.evaluation.gateway.GetEvaluationsBySubmissionIdsGateway
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetEvaluationsBySubmissionIdsService(
    private val evaluationRepository: EvaluationRepository
): GetEvaluationsBySubmissionIdsGateway {
    override fun execute(ids: List<UUID>): Result<List<Evaluation>> {
        return runCatching {
            evaluationRepository.findBySubmissionIds(ids)
                .map { it.toDomain() }
        }
    }

}