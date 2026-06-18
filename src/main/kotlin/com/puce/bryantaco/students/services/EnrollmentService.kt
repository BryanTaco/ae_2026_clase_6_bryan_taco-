package com.puce.bryantaco.students.services

import com.puce.bryantaco.students.dto.EnrollmentRequest
import com.puce.bryantaco.students.dto.EnrollmentResponse
import com.puce.bryantaco.students.dto.EnrollmentUpdateRequest
import com.puce.bryantaco.students.entities.Enrollment
import com.puce.bryantaco.students.exceptions.EnrollmentNotFound
import com.puce.bryantaco.students.exceptions.StudentNotFoundException
import com.puce.bryantaco.students.exceptions.SubjectNotFound
import com.puce.bryantaco.students.mappers.newEnrollment
import com.puce.bryantaco.students.mappers.toResponse
import com.puce.bryantaco.students.repositories.EnrollmentRepository
import com.puce.bryantaco.students.repositories.StudentRepository
import com.puce.bryantaco.students.repositories.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EnrollmentService(
    private val enrollmentRepository: EnrollmentRepository,
    private val studentRepository: StudentRepository,
    private val subjectRepository: SubjectRepository,
) {
    private val logger = LoggerFactory.getLogger(EnrollmentService::class.java)

    fun createEnrollment(request: EnrollmentRequest): EnrollmentResponse {
        logger.info("Creating enrollment: student=${request.studentId} subject=${request.subjectId}")
        val student = studentRepository.findById(request.studentId)
            .orElseThrow { StudentNotFoundException(request.studentId) }
        val subject = subjectRepository.findById(request.subjectId)
            .orElseThrow { SubjectNotFound(request.subjectId) }
        return enrollmentRepository.save(newEnrollment(student, subject)).toResponse()
    }

    fun getAllEnrollments(): List<EnrollmentResponse> {
        logger.info("Getting all enrollments")
        return enrollmentRepository.findAll().map { it.toResponse() }
    }

    fun getEnrollmentById(id: Long): EnrollmentResponse {
        logger.info("Getting enrollment by id: $id")
        return enrollmentRepository.findById(id)
            .orElseThrow { EnrollmentNotFound(id) }
            .toResponse()
    }

    fun updateEnrollment(id: Long, request: EnrollmentUpdateRequest): EnrollmentResponse {
        require(request.status.isNotBlank()) { "Status cannot be blank" }
        logger.info("Updating enrollment id: $id status: ${request.status}")
        val existing = enrollmentRepository.findById(id)
            .orElseThrow { EnrollmentNotFound(id) }
        val updated = Enrollment(
            id = existing.id,
            student = existing.student,
            subject = existing.subject,
            status = request.status,
            createdAt = existing.createdAt,
        )
        return enrollmentRepository.save(updated).toResponse()
    }

    fun deleteEnrollment(id: Long) {
        logger.info("Deleting enrollment id: $id")
        if (!enrollmentRepository.existsById(id)) throw EnrollmentNotFound(id)
        enrollmentRepository.deleteById(id)
    }
}
