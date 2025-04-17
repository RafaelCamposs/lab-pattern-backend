package com.example.application.submission.repository

import com.example.application.submission.model.SubmissionModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SubmissionRepository : JpaRepository<SubmissionModel, UUID> {}