package com.puce.bryantaco.students.exceptions

class EnrollmentNotFound(id: Long) : RuntimeException("Enrollment with id $id not found")
