package com.puce.bryantaco.students.mappers


import com.puce.bryantaco.students.dto.StudentRequest
import com.puce.bryantaco.students.dto.StudentResponse
import com.puce.bryantaco.students.entities.Student

fun StudentRequest.toEntity(): Student {

    return Student(
        name= name,
        email= email,

        )
}

fun Student.toResponse(): StudentResponse {
    return StudentResponse(
        id= id,
        name= name,
        email= email,
    )
}