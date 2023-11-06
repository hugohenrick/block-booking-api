package com.hugo.blockbookingapi.repository;

import com.hugo.blockbookingapi.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.property.id = :propertyId AND b.startDate <= :end AND b.endDate >= :start")
    List<Booking> findAllByPropertyAndPeriod(Long propertyId, LocalDate start, LocalDate end);

    @Query("SELECT b FROM Booking b WHERE b.property.id = :propertyId AND b.startDate <= :endDate AND b.endDate >= :startDate")
    List<Booking> findOverlappingBookings(Long propertyId, LocalDate startDate, LocalDate endDate);
}

