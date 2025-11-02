package com.apb.bookingservice.mapper;

import com.apb.bookingservice.dto.BookingDTO;
import com.apb.bookingservice.model.Booking;

public class BookingMapper {

    public static BookingDTO toDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setCustomerId(booking.getCustomerId());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setStartTime(booking.getStartTime());
        bookingDTO.setEndTime(booking.getEndTime());
        bookingDTO.setSalonId(booking.getSalonId());
        bookingDTO.setServiceIds(booking.getServiceIds());
        bookingDTO.setTotalPrice(booking.getTotalPrice());
        return bookingDTO;
    }
}
