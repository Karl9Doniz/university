package com.andre.university;

import com.andre.university.model.Degree;
import com.andre.university.model.Lector;
import com.andre.university.model.Department;
import com.andre.university.repository.DepartmentRepository;
import com.andre.university.repository.LectorRepository;
import com.andre.university.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    DepartmentRepository departmentRepository;

    @Mock
    LectorRepository lectorRepository;

    @InjectMocks
    DepartmentService departmentService;

    @Test
    void getHeadOfDepartment_returnsHeadName() {
        String departmentName = "Mathematics";
        Lector head = Lector.builder()
                .id(42L)
                .name("Daniel Brown")
                .degree(Degree.PROFESSOR)
                .salary(new BigDecimal("140000"))
                .build();
        Department department = Department.builder()
                .id(7L)
                .name(departmentName)
                .head(head)
                .lectors(Set.of(head))
                .build();

        when(departmentRepository.findByNameIgnoreCase(departmentName)).thenReturn(Optional.of(department));

        String result = departmentService.getHeadOfDepartment(departmentName);

        assertEquals("Daniel Brown", result);
    }

    @Test
    void getHeadOfDepartment_notFound_throwsError() {
        String departmentName = "Random";
        when(departmentRepository.findByNameIgnoreCase(departmentName)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> departmentService.getHeadOfDepartment(departmentName));
        assertEquals(departmentName, exception.getMessage());
    }

    @Test
    void getStatistics_returnsCounts() {
        String departmentName = "Physics";

        when(lectorRepository.countByDepartmentAndDegree(departmentName, Degree.ASSISTANT)).thenReturn(2L);
        when(lectorRepository.countByDepartmentAndDegree(departmentName, Degree.ASSOCIATE_PROFESSOR)).thenReturn(1L);
        when(lectorRepository.countByDepartmentAndDegree(departmentName, Degree.PROFESSOR)).thenReturn(3L);

        Map<Degree, Long> stats = departmentService.getStatistics(departmentName);

        assertEquals(2L, stats.get(Degree.ASSISTANT));
        assertEquals(1L, stats.get(Degree.ASSOCIATE_PROFESSOR));
        assertEquals(3L, stats.get(Degree.PROFESSOR));
    }

    @Test
    void getAverageSalary_returnsValue() {
        String departmentName = "Computer Science";
        BigDecimal avgSalary = new BigDecimal("85000.50");

        when(lectorRepository.averageSalaryByDepartment(departmentName)).thenReturn(avgSalary);

        BigDecimal result = departmentService.getAverageSalary(departmentName);

        assertEquals(avgSalary, result);
    }
}
