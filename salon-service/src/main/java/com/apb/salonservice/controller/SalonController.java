package com.apb.salonservice.controller;

import com.apb.salonservice.mapper.SalonMapper;
import com.apb.salonservice.model.Salon;
import com.apb.salonservice.payload.dto.SalonDTO;
import com.apb.salonservice.payload.dto.UserDTO;
import com.apb.salonservice.service.SalonService;
import com.apb.salonservice.service.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/salons")
@RequiredArgsConstructor
public class SalonController {
    private final SalonService salonService;
    private final UserFeignClient userFeignClient;

    @PostMapping
    public ResponseEntity<SalonDTO> createSalon(@RequestBody SalonDTO salonDTO, @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();

        Salon salon = salonService.createSalon(salonDTO, userDTO);
        SalonDTO responseDTO = SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{salonId}")
    public ResponseEntity<SalonDTO> updateSalon(@PathVariable Long salonId, @RequestBody SalonDTO salonDTO, @RequestHeader("Authorization") String jwt) throws Exception {
        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();
        Salon salon = salonService.updateSalon(salonDTO, userDTO, salonId);
        SalonDTO responseDTO = SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<SalonDTO>> getSalons() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        List<Salon> salons = salonService.getAllSalons();
        List<SalonDTO> salonDTOs = salons.stream()
                .map(SalonMapper::mapToDTO)
                .toList();
        return ResponseEntity.ok(salonDTOs);
    }

    @GetMapping("/{salonId}")
    public ResponseEntity<SalonDTO> getSalonById(@PathVariable Long salonId) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        Salon salon = salonService.getSalonById(salonId);
        SalonDTO salonDTO = SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(salonDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SalonDTO>> searchSalons(@RequestParam("city") String city) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        List<Salon> salons = salonService.searchSalonByCity(city);
        List<SalonDTO> salonDTOs = salons.stream()
                .map(SalonMapper::mapToDTO)
                .toList();
        return ResponseEntity.ok(salonDTOs);
    }

    @GetMapping("/owner")
    public ResponseEntity<SalonDTO> getSalonsByOwnerId(@RequestHeader String jwt) throws Exception {
        UserDTO userDTO = userFeignClient.getUserProfile(jwt).getBody();
        if (userDTO == null) {
            throw new Exception("User with given JWT not found");
        }
        Salon salon = salonService.getSalonByOwnerId(userDTO.getId());
        SalonDTO salonDTO = SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(salonDTO);
    }

}
