package com.puce.bryantaco.students.controllers

import com.puce.bryantaco.students.dto.SubjectRequest
import com.puce.bryantaco.students.dto.SubjectResponse
import com.puce.bryantaco.students.services.SubjectService
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
@RequestMapping("/api/subjects")
class SubjectController(
    private val subjectService: SubjectService
) {
    private val logger = LoggerFactory.getLogger(SubjectController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createSubject(@RequestBody request: SubjectRequest): SubjectResponse {
        logger.info("POST /api/subjects - ${request.name}")
        return subjectService.createSubject(request)
    }

    @GetMapping
    fun getAllSubjects(): List<SubjectResponse> {
        logger.info("GET /api/subjects")
        return subjectService.getAllSubjects()
    }

    @GetMapping("/{id}")
    fun getSubjectById(@PathVariable id: Long): SubjectResponse {
        logger.info("GET /api/subjects/$id")
        return subjectService.getSubjectById(id)
    }

    @PutMapping("/{id}")
    fun updateSubject(@PathVariable id: Long, @RequestBody request: SubjectRequest): SubjectResponse {
        logger.info("PUT /api/subjects/$id")
        return subjectService.updateSubject(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSubject(@PathVariable id: Long) {
        logger.info("DELETE /api/subjects/$id")
        subjectService.deleteSubject(id)
    }
}
