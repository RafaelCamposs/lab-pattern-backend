package com.example.domain.tournament.entity.enum

enum class TournamentStatusEnum {
    CREATED,
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED;

    fun isUpdatable(): Boolean {
        return this == CREATED || this == SCHEDULED
    }

    fun isDeletable(): Boolean {
        return this == CREATED || this == SCHEDULED || this == COMPLETED
    }
}