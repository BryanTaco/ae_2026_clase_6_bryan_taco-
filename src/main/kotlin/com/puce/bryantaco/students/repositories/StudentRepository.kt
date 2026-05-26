package com.puce.bryantaco.students.repositories

import com.puce.bryantaco.students.entities.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface StudentRepository: JpaRepository<Student, Long>