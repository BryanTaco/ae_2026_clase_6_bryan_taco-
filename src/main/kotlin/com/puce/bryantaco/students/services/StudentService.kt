package com.puce.bryantaco.students.services

import com.puce.bryantaco.students.dto.StudentRequest
import com.puce.bryantaco.students.dto.StudentResponse
import com.puce.bryantaco.students.entities.Student
import com.puce.bryantaco.students.exceptions.StudentNotFoundException
import com.puce.bryantaco.students.mappers.toEntity
import com.puce.bryantaco.students.mappers.toResponse
import com.puce.bryantaco.students.repositories.StudentRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val studentRepository: StudentRepository
) {
    private val logger = LoggerFactory.getLogger(StudentService::class.java)

    fun createStudent(request: StudentRequest): StudentResponse {
        require(request.name.isNotBlank()) { "Name cannot be blank" }
        logger.info("Creating student: ${request.name}")
        val saved = studentRepository.save(request.toEntity())
        logger.info("Student saved with id: ${saved.id}")
        return saved.toResponse()
    }

    fun getAllStudents(): List<StudentResponse> {
        logger.info("Getting all students")
        return studentRepository.findAll().map { it.toResponse() }
    }

    fun getStudentById(id: Long): StudentResponse {
        logger.info("Getting student by id: $id")
        return studentRepository.findById(id)
            .orElseThrow { StudentNotFoundException(id) }
            .toResponse()
    }

    fun updateStudent(id: Long, request: StudentRequest): StudentResponse {
        require(request.name.isNotBlank()) { "Name cannot be blank" }
        logger.info("Updating student id: $id")
        val existing = studentRepository.findById(id)
            .orElseThrow { StudentNotFoundException(id) }
        val updated = Student(id = existing.id, name = request.name, email = request.email)
        return studentRepository.save(updated).toResponse()
    }

    fun deleteStudent(id: Long) {
        logger.info("Deleting student id: $id")
        if (!studentRepository.existsById(id)) throw StudentNotFoundException(id)
        studentRepository.deleteById(id)
    }
}
