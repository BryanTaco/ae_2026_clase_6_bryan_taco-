package com.puce.bryantaco.students.services

import com.puce.bryantaco.students.dto.EnrollmentRequest
import com.puce.bryantaco.students.dto.EnrollmentUpdateRequest
import com.puce.bryantaco.students.entities.Enrollment
import com.puce.bryantaco.students.entities.Professor
import com.puce.bryantaco.students.entities.Student
import com.puce.bryantaco.students.entities.Subject
import com.puce.bryantaco.students.exceptions.EnrollmentNotFound
import com.puce.bryantaco.students.exceptions.StudentNotFoundException
import com.puce.bryantaco.students.exceptions.SubjectNotFound
import com.puce.bryantaco.students.repositories.EnrollmentRepository
import com.puce.bryantaco.students.repositories.StudentRepository
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
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class EnrollmentServiceTest {

    @Mock
    private lateinit var enrollmentRepository: EnrollmentRepository

    @Mock
    private lateinit var studentRepository: StudentRepository

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @InjectMocks
    private lateinit var enrollmentService: EnrollmentService

    private val professor = Professor(id = 1L, name = "Dr. García", email = "garcia@puce.edu.ec")
    private val student = Student(id = 1L, name = "Ana López", email = "ana@puce.edu.ec")
    private val subject = Subject(id = 10L, name = "Matemáticas", code = "MAT101", professor = professor)

    // ─── createEnrollment ────────────────────────────────────────────────────

    @Test
    fun `createEnrollment lanza StudentNotFoundException cuando el estudiante no existe`() {
        val request = EnrollmentRequest(studentId = 99L, subjectId = 10L)
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            enrollmentService.createEnrollment(request)
        }
    }

    @Test
    fun `createEnrollment lanza SubjectNotFound cuando la materia no existe`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 99L)
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(SubjectNotFound::class.java) {
            enrollmentService.createEnrollment(request)
        }
    }

    @Test
    fun `createEnrollment retorna EnrollmentResponse cuando los datos son validos`() {
        val request = EnrollmentRequest(studentId = 1L, subjectId = 10L)
        val saved = Enrollment(id = 100L, student = student, subject = subject, status = "INSCRITO", createdAt = LocalDateTime.now())
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(10L)).thenReturn(Optional.of(subject))
        `when`(enrollmentRepository.save(any())).thenReturn(saved)

        val result = enrollmentService.createEnrollment(request)

        assertEquals(100L, result.id)
        assertEquals("INSCRITO", result.status)
        assertEquals(1L, result.student.id)
        assertEquals(10L, result.subject.id)
    }

    // ─── getAllEnrollments ────────────────────────────────────────────────────

    @Test
    fun `getAllEnrollments retorna lista de EnrollmentResponse`() {
        val now = LocalDateTime.now()
        val enrollments = listOf(
            Enrollment(id = 1L, student = student, subject = subject, status = "INSCRITO", createdAt = now),
            Enrollment(id = 2L, student = student, subject = subject, status = "APROBADO", createdAt = now),
        )
        `when`(enrollmentRepository.findAll()).thenReturn(enrollments)

        val result = enrollmentService.getAllEnrollments()

        assertEquals(2, result.size)
        assertEquals("INSCRITO", result[0].status)
        assertEquals("APROBADO", result[1].status)
    }

    // ─── getEnrollmentById ────────────────────────────────────────────────────

    @Test
    fun `getEnrollmentById retorna EnrollmentResponse cuando la inscripcion existe`() {
        val enrollment = Enrollment(id = 5L, student = student, subject = subject, status = "INSCRITO", createdAt = LocalDateTime.now())
        `when`(enrollmentRepository.findById(5L)).thenReturn(Optional.of(enrollment))

        val result = enrollmentService.getEnrollmentById(5L)

        assertEquals(5L, result.id)
        assertEquals("INSCRITO", result.status)
    }

    @Test
    fun `getEnrollmentById lanza EnrollmentNotFound cuando la inscripcion no existe`() {
        `when`(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(EnrollmentNotFound::class.java) {
            enrollmentService.getEnrollmentById(99L)
        }
    }

    // ─── updateEnrollment ────────────────────────────────────────────────────

    @Test
    fun `updateEnrollment lanza IllegalArgumentException cuando el status esta en blanco`() {
        val request = EnrollmentUpdateRequest(status = "  ")

        assertThrows(IllegalArgumentException::class.java) {
            enrollmentService.updateEnrollment(1L, request)
        }
    }

    @Test
    fun `updateEnrollment lanza EnrollmentNotFound cuando la inscripcion no existe`() {
        val request = EnrollmentUpdateRequest(status = "APROBADO")
        `when`(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(EnrollmentNotFound::class.java) {
            enrollmentService.updateEnrollment(99L, request)
        }
    }

    @Test
    fun `updateEnrollment retorna EnrollmentResponse actualizada cuando los datos son validos`() {
        val request = EnrollmentUpdateRequest(status = "APROBADO")
        val now = LocalDateTime.now()
        val existing = Enrollment(id = 1L, student = student, subject = subject, status = "INSCRITO", createdAt = now)
        val updated = Enrollment(id = 1L, student = student, subject = subject, status = "APROBADO", createdAt = now)
        `when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(enrollmentRepository.save(any())).thenReturn(updated)

        val result = enrollmentService.updateEnrollment(1L, request)

        assertEquals("APROBADO", result.status)
        assertEquals(1L, result.student.id)
    }

    // ─── deleteEnrollment ────────────────────────────────────────────────────

    @Test
    fun `deleteEnrollment lanza EnrollmentNotFound cuando la inscripcion no existe`() {
        `when`(enrollmentRepository.existsById(99L)).thenReturn(false)

        assertThrows(EnrollmentNotFound::class.java) {
            enrollmentService.deleteEnrollment(99L)
        }
    }

    @Test
    fun `deleteEnrollment elimina la inscripcion cuando existe`() {
        `when`(enrollmentRepository.existsById(1L)).thenReturn(true)

        enrollmentService.deleteEnrollment(1L)

        verify(enrollmentRepository).deleteById(1L)
    }
}
