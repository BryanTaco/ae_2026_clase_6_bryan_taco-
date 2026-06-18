package com.puce.bryantaco.students.controllers

import com.puce.bryantaco.students.dto.ProfessorRequest
import com.puce.bryantaco.students.dto.ProfessorResponse
import com.puce.bryantaco.students.services.ProfessorService
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
@RequestMapping("/api/professors")
class ProfessorController(
    private val professorService: ProfessorService
) {
    private val logger = LoggerFactory.getLogger(ProfessorController::class.java)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createProfessor(@RequestBody request: ProfessorRequest): ProfessorResponse {
        logger.info("POST /api/professors - ${request.name}")
        return professorService.createProfessor(request)
    }

    @GetMapping
    fun getAllProfessors(): List<ProfessorResponse> {
        logger.info("GET /api/professors")
        return professorService.getAllProfessors()
    }

    @GetMapping("/{id}")
    fun getProfessorById(@PathVariable id: Long): ProfessorResponse {
        logger.info("GET /api/professors/$id")
        return professorService.getProfessorById(id)
    }

    @PutMapping("/{id}")
    fun updateProfessor(@PathVariable id: Long, @RequestBody request: ProfessorRequest): ProfessorResponse {
        logger.info("PUT /api/professors/$id")
        return professorService.updateProfessor(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProfessor(@PathVariable id: Long) {
        logger.info("DELETE /api/professors/$id")
        professorService.deleteProfessor(id)
    }
}
