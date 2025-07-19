package com.example.ecom.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LovDto {
    private String code;
    private String label;

    public LovDto(String code, String label) {
        this.code = code;
        this.label = label;
    }

}
