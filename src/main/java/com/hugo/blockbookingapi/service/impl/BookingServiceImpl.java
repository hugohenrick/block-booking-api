package com.hugo.blockbookingapi.service.impl;

import com.hugo.blockbookingapi.exception.BookingConflictException;
import com.hugo.blockbookingapi.exception.BookingNotFoundException;
import com.hugo.blockbookingapi.model.Block;
import com.hugo.blockbookingapi.model.Booking;
import com.hugo.blockbookingapi.repository.BlockRepository;
import com.hugo.blockbookingapi.repository.BookingRepository;
import com.hugo.blockbookingapi.service.BookingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BlockRepository blockRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, BlockRepository blockRepository) {
        this.bookingRepository = bookingRepository;
        this.blockRepository = blockRepository;
    }

    @Override
    @Transactional
    public Booking createBooking(Booking booking) {
        if (isOverlappingWithExistingBookings(booking) || isOverlappingWithBlocks(booking)) {
            throw new IllegalStateException("Booking dates are overlapping with existing bookings or blocks.");
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return Optional.ofNullable(bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id)));
    }

    @Override
    @Transactional
    public Booking updateBooking(Long bookingId, Booking updatedBooking) throws BookingConflictException {
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        if (!existingBooking.getStartDate().isEqual(updatedBooking.getStartDate()) ||
                !existingBooking.getEndDate().isEqual(updatedBooking.getEndDate())) {

            if (isOverlappingWithExistingBookings(updatedBooking) || isOverlappingWithBlocks(updatedBooking)) {
                throw new BookingConflictException("The updated dates conflict with existing bookings or blocks.");
            }
        }

        existingBooking.setStartDate(updatedBooking.getStartDate());
        existingBooking.setEndDate(updatedBooking.getEndDate());

        return bookingRepository.save(existingBooking);
    }

    @Override
    @Transactional
    public void deleteBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
        } else {
            throw new BookingNotFoundException("Booking with id " + id + " does not exist");
        }
    }

    private boolean isOverlappingWithExistingBookings(Booking booking) {
        List<Booking> existingBookings = bookingRepository
                .findAllByPropertyAndPeriod(booking.getProperty().getId(), booking.getStartDate(), booking.getEndDate());
        return existingBookings.stream().anyMatch(existing -> !existing.getId().equals(booking.getId()));
    }

    private boolean isOverlappingWithBlocks(Booking booking) {
        List<Block> existingBlocks = blockRepository
                .findAllByPropertyAndPeriod(booking.getProperty().getId(), booking.getStartDate(), booking.getEndDate());
        return !existingBlocks.isEmpty();
    }

}
