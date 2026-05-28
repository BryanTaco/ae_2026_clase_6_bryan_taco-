package com.puce.bryantaco.students.services

import com.puce.bryantaco.students.dto.StudentRequest
import com.puce.bryantaco.students.dto.StudentResponse
import com.puce.bryantaco.students.entities.Student
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
        logger.info("Creating Student ${request.name}")

        val studentToSave = request.toEntity()
        val savedStudent = studentRepository.save(studentToSave)

        logger.info("Save student with id ${savedStudent.id}")
        return savedStudent.toResponse()
    } // ← closes createStudent

    fun getAllStudents(): List<StudentResponse> {
        logger.info("Getting all students")

        val savedStudents = studentRepository.findAll()
        return savedStudents.map { it.toResponse() }
    }

}