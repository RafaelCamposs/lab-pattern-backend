package com.example.domain.evaluation.gateway

import com.example.domain.evaluation.entity.Evaluation
import java.util.*

interface GetEvaluationsBySubmissionIdsGateway {
    fun execute(ids: List<UUID>): Result<List<Evaluation>>
}