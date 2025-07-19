package com.example.ecom.service;

import com.example.ecom.dto.LovDto;
import com.example.ecom.repository.LovRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class LovService {

    @Autowired
    private LovRepository lovRepository;

    public List<LovDto> getLovByType(String type) {
        return lovRepository.findByType(type.toUpperCase() );
    }
}

