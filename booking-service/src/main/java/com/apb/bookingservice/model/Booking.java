package com.apb.bookingservice.model;

import com.apb.bookingservice.domain.BookingStatus;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
public class Booking {

    @Id
    @GeneratedValue
    private Long id;

    private Long salonId;;

    private Long customerId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ElementCollection
    private Set<Long> serviceIds;

    private BookingStatus status = BookingStatus.PENDING;

    private double totalPrice;

}
