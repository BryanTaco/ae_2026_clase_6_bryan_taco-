package com.puce.bryantaco.students.repositories

import com.puce.bryantaco.students.entities.Subject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubjectRepository : JpaRepository<Subject, Long>
