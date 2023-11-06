package com.hugo.blockbookingapi.repository;

import com.hugo.blockbookingapi.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
    @Query("SELECT b FROM Block b WHERE b.property.id = :propertyId AND b.startDate <= :end AND b.endDate >= :start")
    List<Block> findAllByPropertyAndPeriod(Long propertyId, LocalDate start, LocalDate end);
}
