package com.hugo.blockbookingapi.service;

import com.hugo.blockbookingapi.exception.BookingConflictException;
import com.hugo.blockbookingapi.model.Booking;
import com.hugo.blockbookingapi.model.Property;
import com.hugo.blockbookingapi.repository.BlockRepository;
import com.hugo.blockbookingapi.repository.BookingRepository;
import com.hugo.blockbookingapi.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BlockRepository blockRepository;

    @Test
    void createBookingTest() {
        Property property = new Property();
        property.setId(1L);
        property.setName("Beach House");

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStartDate(LocalDate.of(2023, 11, 10));
        booking.setEndDate(LocalDate.of(2023, 11, 15));
        booking.setProperty(property);

        when(blockRepository.findAllByPropertyAndPeriod(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());
        
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking createdBooking = bookingService.createBooking(booking);

        assertThat(createdBooking).isNotNull();
        verify(bookingRepository).save(booking);
    }

    @Test
    void updateBookingTest() throws BookingConflictException {
        Long bookingId = 1L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);

        Property property = new Property();
        property.setId(1L);
        property.setName("Beach House");

        Booking existingBooking = new Booking();
        existingBooking.setId(bookingId);
        existingBooking.setStartDate(startDate);
        existingBooking.setEndDate(endDate);
        existingBooking.setProperty(property);

        Booking updatedDetails = new Booking();
        updatedDetails.setStartDate(startDate.plusDays(3));
        updatedDetails.setEndDate(endDate.plusDays(3));
        updatedDetails.setProperty(property);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));
        when(blockRepository.findAllByPropertyAndPeriod(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList()); // Simulate that there are no overlapping blocks
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking updatedBooking = bookingService.updateBooking(bookingId, updatedDetails);

        assertThat(updatedBooking).isNotNull();
        assertThat(updatedBooking.getId()).isEqualTo(existingBooking.getId());
        assertThat(updatedBooking.getStartDate()).isEqualTo(updatedDetails.getStartDate());
        assertThat(updatedBooking.getEndDate()).isEqualTo(updatedDetails.getEndDate());
    }

    @Test
    void deleteBlockTest() {
        Long bookingId = 1L;

        bookingService.deleteBooking(bookingId);

        verify(bookingRepository).deleteById(bookingId);
    }
}

