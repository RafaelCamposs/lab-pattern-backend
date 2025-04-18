package com.example.application.evaluation.converter

import com.example.domain.evaluation.entity.Feedback
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter

class FeedbackStringConverter(
    private val objectMapper: ObjectMapper
) : AttributeConverter<Feedback, String> {
    override fun convertToDatabaseColumn(feedback: Feedback): String {
        return objectMapper.writeValueAsString(feedback)
    }

    override fun convertToEntityAttribute(feedbackAsJson: String?): Feedback {
        return objectMapper.readValue(feedbackAsJson, Feedback::class.java)
    }

}