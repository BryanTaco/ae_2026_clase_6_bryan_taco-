package com.puce.bryantaco.students.exceptions

class SubjectNotFound(id: Long) : RuntimeException("Subject with id $id not found")
