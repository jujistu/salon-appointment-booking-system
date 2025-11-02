package com.apb.salonservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public String HomeControllerHandler(){
        return "Salon Service for Salon Appointment Booking";
    }
}
