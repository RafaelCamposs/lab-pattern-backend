package com.example.application.pattern.repository

import com.example.application.pattern.model.DesignPatternModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DesignPatternRepository: JpaRepository<DesignPatternModel, UUID> {}