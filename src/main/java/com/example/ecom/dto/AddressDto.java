package com.example.ecom.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({
        "addressName",
        "latitude",
        "longitude",
        "isDefaultFlag"
})
public class AddressDto {
    private String addressName;
    private Long latitude  ;
    private Long longitude;
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean isDefaultFlag = false;
}
