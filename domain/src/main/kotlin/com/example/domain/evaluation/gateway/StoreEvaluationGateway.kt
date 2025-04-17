package com.example.domain.evaluation.gateway

import com.example.domain.evaluation.entity.Evaluation
import com.example.domain.evaluation.entity.dto.StoreEvaluationDto

interface StoreEvaluationGateway {
    fun execute(storeEvaluationDto: StoreEvaluationDto): Result<Evaluation>
}