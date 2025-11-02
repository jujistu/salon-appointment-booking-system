package com.apb.salonservice.mapper;

import com.apb.salonservice.model.Salon;
import com.apb.salonservice.payload.dto.SalonDTO;

public class SalonMapper {

    public static SalonDTO mapToDTO(Salon salon) {
        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(salon.getId());
        salonDTO.setName(salon.getName());
        salonDTO.setAddress(salon.getAddress());
        salonDTO.setCity(salon.getCity());
        salonDTO.setImages(salon.getImages());
        salonDTO.setOpeningTime(salon.getOpeningTime());
        salonDTO.setClosingTime(salon.getClosingTime());
        salonDTO.setPhone(salon.getPhone());
        salonDTO.setEmail(salon.getEmail());
        salonDTO.setOwnerId(salon.getOwnerId());
        return salonDTO;
    }
}
