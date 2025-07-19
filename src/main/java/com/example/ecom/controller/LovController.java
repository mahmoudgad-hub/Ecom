package com.example.ecom.controller;

import com.example.ecom.dto.LovDto;
import com.example.ecom.service.LovService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/lov")
public class LovController {

    @Autowired
    private LovService lovService;

    @GetMapping("/{type}")
    public ResponseEntity<?> getLov(@PathVariable String type) {
        List<LovDto> list = lovService.getLovByType(type);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("timestamp", LocalDateTime.now());
        response.put("data", list);

        return ResponseEntity.ok(response);
    }
}
