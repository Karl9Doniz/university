package com.andre.university.repository;

import com.andre.university.model.Degree;
import com.andre.university.model.Lector;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LectorRepository extends JpaRepository<Lector, Long> {
    @Query("""
      SELECT COUNT(l)
      FROM Lector l JOIN l.departments d
      WHERE d.name = :department AND l.degree = :degree
    """)
    long countByDepartmentAndDegree(@Param("department") String department, @Param("degree") Degree degree);

    @Query("""
      SELECT AVG(l.salary)
      FROM Lector l JOIN l.departments d
      WHERE d.name = :department
    """)
    BigDecimal averageSalaryByDepartment(@Param("department") String department);

    @Query("""
      SELECT l.name
      FROM Lector l
      WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :template, '%'))
    """)
    List<String> globalSearchByName(@Param("template") String template);
}
