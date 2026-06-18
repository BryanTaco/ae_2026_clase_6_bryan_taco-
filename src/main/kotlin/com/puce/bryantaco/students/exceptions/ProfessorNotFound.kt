package com.puce.bryantaco.students.exceptions

class ProfessorNotFound(id: Long) : RuntimeException("Professor with id $id not found")
