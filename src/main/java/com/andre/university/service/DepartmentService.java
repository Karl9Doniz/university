package com.andre.university.service;

import com.andre.university.model.Degree;
import com.andre.university.model.Department;
import com.andre.university.repository.DepartmentRepository;
import com.andre.university.repository.LectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final LectorRepository lectorRepo;

    public DepartmentService(DepartmentRepository departmentRepository, LectorRepository lectorRepo) {
        this.departmentRepository = departmentRepository;
        this.lectorRepo = lectorRepo;
    }

    public String getHeadOfDepartment(String departmentName) {
        Department department = departmentRepository.findByNameIgnoreCase(departmentName)
                .orElseThrow(() -> new NoSuchElementException(departmentName));
        return department.getHead().getName();
    }

    public Map<Degree, Long> getStatistics(String departmentName) {
        EnumMap<Degree, Long> stats = new EnumMap<>(Degree.class);
        for (Degree degree : Degree.values()) {
            long count = lectorRepo.countByDepartmentAndDegree(departmentName, degree);
            stats.put(degree, count);
        }
        return stats;
    }

    public BigDecimal getAverageSalary(String departmentName) {
        return lectorRepo.averageSalaryByDepartment(departmentName);
    }

    public long getEmployeeCount(String departmentName) {
        Map<Degree, Long> stats = getStatistics(departmentName);
        return stats.values().stream().mapToLong(Long::longValue).sum();
    }
}

