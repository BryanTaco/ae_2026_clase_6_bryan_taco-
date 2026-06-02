package com.puce.bryantaco.students.controllers

import com.puce.bryantaco.students.dto.StudentRequest
import com.puce.bryantaco.students.dto.StudentResponse
import com.puce.bryantaco.students.services.StudentService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/students")
class StudentController(
    private val studentService: StudentService
) {
    private val logger = LoggerFactory.getLogger(StudentController::class.java)

    @PostMapping
    fun createStudent(
        @RequestBody request: StudentRequest
    ): StudentResponse {
        logger.info("Creating Student ${request.name}")
        return studentService.createStudent(request)
    }

    @GetMapping
    fun getAllStudents(): List<StudentResponse> {
        logger.info("Getting all students")
        return studentService.getAllStudents()
    }
}