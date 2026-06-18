package com.puce.bryantaco.students.mappers

import com.puce.bryantaco.students.dto.ProfessorRequest
import com.puce.bryantaco.students.dto.ProfessorResponse
import com.puce.bryantaco.students.entities.Professor

fun ProfessorRequest.toEntity(): Professor = Professor(
    name = name,
    email = email,
)

fun Professor.toResponse(): ProfessorResponse = ProfessorResponse(
    id = id,
    name = name,
    email = email,
)
