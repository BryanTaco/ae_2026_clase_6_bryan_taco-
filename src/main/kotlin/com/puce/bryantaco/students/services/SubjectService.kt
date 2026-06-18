package com.puce.bryantaco.students.services

import com.puce.bryantaco.students.dto.SubjectRequest
import com.puce.bryantaco.students.dto.SubjectResponse
import com.puce.bryantaco.students.entities.Subject
import com.puce.bryantaco.students.exceptions.ProfessorNotFound
import com.puce.bryantaco.students.exceptions.SubjectNotFound
import com.puce.bryantaco.students.mappers.toResponse
import com.puce.bryantaco.students.repositories.ProfessorRepository
import com.puce.bryantaco.students.repositories.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SubjectService(
    private val subjectRepository: SubjectRepository,
    private val professorRepository: ProfessorRepository,
) {
    private val logger = LoggerFactory.getLogger(SubjectService::class.java)

    fun createSubject(request: SubjectRequest): SubjectResponse {
        require(request.name.isNotBlank()) { "Name cannot be blank" }
        require(request.code.isNotBlank()) { "Code cannot be blank" }
        logger.info("Creating subject: ${request.name}")
        val professor = professorRepository.findById(request.professorId)
            .orElseThrow { ProfessorNotFound(request.professorId) }
        return subjectRepository.save(Subject(name = request.name, code = request.code, professor = professor)).toResponse()
    }

    fun getAllSubjects(): List<SubjectResponse> {
        logger.info("Getting all subjects")
        return subjectRepository.findAll().map { it.toResponse() }
    }

    fun getSubjectById(id: Long): SubjectResponse {
        logger.info("Getting subject by id: $id")
        return subjectRepository.findById(id)
            .orElseThrow { SubjectNotFound(id) }
            .toResponse()
    }

    fun updateSubject(id: Long, request: SubjectRequest): SubjectResponse {
        require(request.name.isNotBlank()) { "Name cannot be blank" }
        require(request.code.isNotBlank()) { "Code cannot be blank" }
        logger.info("Updating subject id: $id")
        val existing = subjectRepository.findById(id)
            .orElseThrow { SubjectNotFound(id) }
        val professor = professorRepository.findById(request.professorId)
            .orElseThrow { ProfessorNotFound(request.professorId) }
        return subjectRepository.save(
            Subject(id = existing.id, name = request.name, code = request.code, professor = professor)
        ).toResponse()
    }

    fun deleteSubject(id: Long) {
        logger.info("Deleting subject id: $id")
        if (!subjectRepository.existsById(id)) throw SubjectNotFound(id)
        subjectRepository.deleteById(id)
    }
}
