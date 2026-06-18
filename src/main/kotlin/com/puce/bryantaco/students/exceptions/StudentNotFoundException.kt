package com.puce.bryantaco.students.exceptions

class StudentNotFoundException(id: Long) : RuntimeException("Student with id $id not found")
