package com.puce.bryantaco.students.services

import com.puce.bryantaco.students.dto.ProfessorRequest
import com.puce.bryantaco.students.entities.Professor
import com.puce.bryantaco.students.exceptions.ProfessorNotFound
import com.puce.bryantaco.students.repositories.ProfessorRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class ProfessorServiceTest {

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var professorService: ProfessorService

    // ─── createProfessor ─────────────────────────────────────────────────────

    @Test
    fun `createProfessor lanza IllegalArgumentException cuando el nombre esta en blanco`() {
        val request = ProfessorRequest(name = "   ", email = "prof@puce.edu.ec")

        assertThrows(IllegalArgumentException::class.java) {
            professorService.createProfessor(request)
        }
    }

    @Test
    fun `createProfessor retorna ProfessorResponse cuando el nombre es valido`() {
        val request = ProfessorRequest(name = "Dr. García", email = "garcia@puce.edu.ec")
        val saved = Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec")
        `when`(professorRepository.save(any())).thenReturn(saved)

        val result = professorService.createProfessor(request)

        assertEquals(1L, result.id)
        assertEquals("Dr. García", result.name)
        assertEquals("garcia@puce.edu.ec", result.email)
    }

    // ─── getAllProfessors ─────────────────────────────────────────────────────

    @Test
    fun `getAllProfessors retorna lista de ProfessorResponse`() {
        val professors = listOf(
            Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec"),
            Professor(id = 2L, name = "Dra. Torres", email = null),
        )
        `when`(professorRepository.findAll()).thenReturn(professors)

        val result = professorService.getAllProfessors()

        assertEquals(2, result.size)
        assertEquals("Dr. García", result[0].name)
        assertEquals("Dra. Torres", result[1].name)
    }

    // ─── getProfessorById ─────────────────────────────────────────────────────

    @Test
    fun `getProfessorById retorna ProfessorResponse cuando el profesor existe`() {
        val professor = Professor(id = 3L, name = "Dr. Mora", email = "mora@puce.edu.ec")
        `when`(professorRepository.findById(3L)).thenReturn(Optional.of(professor))

        val result = professorService.getProfessorById(3L)

        assertEquals(3L, result.id)
        assertEquals("Dr. Mora", result.name)
    }

    @Test
    fun `getProfessorById lanza ProfessorNotFound cuando el profesor no existe`() {
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(ProfessorNotFound::class.java) {
            professorService.getProfessorById(99L)
        }
    }

    // ─── updateProfessor ─────────────────────────────────────────────────────

    @Test
    fun `updateProfessor lanza IllegalArgumentException cuando el nombre esta en blanco`() {
        val request = ProfessorRequest(name = "", email = "x@puce.edu.ec")

        assertThrows(IllegalArgumentException::class.java) {
            professorService.updateProfessor(1L, request)
        }
    }

    @Test
    fun `updateProfessor lanza ProfessorNotFound cuando el profesor no existe`() {
        val request = ProfessorRequest(name = "Nuevo", email = null)
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(ProfessorNotFound::class.java) {
            professorService.updateProfessor(99L, request)
        }
    }

    @Test
    fun `updateProfessor retorna ProfessorResponse actualizado cuando los datos son validos`() {
        val request = ProfessorRequest(name = "Dr. García Actualizado", email = "nueva@puce.edu.ec")
        val existing = Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec")
        val updated = Professor(id = 1L, name = "Dr. García Actualizado", email = "nueva@puce.edu.ec")
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(professorRepository.save(any())).thenReturn(updated)

        val result = professorService.updateProfessor(1L, request)

        assertEquals("Dr. García Actualizado", result.name)
        assertEquals("nueva@puce.edu.ec", result.email)
    }

    // ─── deleteProfessor ─────────────────────────────────────────────────────

    @Test
    fun `deleteProfessor lanza ProfessorNotFound cuando el profesor no existe`() {
        `when`(professorRepository.existsById(99L)).thenReturn(false)

        assertThrows(ProfessorNotFound::class.java) {
            professorService.deleteProfessor(99L)
        }
    }

    @Test
    fun `deleteProfessor elimina el profesor cuando existe`() {
        `when`(professorRepository.existsById(1L)).thenReturn(true)

        professorService.deleteProfessor(1L)

        verify(professorRepository).deleteById(1L)
    }
}
