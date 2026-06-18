package com.puce.bryantaco.students.controllers

import com.puce.bryantaco.students.dto.StudentRequest
import com.puce.bryantaco.students.dto.StudentResponse
import com.puce.bryantaco.students.services.StudentService
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
@RequestMapping("/api/students")
class StudentController(
    private val studentService: StudentService
) {
    private val logger = LoggerFactory.getLogger(StudentController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createStudent(@RequestBody request: StudentRequest): StudentResponse {
        logger.info("POST /api/students - ${request.name}")
        return studentService.createStudent(request)
    }

    @GetMapping
    fun getAllStudents(): List<StudentResponse> {
        logger.info("GET /api/students")
        return studentService.getAllStudents()
    }

    @GetMapping("/{id}")
    fun getStudentById(@PathVariable id: Long): StudentResponse {
        logger.info("GET /api/students/$id")
        return studentService.getStudentById(id)
    }

    @PutMapping("/{id}")
    fun updateStudent(@PathVariable id: Long, @RequestBody request: StudentRequest): StudentResponse {
        logger.info("PUT /api/students/$id")
        return studentService.updateStudent(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStudent(@PathVariable id: Long) {
        logger.info("DELETE /api/students/$id")
        studentService.deleteStudent(id)
    }
}
