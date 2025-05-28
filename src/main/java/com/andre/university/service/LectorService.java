package com.andre.university.service;

import com.andre.university.repository.LectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LectorService {
    private final LectorRepository lectorRepository;

    public LectorService(LectorRepository lectorRepo) {
        this.lectorRepository = lectorRepo;
    }

    public List<String> globalSearch(String tpl) {
        return lectorRepository.globalSearchByName(tpl);
    }
}

