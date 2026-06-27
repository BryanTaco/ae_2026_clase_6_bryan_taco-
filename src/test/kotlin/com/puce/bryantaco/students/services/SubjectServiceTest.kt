package com.puce.bryantaco.students.services

import com.puce.bryantaco.students.dto.SubjectRequest
import com.puce.bryantaco.students.entities.Professor
import com.puce.bryantaco.students.entities.Subject
import com.puce.bryantaco.students.exceptions.ProfessorNotFound
import com.puce.bryantaco.students.exceptions.SubjectNotFound
import com.puce.bryantaco.students.repositories.ProfessorRepository
import com.puce.bryantaco.students.repositories.SubjectRepository
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
class SubjectServiceTest {

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var subjectService: SubjectService

    private val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec")

    // ─── createSubject ────────────────────────────────────────────────────────

    @Test
    fun `createSubject lanza IllegalArgumentException cuando el nombre esta en blanco`() {
        val request = SubjectRequest(name = "  ", code = "MAT101", professorId = 1L)

        assertThrows(IllegalArgumentException::class.java) {
            subjectService.createSubject(request)
        }
    }

    @Test
    fun `createSubject lanza IllegalArgumentException cuando el codigo esta en blanco`() {
        val request = SubjectRequest(name = "Matemáticas", code = "", professorId = 1L)

        assertThrows(IllegalArgumentException::class.java) {
            subjectService.createSubject(request)
        }
    }

    @Test
    fun `createSubject lanza ProfessorNotFound cuando el profesor no existe`() {
        val request = SubjectRequest(name = "Matemáticas", code = "MAT101", professorId = 99L)
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(ProfessorNotFound::class.java) {
            subjectService.createSubject(request)
        }
    }

    @Test
    fun `createSubject retorna SubjectResponse cuando los datos son validos`() {
        val request = SubjectRequest(name = "Matemáticas", code = "MAT101", professorId = 1L)
        val saved = Subject(id = 10L, name = "Matemáticas", code = "MAT101", professor = professor)
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(subjectRepository.save(any())).thenReturn(saved)

        val result = subjectService.createSubject(request)

        assertEquals(10L, result.id)
        assertEquals("Matemáticas", result.name)
        assertEquals("MAT101", result.code)
        assertEquals(1L, result.professor.id)
    }

    // ─── getAllSubjects ───────────────────────────────────────────────────────

    @Test
    fun `getAllSubjects retorna lista de SubjectResponse`() {
        val subjects = listOf(
            Subject(id = 1L, name = "Matemáticas", code = "MAT101", professor = professor),
            Subject(id = 2L, name = "Física", code = "FIS101", professor = professor),
        )
        `when`(subjectRepository.findAll()).thenReturn(subjects)

        val result = subjectService.getAllSubjects()

        assertEquals(2, result.size)
        assertEquals("Matemáticas", result[0].name)
        assertEquals("Física", result[1].name)
    }

    // ─── getSubjectById ───────────────────────────────────────────────────────

    @Test
    fun `getSubjectById retorna SubjectResponse cuando la materia existe`() {
        val subject = Subject(id = 5L, name = "Química", code = "QUI101", professor = professor)
        `when`(subjectRepository.findById(5L)).thenReturn(Optional.of(subject))

        val result = subjectService.getSubjectById(5L)

        assertEquals(5L, result.id)
        assertEquals("Química", result.name)
    }

    @Test
    fun `getSubjectById lanza SubjectNotFound cuando la materia no existe`() {
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(SubjectNotFound::class.java) {
            subjectService.getSubjectById(99L)
        }
    }

    // ─── updateSubject ────────────────────────────────────────────────────────

    @Test
    fun `updateSubject lanza IllegalArgumentException cuando el nombre esta en blanco`() {
        val request = SubjectRequest(name = "", code = "MAT101", professorId = 1L)

        assertThrows(IllegalArgumentException::class.java) {
            subjectService.updateSubject(1L, request)
        }
    }

    @Test
    fun `updateSubject lanza IllegalArgumentException cuando el codigo esta en blanco`() {
        val request = SubjectRequest(name = "Matemáticas", code = "  ", professorId = 1L)

        assertThrows(IllegalArgumentException::class.java) {
            subjectService.updateSubject(1L, request)
        }
    }

    @Test
    fun `updateSubject lanza SubjectNotFound cuando la materia no existe`() {
        val request = SubjectRequest(name = "Matemáticas", code = "MAT101", professorId = 1L)
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(SubjectNotFound::class.java) {
            subjectService.updateSubject(99L, request)
        }
    }

    @Test
    fun `updateSubject lanza ProfessorNotFound cuando el profesor no existe`() {
        val request = SubjectRequest(name = "Matemáticas", code = "MAT101", professorId = 99L)
        val existing = Subject(id = 1L, name = "Old", code = "OLD101", professor = professor)
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(ProfessorNotFound::class.java) {
            subjectService.updateSubject(1L, request)
        }
    }

    @Test
    fun `updateSubject retorna SubjectResponse actualizada cuando los datos son validos`() {
        val request = SubjectRequest(name = "Matemáticas Avanzadas", code = "MAT201", professorId = 1L)
        val existing = Subject(id = 1L, name = "Matemáticas", code = "MAT101", professor = professor)
        val updated = Subject(id = 1L, name = "Matemáticas Avanzadas", code = "MAT201", professor = professor)
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(subjectRepository.save(any())).thenReturn(updated)

        val result = subjectService.updateSubject(1L, request)

        assertEquals("Matemáticas Avanzadas", result.name)
        assertEquals("MAT201", result.code)
    }

    // ─── deleteSubject ────────────────────────────────────────────────────────

    @Test
    fun `deleteSubject lanza SubjectNotFound cuando la materia no existe`() {
        `when`(subjectRepository.existsById(99L)).thenReturn(false)

        assertThrows(SubjectNotFound::class.java) {
            subjectService.deleteSubject(99L)
        }
    }

    @Test
    fun `deleteSubject elimina la materia cuando existe`() {
        `when`(subjectRepository.existsById(1L)).thenReturn(true)

        subjectService.deleteSubject(1L)

        verify(subjectRepository).deleteById(1L)
    }
}
