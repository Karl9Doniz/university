package com.andre.university;

import com.andre.university.repository.LectorRepository;
import com.andre.university.service.LectorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LectorServiceTest {

    @Mock
    LectorRepository lectorRepository;

    @InjectMocks
    LectorService lectorService;

    @Test
    void globalSearch_returnsMatchingNames() {
        String template = "van";
        List<String> expected = List.of("Ivan Petrenko", "Petro Ivanov");

        when(lectorRepository.globalSearchByName(template)).thenReturn(expected);

        List<String> result = lectorService.globalSearch(template);

        assertEquals(2, result.size());
        assertTrue(result.contains("Ivan Petrenko"));
        assertTrue(result.contains("Petro Ivanov"));
    }
}
