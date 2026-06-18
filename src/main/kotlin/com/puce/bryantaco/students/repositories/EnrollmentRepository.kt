package com.puce.bryantaco.students.repositories

import com.puce.bryantaco.students.entities.Enrollment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EnrollmentRepository : JpaRepository<Enrollment, Long>
