package com.puce.bryantaco.students.mappers

import com.puce.bryantaco.students.dto.SubjectResponse
import com.puce.bryantaco.students.entities.Professor
import com.puce.bryantaco.students.entities.Subject

fun Subject.toResponse(): SubjectResponse = SubjectResponse(
    id = id,
    name = name,
    code = code,
    professor = professor!!.toResponse(),
)

fun newSubject(name: String, code: String, professor: Professor): Subject = Subject(
    name = name,
    code = code,
    professor = professor,
)
