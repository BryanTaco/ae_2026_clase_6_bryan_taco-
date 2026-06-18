package com.puce.bryantaco.students.controllers

import com.puce.bryantaco.students.dto.EnrollmentRequest
import com.puce.bryantaco.students.dto.EnrollmentResponse
import com.puce.bryantaco.students.dto.EnrollmentUpdateRequest
import com.puce.bryantaco.students.services.EnrollmentService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/enrollments")
class EnrollmentController(
    private val enrollmentService: EnrollmentService
) {
    private val logger = LoggerFactory.getLogger(EnrollmentController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createEnrollment(@RequestBody request: EnrollmentRequest): EnrollmentResponse {
        logger.info("POST /api/enrollments - student=${request.studentId} subject=${request.subjectId}")
        return enrollmentService.createEnrollment(request)
    }

    @GetMapping
    fun getAllEnrollments(): List<EnrollmentResponse> {
        logger.info("GET /api/enrollments")
        return enrollmentService.getAllEnrollments()
    }

    @GetMapping("/{id}")
    fun getEnrollmentById(@PathVariable id: Long): EnrollmentResponse {
        logger.info("GET /api/enrollments/$id")
        return enrollmentService.getEnrollmentById(id)
    }

    @PutMapping("/{id}")
    fun updateEnrollment(@PathVariable id: Long, @RequestBody request: EnrollmentUpdateRequest): EnrollmentResponse {
        logger.info("PUT /api/enrollments/$id - status=${request.status}")
        return enrollmentService.updateEnrollment(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteEnrollment(@PathVariable id: Long) {
        logger.info("DELETE /api/enrollments/$id")
        enrollmentService.deleteEnrollment(id)
    }
}
