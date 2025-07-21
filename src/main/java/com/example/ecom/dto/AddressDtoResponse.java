package com.example.ecom.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({
        "addressId",
        "addressName",
        "latitude",
        "longitude",
        "isDefaultFlag"
})
public class AddressDtoResponse {
    private Long addressId;
    private String addressName;
    private Long latitude  ;
    private Long longitude;
    private Boolean isDefaultFlag;
}
