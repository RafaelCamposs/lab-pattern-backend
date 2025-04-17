package com.example.application.submission.model

import com.example.domain.submission.entity.Submission
import com.example.domain.submission.entity.dto.StoreSubmissionDto
import com.example.domain.submission.entity.enum.SubmissionStatusEnum
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity(name = "submission")
data class SubmissionModel(
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    val id: UUID? = null,

    val userId: UUID,

    val challengeId: UUID,

    val code: String,

    val language: String,

    val submittedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false, columnDefinition = "varchar(25)")
    val status: SubmissionStatusEnum = SubmissionStatusEnum.PENDING,
) {
    companion object {
        fun fromStoreSubmissionDto(storeSubmissionDto: StoreSubmissionDto) = SubmissionModel(
            userId = storeSubmissionDto.userId,
            challengeId = storeSubmissionDto.challengeId,
            code = storeSubmissionDto.code,
            language = storeSubmissionDto.language
        )
    }

    fun toDomain() = Submission(
        id = id,
        userId = userId,
        challengeId = challengeId,
        code = code,
        language = language,
        submittedAt = submittedAt,
        status = status
    )
}
