package com.example.ecom.controller;


import com.example.ecom.Lang.MessageUtil;
import com.example.ecom.auth.CurrentUser;
import com.example.ecom.dto.AddressDto;
import com.example.ecom.dto.AddressDtoResponse;
import com.example.ecom.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    public AddressService  addressService;

    @Autowired
    private MessageUtil messageUtil;

//    @GetMapping("/{userId}")
//    public ResponseEntity<?> getAddressByUserId(@PathVariable Long userId){
//        AddressDto  addressDto =   addressService.readAddressByUserId(userId);
//        Map<String, Object> result = new HashMap<>();
//        result.put("data", addressDto);
//        return ResponseEntity.ok(result);
//    }
    @GetMapping("")
    public ResponseEntity<?> getAddressByUserId(){

        List<AddressDtoResponse>   addressDtoResponse =   addressService.readAddressByUserId();
        Map<String, Object> result = new HashMap<>();
        result.put("data", addressDtoResponse);

        return ResponseEntity.ok(result);
    }
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Map<String, Object>> deleteAddressById(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Map<String, Object>> updateAddressById(@PathVariable Long addressId, @RequestBody AddressDto addressDto) {
        addressService.updateAddress(addressId,addressDto) ;
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User updated successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createAddress(@RequestBody AddressDto  addressDto) {
        AddressDtoResponse  addressDtoResponse =  addressService.createAddress(addressDto);

        Map<String, Object> response = new HashMap<>();
        response.put("data", addressDtoResponse);
        response.put("message", messageUtil.get("user.user_created_success"));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



}
