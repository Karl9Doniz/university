package com.andre.university.service;

import com.andre.university.repository.LectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LectorService {
    private final LectorRepository lectorRepo;

    public LectorService(LectorRepository lectorRepo) {
        this.lectorRepo = lectorRepo;
    }

    public List<String> globalSearch(String tpl) {
        return lectorRepo.globalSearchByName(tpl);
    }
}

