package com.example.application.submission.entrypoint.rest

import com.example.application.submission.entrypoint.rest.dto.request.StoreSubmissionRequestDto
import com.example.application.submission.entrypoint.rest.dto.response.SubmissionResponseDto
import com.example.domain.submission.usecase.GetSubmissionByIdUseCase
import com.example.domain.submission.usecase.StoreSubmissionUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin(allowedHeaders = ["*"], origins = ["*"])
@RestController
@RequestMapping("/v1/submissions")
class SubmissionController (
    private val storeSubmissionUseCase: StoreSubmissionUseCase,
    private val getSubmissionByIdUseCase: GetSubmissionByIdUseCase,
) {
     @PostMapping
     fun storeSubmission(@RequestBody request: StoreSubmissionRequestDto): ResponseEntity<SubmissionResponseDto> {
         return try {
             val submission = storeSubmissionUseCase.execute(request.toDomain()).getOrThrow()
             ResponseEntity(SubmissionResponseDto.fromDomain(submission), HttpStatus.CREATED)
         } catch (e: Exception) {
             ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build()
         }
     }

     @GetMapping("/{id}")
     fun getSubmissionById(@PathVariable id: UUID): ResponseEntity<SubmissionResponseDto> {
         return getSubmissionByIdUseCase.execute(id).fold(
                onSuccess = { submission ->
                    ResponseEntity.ok(SubmissionResponseDto.fromDomain(submission))
                },
                onFailure = {
                    ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                }
         )
     }
}