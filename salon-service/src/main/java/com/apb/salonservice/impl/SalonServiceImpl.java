package com.apb.salonservice.impl;

import com.apb.salonservice.model.Salon;
import com.apb.salonservice.payload.dto.SalonDTO;
import com.apb.salonservice.payload.dto.UserDTO;
import com.apb.salonservice.repository.SalonRepository;
import com.apb.salonservice.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;

    @Override
    public Salon createSalon(SalonDTO salonDTO, UserDTO userDTO) {
        Salon salon = new Salon();
        salon.setName(salonDTO.getName());
        salon.setAddress(salonDTO.getAddress());
        salon.setEmail(salonDTO.getEmail());
        salon.setCity(salonDTO.getCity());
        salon.setImages(salonDTO.getImages());
        salon.setOwnerId(userDTO.getId());
        salon.setOpeningTime(salonDTO.getOpeningTime());
        salon.setClosingTime(salonDTO.getClosingTime());
        salon.setPhone(salonDTO.getPhone());

        return salonRepository.save(salon);
    }

    @Override
    public Salon updateSalon(SalonDTO salonDTO, UserDTO userDTO, Long salonId) throws Exception {
        Salon existingSalon = salonRepository.findById(salonId).orElse(null);
        if (!salonDTO.getOwnerId().equals(userDTO.getId())) {
            throw new Exception("You are not allowed to update this salon");
        }
        if (existingSalon != null) {
            existingSalon.setCity(salonDTO.getCity());
            existingSalon.setName(salonDTO.getName());
            existingSalon.setAddress(salonDTO.getAddress());
            existingSalon.setEmail(salonDTO.getEmail());
            existingSalon.setImages(salonDTO.getImages());
            existingSalon.setOwnerId(userDTO.getId());
            existingSalon.setOpeningTime(salonDTO.getOpeningTime());
            existingSalon.setClosingTime(salonDTO.getClosingTime());
            existingSalon.setPhone(salonDTO.getPhone());
            return salonRepository.save(existingSalon);
        }
        throw new Exception("Salon does not exist");
    }

    @Override
    public List<Salon> getAllSalons() {
        return salonRepository.findAll();
    }

    @Override
    public Salon getSalonById(Long salonId) throws Exception {
        Salon salon = salonRepository.findById(salonId).orElse(null);
        if (salon == null) {
            throw new Exception("Salon does not exist");
        }
        return salon;
    }

    @Override
    public Salon getSalonByOwnerId(Long ownerId) {
        return salonRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Salon> searchSalonByCity(String city) {
        return salonRepository.searchSalons(city);
    }
}
