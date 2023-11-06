package com.hugo.blockbookingapi.service;

import com.hugo.blockbookingapi.exception.BookingConflictException;
import com.hugo.blockbookingapi.model.Booking;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface BookingService {
    Booking createBooking(Booking booking);

    Optional<Booking> getBookingById(Long id);

    Booking updateBooking(Long id, Booking booking) throws BookingConflictException;

    void deleteBooking(Long id);
}
