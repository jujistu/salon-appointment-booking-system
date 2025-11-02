package com.apb.salonservice.service;

import com.apb.salonservice.model.Salon;
import com.apb.salonservice.payload.dto.SalonDTO;
import com.apb.salonservice.payload.dto.UserDTO;

import java.util.List;


public interface SalonService {
    Salon createSalon(SalonDTO salonDTO, UserDTO userDTO);
    Salon updateSalon(SalonDTO salonDTO, UserDTO userDTO, Long salonId) throws Exception;
    List<Salon> getAllSalons();
    Salon getSalonById(Long salonId) throws Exception;
    Salon getSalonByOwnerId(Long ownerId);
    List<Salon> searchSalonByCity(String city);
}
