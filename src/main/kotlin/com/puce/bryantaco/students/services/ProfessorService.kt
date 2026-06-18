package com.puce.bryantaco.students.services

import com.puce.bryantaco.students.dto.ProfessorRequest
import com.puce.bryantaco.students.dto.ProfessorResponse
import com.puce.bryantaco.students.entities.Professor
import com.puce.bryantaco.students.exceptions.ProfessorNotFound
import com.puce.bryantaco.students.mappers.toEntity
import com.puce.bryantaco.students.mappers.toResponse
import com.puce.bryantaco.students.repositories.ProfessorRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProfessorService(
    private val professorRepository: ProfessorRepository
) {
    private val logger = LoggerFactory.getLogger(ProfessorService::class.java)

    fun createProfessor(request: ProfessorRequest): ProfessorResponse {
        require(request.name.isNotBlank()) { "Name cannot be blank" }
        logger.info("Creating professor: ${request.name}")
        return professorRepository.save(request.toEntity()).toResponse()
    }

    fun getAllProfessors(): List<ProfessorResponse> {
        logger.info("Getting all professors")
        return professorRepository.findAll().map { it.toResponse() }
    }

    fun getProfessorById(id: Long): ProfessorResponse {
        logger.info("Getting professor by id: $id")
        return professorRepository.findById(id)
            .orElseThrow { ProfessorNotFound(id) }
            .toResponse()
    }

    fun updateProfessor(id: Long, request: ProfessorRequest): ProfessorResponse {
        require(request.name.isNotBlank()) { "Name cannot be blank" }
        logger.info("Updating professor id: $id")
        val existing = professorRepository.findById(id)
            .orElseThrow { ProfessorNotFound(id) }
        val updated = Professor(id = existing.id, name = request.name, email = request.email)
        return professorRepository.save(updated).toResponse()
    }

    fun deleteProfessor(id: Long) {
        logger.info("Deleting professor id: $id")
        if (!professorRepository.existsById(id)) throw ProfessorNotFound(id)
        professorRepository.deleteById(id)
    }
}
