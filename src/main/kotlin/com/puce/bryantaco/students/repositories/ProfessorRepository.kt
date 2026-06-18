package com.puce.bryantaco.students.repositories

import com.puce.bryantaco.students.entities.Professor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfessorRepository : JpaRepository<Professor, Long>
