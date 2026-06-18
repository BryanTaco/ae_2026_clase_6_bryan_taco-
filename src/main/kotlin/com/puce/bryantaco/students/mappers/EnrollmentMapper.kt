package com.puce.bryantaco.students.mappers

import com.puce.bryantaco.students.dto.EnrollmentResponse
import com.puce.bryantaco.students.entities.Enrollment
import com.puce.bryantaco.students.entities.Student
import com.puce.bryantaco.students.entities.Subject
import java.time.LocalDateTime

fun Enrollment.toResponse(): EnrollmentResponse = EnrollmentResponse(
    id = id,
    createdAt = createdAt,
    status = status,
    student = student!!.toResponse(),
    subject = subject!!.toResponse(),
)

fun newEnrollment(student: Student, subject: Subject): Enrollment = Enrollment(
    student = student,
    subject = subject,
    status = "INSCRITO",
    createdAt = LocalDateTime.now(),
)
