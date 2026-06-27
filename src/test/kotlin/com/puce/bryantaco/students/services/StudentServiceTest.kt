package com.puce.bryantaco.students.services

import com.puce.bryantaco.students.dto.StudentRequest
import com.puce.bryantaco.students.entities.Student
import com.puce.bryantaco.students.exceptions.StudentNotFoundException
import com.puce.bryantaco.students.repositories.StudentRepository
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
class StudentServiceTest {

    @Mock
    private lateinit var studentRepository: StudentRepository

    @InjectMocks
    private lateinit var studentService: StudentService

    // ─── createStudent ────────────────────────────────────────────────────────

    @Test
    fun `createStudent lanza IllegalArgumentException cuando el nombre esta en blanco`() {
        val request = StudentRequest(name = "  ", email = "test@puce.edu.ec")

        assertThrows(IllegalArgumentException::class.java) {
            studentService.createStudent(request)
        }
    }

    @Test
    fun `createStudent retorna StudentResponse cuando el nombre es valido`() {
        val request = StudentRequest(name = "Ana López", email = "ana@puce.edu.ec")
        val saved = Student(id = 1L, name = "Ana López", email = "ana@puce.edu.ec")
        `when`(studentRepository.save(any())).thenReturn(saved)

        val result = studentService.createStudent(request)

        assertEquals(1L, result.id)
        assertEquals("Ana López", result.name)
        assertEquals("ana@puce.edu.ec", result.email)
    }

    // ─── getAllStudents ───────────────────────────────────────────────────────

    @Test
    fun `getAllStudents retorna lista de StudentResponse`() {
        val students = listOf(
            Student(id = 1L, name = "Ana", email = "ana@puce.edu.ec"),
            Student(id = 2L, name = "Pedro", email = null),
        )
        `when`(studentRepository.findAll()).thenReturn(students)

        val result = studentService.getAllStudents()

        assertEquals(2, result.size)
        assertEquals("Ana", result[0].name)
        assertEquals("Pedro", result[1].name)
    }

    // ─── getStudentById ───────────────────────────────────────────────────────

    @Test
    fun `getStudentById retorna StudentResponse cuando el estudiante existe`() {
        val student = Student(id = 5L, name = "Luis", email = "luis@puce.edu.ec")
        `when`(studentRepository.findById(5L)).thenReturn(Optional.of(student))

        val result = studentService.getStudentById(5L)

        assertEquals(5L, result.id)
        assertEquals("Luis", result.name)
    }

    @Test
    fun `getStudentById lanza StudentNotFoundException cuando el estudiante no existe`() {
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            studentService.getStudentById(99L)
        }
    }

    // ─── updateStudent ────────────────────────────────────────────────────────

    @Test
    fun `updateStudent lanza IllegalArgumentException cuando el nombre esta en blanco`() {
        val request = StudentRequest(name = "", email = "x@puce.edu.ec")

        assertThrows(IllegalArgumentException::class.java) {
            studentService.updateStudent(1L, request)
        }
    }

    @Test
    fun `updateStudent lanza StudentNotFoundException cuando el estudiante no existe`() {
        val request = StudentRequest(name = "Nuevo", email = null)
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        assertThrows(StudentNotFoundException::class.java) {
            studentService.updateStudent(99L, request)
        }
    }

    @Test
    fun `updateStudent retorna StudentResponse actualizado cuando los datos son validos`() {
        val request = StudentRequest(name = "Ana Actualizada", email = "nueva@puce.edu.ec")
        val existing = Student(id = 1L, name = "Ana", email = "ana@puce.edu.ec")
        val updated = Student(id = 1L, name = "Ana Actualizada", email = "nueva@puce.edu.ec")
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(studentRepository.save(any())).thenReturn(updated)

        val result = studentService.updateStudent(1L, request)

        assertEquals("Ana Actualizada", result.name)
        assertEquals("nueva@puce.edu.ec", result.email)
    }

    // ─── deleteStudent ────────────────────────────────────────────────────────

    @Test
    fun `deleteStudent lanza StudentNotFoundException cuando el estudiante no existe`() {
        `when`(studentRepository.existsById(99L)).thenReturn(false)

        assertThrows(StudentNotFoundException::class.java) {
            studentService.deleteStudent(99L)
        }
    }

    @Test
    fun `deleteStudent elimina el estudiante cuando existe`() {
        `when`(studentRepository.existsById(1L)).thenReturn(true)

        studentService.deleteStudent(1L)

        verify(studentRepository).deleteById(1L)
    }
}
