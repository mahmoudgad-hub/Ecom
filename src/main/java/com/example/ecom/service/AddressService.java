package com.example.ecom.service;

import com.example.ecom.Lang.MessageUtil;
import com.example.ecom.auth.CurrentUser;
import com.example.ecom.dto.AddressDto;
import com.example.ecom.dto.AddressDtoResponse;
import com.example.ecom.entity.AddressEntity;
import com.example.ecom.exception.user.UserExistException;
import com.example.ecom.exception.user.UserNotFoundException;
import com.example.ecom.repository.AddressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private MessageUtil messageUtil;



    public List<AddressDtoResponse> readAddressByUserId() {
        Long userId = CurrentUser.getUserId();
        List<AddressEntity> addressEntityList = addressRepo.findByUserId(userId);
        if (addressEntityList.isEmpty()) {
            throw new UserNotFoundException("NO DATA FOUND USER");
        }



        return addressEntityList.stream()
                .map(entity -> {
                    AddressDtoResponse addressDtoResponse = new AddressDtoResponse();
                    addressDtoResponse.setAddressId(entity.getAddressId());
                    addressDtoResponse.setAddressName(entity.getAddressName());
                    addressDtoResponse.setLatitude(entity.getLatitude());
                    addressDtoResponse.setLongitude(entity.getLongitude());
                    addressDtoResponse.setIsDefaultFlag(entity.getIsDefaultFlag());
                    return addressDtoResponse;
                })
                .collect(Collectors.toList());
    }


    public AddressDtoResponse createAddress(AddressDto addressDto) {

        AddressEntity addressEntity = mapToEntity(addressDto);
        if (addressDto.getIsDefaultFlag().equals(true)){
            setDefaultAddressValueN();
        }
        addressRepo.save(addressEntity);
        return mapToDtoResponse(addressEntity);
    }

    public void updateAddress(Long addressId, AddressDto addressDto) {
        AddressEntity addressEntity = addressRepo.findById(addressId)
                .orElseThrow(() -> new UserExistException(messageUtil.get("user.user_not_found")));

        addressEntity.setAddressName(addressDto.getAddressName());
        addressEntity.setLatitude(addressDto.getLatitude());
        addressEntity.setLongitude(addressDto.getLongitude());
        if (addressDto.getIsDefaultFlag().equals(true)){
            setDefaultAddressValueN();
        }

        addressRepo.save(addressEntity);
    }

    public void deleteAddress(Long addressId) {
        AddressEntity addressEntity = addressRepo.findById(addressId)
                .orElseThrow(() -> new UserNotFoundException(messageUtil.get("user.user_not_found")));
        addressRepo.delete(addressEntity);
    }


    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = Exception.class
    )
    public void setDefaultAddressValueN() {
        Long userId = CurrentUser.getUserId();
        List<AddressEntity> currentDefaults = addressRepo.findByUserIdAndIsDefaultFlag(userId, true);
        for (AddressEntity address : currentDefaults) {
            address.setIsDefaultFlag(false);
        }
        addressRepo.saveAll(currentDefaults);

//        AddressEntity newDefault = addressRepo.findById(newAddressId)
//                .orElseThrow(() -> new RuntimeException("Address not found"));
//        newDefault.setIsDefaultFlag(true);
//        addressRepo.save(newDefault);
    }


    private AddressEntity mapToEntity(AddressDto addressDto) {
        AddressEntity addressEntity = new AddressEntity();

      //  addressEntity.setUserId(addressDto.getUserId());
        addressEntity.setAddressName(addressDto.getAddressName());
        addressEntity.setLongitude(addressDto.getLongitude());
        addressEntity.setLatitude(addressDto.getLatitude());
        addressEntity.setIsDefaultFlag(addressDto.getIsDefaultFlag());

        return addressEntity;
    }

    private AddressDtoResponse mapToDtoResponse(AddressEntity  addressEntity) {
        AddressDtoResponse  addressDtoResponse = new AddressDtoResponse();
        addressDtoResponse.setAddressId(addressEntity.getAddressId());
        addressDtoResponse.setAddressName(addressEntity.getAddressName());
        addressDtoResponse.setLongitude(addressEntity.getLongitude());
        addressDtoResponse.setLatitude(addressEntity.getLatitude());
        addressDtoResponse.setIsDefaultFlag(addressEntity.getIsDefaultFlag());

        return addressDtoResponse;
    }


}
